package uz.ccrew.assignmentservice.service.impl;

import uz.ccrew.assignmentservice.chat.entity.Chat;
import uz.ccrew.assignmentservice.chat.repository.ChatRepository;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentCreateDTO;
import uz.ccrew.assignmentservice.entity.*;
import uz.ccrew.assignmentservice.enums.TransferType;
import uz.ccrew.assignmentservice.repository.*;
import uz.ccrew.assignmentservice.util.AuthUtil;
import uz.ccrew.assignmentservice.enums.UserRole;
import uz.ccrew.assignmentservice.enums.Category;
import uz.ccrew.assignmentservice.exp.NotFoundException;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.payment.PaymentService;
import uz.ccrew.assignmentservice.exp.BadRequestException;
import uz.ccrew.assignmentservice.mapper.AssignmentMapper;
import uz.ccrew.assignmentservice.service.AssignmentService;
import uz.ccrew.assignmentservice.assignment.AssignmentCancelDTO;
import uz.ccrew.assignmentservice.notifcation.NotificationService;
import uz.ccrew.assignmentservice.assignment.AssignmentCompleteDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentColumnsDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;
import uz.ccrew.assignmentservice.assignment.AssignmentStatusChangeDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AuthUtil authUtil;
    private final PaymentService paymentService;
    private final AssignmentMapper assignmentMapper;
    private final NotificationService notificationService;
    private final AssignmentRepository assignmentRepository;
    private final RequisiteAssignmentRepository requisiteAssignmentRepository;
    private final SwiftTransferAssignmentRepository swiftTransferAssignmentRepository;
    private final TransferAssignmentRepository transferAssignmentRepository;
    private final CertificateAssignmentRepository certificateAssignmentRepository;
    private final ChatRepository chatRepository;

    @Override
    public Page<AssignmentSummaryDTO> getSummary(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        User user = authUtil.loadLoggedUser();

        Page<Assignment> assignments = assignmentRepository.findAllByCreatedBy_Id(user.getId(), pageable);
        List<AssignmentSummaryDTO> assignmentSummaries = assignmentMapper.toDTOList(assignments.getContent());

        return new PageImpl<>(assignmentSummaries, pageable, assignments.getTotalElements());
    }

    @Override
    public AssignmentDetailedDTO getDetailed(Long id) {
        User user = authUtil.loadLoggedUser();
        Optional<AssignmentDetailedDTO> detailedDTO = assignmentRepository.findAssignmentDetailedByIdAndUserId(user.getId(), id);
        if (detailedDTO.isEmpty()) {
            throw new NotFoundException("Detailed Assignment Not Found");
        }
        return detailedDTO.get();
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

    @Override
    public AssignmentCreateDTO createAssignment(AssignmentCreateDTO assignmentCreateDTO) {
        User user = authUtil.loadLoggedUser();

        Chat chat = Chat.builder()
                .chatId(UUID.fromString(assignmentCreateDTO.chatId()))
                .build();
        chatRepository.save(chat);

        Assignment assignment = Assignment.builder()
                .fileId(UUID.fromString(assignmentCreateDTO.fileId()))
                .category(Category.fullFormOf(assignmentCreateDTO.category()))
                .status(AssignmentStatus.IN_REVIEW)
                .details(assignmentCreateDTO.details())
                .chatId(UUID.fromString(assignmentCreateDTO.chatId()))
                .build();
        assignment.setCreatedBy(user);
        assignmentRepository.save(assignment);

        if (assignmentCreateDTO.category().equals(Category.SWIFT_PHYSICAL.getFullForm())) {
            TransferAssignment transferAssignment = TransferAssignment.builder()
                    .assignmentId(assignment.getAssigmentId())
                    .receiverCountry(assignmentCreateDTO.receiverCountry())
                    .amount(assignmentCreateDTO.amount())
                    .receiverFullName(assignmentCreateDTO.fullName())
                    .build();
            transferAssignmentRepository.save(transferAssignment);

            SwiftTransferAssignment swiftTransferAssignment = SwiftTransferAssignment.builder()
                    .assignmentId(assignment.getAssigmentId())
                    .accountNumber(assignmentCreateDTO.accountNumber())
                    .swiftCode(assignmentCreateDTO.swiftCode())
                    .build();
            swiftTransferAssignmentRepository.save(swiftTransferAssignment);

            RequisiteAssignment requisiteAssignment = RequisiteAssignment.builder()
                    .assignmentId(assignment.getAssigmentId())
                    .accountNumber(assignmentCreateDTO.accountNumber())
                    .paymentAmount(assignmentCreateDTO.amount())
                    .build();
            requisiteAssignmentRepository.save(requisiteAssignment);

            return assignmentCreateDTO;

        } else if (assignmentCreateDTO.category().equals(Category.SWIFT_LEGAL.getFullForm())) {
            TransferAssignment transferAssignment = TransferAssignment.builder()
                    .assignmentId(assignment.getAssigmentId())
                    .receiverCountry(assignmentCreateDTO.receiverCountry())
                    .amount(assignmentCreateDTO.amount())
                    .build();
            transferAssignmentRepository.save(transferAssignment);

            SwiftTransferAssignment swiftTransferAssignment = SwiftTransferAssignment.builder()
                    .assignmentId(assignment.getAssigmentId())
                    .accountNumber(assignmentCreateDTO.accountNumber())
                    .swiftCode(assignmentCreateDTO.swiftCode())
                    .legalPersonAddress(assignmentCreateDTO.legalPersonAddress())
                    .receiverOrganizationName(assignmentCreateDTO.receiverOrganizationName())
                    .build();
            swiftTransferAssignmentRepository.save(swiftTransferAssignment);

            RequisiteAssignment requisiteAssignment = RequisiteAssignment.builder()
                    .assignmentId(assignment.getAssigmentId())
                    .accountNumber(assignmentCreateDTO.accountNumber())
                    .paymentAmount(assignmentCreateDTO.amount())
                    .build();
            requisiteAssignmentRepository.save(requisiteAssignment);

        } else if (assignmentCreateDTO.category().equals(Category.INTERNATIONAL_TRANSFER.getFullForm())) {
            TransferAssignment transferAssignment = TransferAssignment.builder()
                    .assignmentId(assignment.getAssigmentId())
                    .receiverCountry(assignmentCreateDTO.receiverCountry())
                    .receiverFullName(assignmentCreateDTO.fullName())
                    .phoneNumber(assignmentCreateDTO.phoneNumber())
                    .amount(assignmentCreateDTO.amount())
                    .type((TransferType.valueOf(assignmentCreateDTO.transferType())))
                    .build();
            transferAssignmentRepository.save(transferAssignment);

            RequisiteAssignment requisiteAssignment = RequisiteAssignment.builder()
                    .assignmentId(assignment.getAssigmentId())
                    .paymentAmount(assignmentCreateDTO.amount())
                    .build();
            requisiteAssignmentRepository.save(requisiteAssignment);

        } else if (assignmentCreateDTO.category().equals(Category.CERTIFICATES.getFullForm())) {
            CertificateAssignment certificateAssignment = CertificateAssignment.builder()
                    .assignmentId(assignment.getAssigmentId())
                    .beginDate(assignmentCreateDTO.beginDate())
                    .endDate(assignmentCreateDTO.endDate())
                    .build();
            certificateAssignmentRepository.save(certificateAssignment);

            RequisiteAssignment requisiteAssignment = RequisiteAssignment.builder()
                    .assignmentId(assignment.getAssigmentId())
                    .build();
            requisiteAssignmentRepository.save(requisiteAssignment);
        } else if (assignmentCreateDTO.category().equals(Category.DISPUTE.getFullForm())) {

        }

        return null;
    }

    @Transactional
    @Override
    public void cancel(AssignmentCancelDTO dto) {
        Assignment assignment = assignmentRepository.loadById(dto.assignmentId(), "Assigment not found");
        if (!assignment.getStatus().equals(AssignmentStatus.ACCEPTED)) {
            throw new BadRequestException("You can't cancel assigment which status is not ACCEPTED");
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
            if (!assignment.getStatus().equals(AssignmentStatus.ACCEPTED)) {
                throw new BadRequestException("Employee can't change assigment status which status is not ACCEPTED");
            }
            if (!dto.status().equals(AssignmentStatus.IN_PROGRESS)) {
                throw new BadRequestException("Employee can change assignment status only to IN_PROGRESS");
            }
            notificationService.sendNotification(assignment.getCreatedBy().getLogin(), "Ваше поручение в процессе подготовки");
        } else {
            if (!assignment.getStatus().equals(AssignmentStatus.CANCELLED)) {
                throw new BadRequestException("Manager can't change assigment status which status is not CANCELLED");
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
        assignment.setResponseFileId(UUID.fromString(dto.fileId()));
        assignment.setNote(dto.note());
        assignment.setStatus(AssignmentStatus.SUCCESS);

        assignmentRepository.save(assignment);

        notificationService.sendNotification(assignment.getCreatedBy().getLogin(), "Ваше поручение выполнено и готово к скачиванию");
    }
}
