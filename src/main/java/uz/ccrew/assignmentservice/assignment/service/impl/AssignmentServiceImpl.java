package uz.ccrew.assignmentservice.assignment.service.impl;

import uz.ccrew.assignmentservice.assignment.AssignmentFullDTO;
import uz.ccrew.assignmentservice.user.User;
import uz.ccrew.assignmentservice.user.UserRole;
import uz.ccrew.assignmentservice.base.AuthUtil;
import uz.ccrew.assignmentservice.assignment.dto.*;
import uz.ccrew.assignmentservice.payment.PaymentType;
import uz.ccrew.assignmentservice.user.UserRepository;
import uz.ccrew.assignmentservice.exp.NotFoundException;
import uz.ccrew.assignmentservice.payment.PaymentService;
import uz.ccrew.assignmentservice.exp.BadRequestException;
import uz.ccrew.assignmentservice.assignment.enums.Category;
import uz.ccrew.assignmentservice.chat.service.MessageService;
import uz.ccrew.assignmentservice.assignment.AssignmentMapper;
import uz.ccrew.assignmentservice.chat.service.ChatUserService;
import uz.ccrew.assignmentservice.assignment.entity.Assignment;
import uz.ccrew.assignmentservice.notifcation.NotificationService;
import uz.ccrew.assignmentservice.assignment.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.assignment.service.AssignmentService;
import uz.ccrew.assignmentservice.assignment.entity.RequisiteAssignment;
import uz.ccrew.assignmentservice.assignment.repository.AssignmentRepository;
import uz.ccrew.assignmentservice.assignment.service.AssignmentCreateService;
import uz.ccrew.assignmentservice.assignment.repository.RequisiteAssignmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AuthUtil authUtil;
    private final MessageService messageService;
    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final ChatUserService chatUserService;
    private final AssignmentMapper assignmentMapper;
    private final NotificationService notificationService;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentCreateService assignmentCreateService;
    private final RequisiteAssignmentRepository requisiteAssignmentRepository;

    @Override
    public Page<AssignmentSummaryDTO> getSummary(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        User user = authUtil.loadLoggedUser();

        Page<Assignment> assignments = assignmentRepository.findAllByCreatedBy_Id(user.getId(), pageable);
        List<AssignmentSummaryDTO> assignmentSummaries = assignmentMapper.toDTOList(assignments.getContent());

        return new PageImpl<>(assignmentSummaries, pageable, assignments.getTotalElements());
    }

    @Override
    public AssignmentDetailedDTO getDetailed(Long assignmentId) {
        User user = authUtil.loadLoggedUser();
        Assignment assignment = assignmentRepository.loadById(assignmentId, "Assignment not found");

        Optional<AssignmentDetailedDTO> optional = assignmentRepository.findAssignmentDetailedByIdAndUserId(user.getId(), assignmentId);
        if (optional.isEmpty()) {
            throw new NotFoundException("Detailed Assignment Not Found");
        }

        AssignmentDetailedDTO detailedDTO = optional.get();
        detailedDTO.setComments(messageService.getList(assignment.getChat().getChatId()));

        return detailedDTO;
    }

    @Override
    public Map<String, String> getAllCategories() {
        return Arrays.stream(Category.values())
                .collect(Collectors.toMap(Category::getFullForm, Category::getDescription));
    }

    @Override
    public AssignmentColumnsDTO getColumns(String fullForm) {
        Category category = Category.fullFormOf(fullForm);
        if (category == null) {
            throw new BadRequestException("Category Not Found");
        }

        return AssignmentColumnsDTO.builder()
                .columns(category.getColumns())
                .build();
    }

    @Transactional
    @Override
    public AssignmentSummaryDTO createAssignment(AssignmentCreateDTO dto) {
        RequisiteAssignment requisiteAssignment = assignmentCreateService.create(dto);
        Assignment assignment = requisiteAssignment.getAssignment();

        withdraw(requisiteAssignment);

        return AssignmentSummaryDTO.builder()
                .assigmentId(assignment.getAssignmentId())
                .date(assignment.getCreatedOn())
                .category(assignment.getCategory())
                .status(assignment.getStatus())
                .build();
    }

    @Transactional
    @Override
    public AssignmentSummaryDTO withdrawAgain(WithdrawDTO dto) {
        RequisiteAssignment requisiteAssignment = requisiteAssignmentRepository.loadById(dto.assignmentId(), "Assignment not found");
        Assignment assignment = requisiteAssignment.getAssignment();

        if (!assignment.getStatus().equals(AssignmentStatus.IN_REVIEW)) {
            throw new BadRequestException("Already payed for this assignment");
        }

        PaymentType paymentType = assignmentCreateService.getPaymentType(dto.cardNumber(), dto.accountNumber());
        requisiteAssignment.setPaymentType(paymentType);
        requisiteAssignment.setCardNumber(dto.cardNumber());
        requisiteAssignment.setAccountNumber(dto.accountNumber());
        requisiteAssignmentRepository.save(requisiteAssignment);

        withdraw(requisiteAssignment);

        return AssignmentSummaryDTO.builder()
                .assigmentId(assignment.getAssignmentId())
                .date(assignment.getCreatedOn())
                .category(assignment.getCategory())
                .status(assignment.getStatus())
                .build();
    }

    public void withdraw(RequisiteAssignment requisiteAssignment) {
        User user = authUtil.loadLoggedUser();
        String paymentId = paymentService.withdraw(requisiteAssignment);
        if (paymentId == null) {
            notificationService.sendNotification(user.getLogin(), "Списание средств было неуспешно, попробуйте снова");
        } else {
            requisiteAssignment.setPaymentId(paymentId);
            requisiteAssignmentRepository.save(requisiteAssignment);

            Assignment assignment = requisiteAssignment.getAssignment();
            assignment.setStatus(AssignmentStatus.ACCEPTED);
            assignmentRepository.save(assignment);

            notificationService.sendNotification(user.getLogin(), "Ваша заявка на  поручение принято в работу");
            List<String> employees = userRepository.findEmployeesAndManager();
            notificationService.sendNotification(employees, "У Вас новое поручение на исполнение");
        }
    }

    @Transactional
    @Override
    public void cancel(AssignmentCancelDTO dto) {
        Assignment assignment = assignmentRepository.loadById(dto.assignmentId(), "Assigment not found");
        if (!assignment.getStatus().equals(AssignmentStatus.ACCEPTED)) {
            throw new BadRequestException("You can't cancel assigment which status is not ACCEPTED");
        }
        User user = authUtil.loadLoggedUser();
        if (assignment.getEmployeeId().equals(user.getId())) {
            throw new BadRequestException("You cant change the assignment status. Because you are not assigned employee");
        }

        RequisiteAssignment requisite = requisiteAssignmentRepository.loadById(dto.assignmentId(), "Assigment requisite not found");

        paymentService.reverse(requisite.getPaymentId());

        assignment.setStatus(AssignmentStatus.CANCELLED);
        assignment.setNote(dto.note());
        assignmentRepository.save(assignment);

        notificationService.sendNotification(assignment.getCreatedBy().getLogin(),
                "В Вашем поручении было отказано и средства вернуться через ххх дней \n" + assignment.getNote());
    }

    @Override
    public void changeStatus(AssignmentStatusChangeDTO dto) {
        Assignment assignment = assignmentRepository.loadById(dto.assignmentId(), "Assignment not found");

        User user = authUtil.loadLoggedUser();
        if (user.getRole().equals(UserRole.EMPLOYEE)) {
            if (assignment.getEmployeeId().equals(user.getId())) {
                throw new BadRequestException("You cant change the assignment status. Because you are not assigned employee");
            }
            if (!assignment.getStatus().equals(AssignmentStatus.ACCEPTED)) {
                throw new BadRequestException("Employee can't change assigment status which status is not ACCEPTED");
            }
            if (!dto.status().equals(AssignmentStatus.IN_PROGRESS)) {
                throw new BadRequestException("Employee can change assignment status only to IN_PROGRESS");
            }
            assignment.setProgressStartedOn(LocalDateTime.now());

            notificationService.sendNotification(assignment.getCreatedBy().getLogin(), "Ваше поручение в процессе подготовки");
        } else {
            if (!assignment.getStatus().equals(AssignmentStatus.CANCELLED)) {
                throw new BadRequestException("Manager can't change assigment status which status is not CANCELLED");
            }
            if (dto.status().equals(AssignmentStatus.IN_PROGRESS)) {
                assignment.setProgressStartedOn(LocalDateTime.now());
            }
        }
        assignment.setStatus(dto.status());
        assignmentRepository.save(assignment);
    }

    @Override
    public void complete(AssignmentCompleteDTO dto) {
        Assignment assignment = assignmentRepository.loadById(dto.assignmentId(), "Assignment not found");
        if (!assignment.getStatus().equals(AssignmentStatus.IN_PROGRESS)) {
            throw new BadRequestException("Assignment can be complete only when status is IN_PROGRESS");
        }
        User user = authUtil.loadLoggedUser();
        if (assignment.getEmployeeId().equals(user.getId())) {
            throw new BadRequestException("You cant change the assignment status, Because you are not assigned employee");
        }

        assignment.setResponseFileId(UUID.fromString(dto.fileId()));
        assignment.setNote(dto.note());
        assignment.setStatus(AssignmentStatus.SUCCESS);

        assignmentRepository.save(assignment);

        notificationService.sendNotification(assignment.getCreatedBy().getLogin(), "Ваше поручение выполнено и готово к скачиванию");
    }

    @Override
    public void assignEmployee(AssignEmployeeDTO dto) {
        Assignment assignment = assignmentRepository.loadById(dto.assignmentId(), "Assignment not found");

        User user = authUtil.loadLoggedUser();
        User employee;
        if (user.getRole().equals(UserRole.MANAGER)) {
            employee = userRepository.loadById(dto.employeeId(), "Employee not found");
            notificationService.sendNotification(employee.getLogin(), "У Вас новое поручение на исполнение");
        } else {
            if (assignment.getEmployee() != null) {
                throw new BadRequestException("The assignment already assigned to employee");
            }
            employee = user;
        }
        assignment.setEmployeeId(employee.getId());
        assignmentRepository.save(assignment);

        chatUserService.addUserToChat(employee, assignment.getChat());
    }

    @Override
    public Page<AssignmentShortDTO> getList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());

        Page<AssignmentShortDTO> assignments = assignmentRepository.getAssigmentShortPage(pageable);

        return new PageImpl<>(assignments.getContent(), pageable, assignments.getTotalElements());
    }

    @Override
    public AssignmentFullDTO getFull(Long assignmentId) {
        Assignment assignment = assignmentRepository.loadById(assignmentId, "Assignment not found");
        User user = assignment.getCreatedBy();

        List<String> fileUrls = new ArrayList<>();


        return AssignmentFullDTO.builder()
                .category(assignment.getCategory())
                .userId(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getLogin())
                .email(user.getEmail())
                .assignmentId(assignmentId)
                .createdOn(assignment.getCreatedOn())
                .progressStartedOn(assignment.getProgressStartedOn())
                .fileUrls(fileUrls)
                .status(assignment.getStatus())
                .build();
    }

}
