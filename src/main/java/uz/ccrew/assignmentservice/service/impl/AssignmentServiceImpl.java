package uz.ccrew.assignmentservice.service.impl;

import uz.ccrew.assignmentservice.entity.User;
import uz.ccrew.assignmentservice.util.AuthUtil;
import uz.ccrew.assignmentservice.enums.UserRole;
import uz.ccrew.assignmentservice.entity.Assignment;
import uz.ccrew.assignmentservice.exp.NotFoundException;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.payment.PaymentService;
import uz.ccrew.assignmentservice.exp.BadRequestException;
import uz.ccrew.assignmentservice.mapper.AssignmentMapper;
import uz.ccrew.assignmentservice.service.AssignmentService;
import uz.ccrew.assignmentservice.entity.RequisiteAssignment;
import uz.ccrew.assignmentservice.assignment.AssignmentCancelDTO;
import uz.ccrew.assignmentservice.repository.AssignmentRepository;
import uz.ccrew.assignmentservice.notifcation.NotificationService;
import uz.ccrew.assignmentservice.assignment.AssignmentCompleteDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;
import uz.ccrew.assignmentservice.assignment.AssignmentStatusChangeDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO;
import uz.ccrew.assignmentservice.repository.RequisiteAssignmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AuthUtil authUtil;
    private final PaymentService paymentService;
    private final AssignmentMapper assignmentMapper;
    private final NotificationService notificationService;
    private final AssignmentRepository assignmentRepository;
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
        Map<String, String> categories = new HashMap<>();
        categories.put("swiftPhysical", "SWIFT transfers for physical");
        categories.put("swiftForLegalEntities", "SWIFT transfers for legal entities");
        categories.put("internationalTransfers", "International transfers");
        categories.put("certificates", "Certificate transfers");
        categories.put("cardRefresh", "Card reissue");
        categories.put("dispute", "Open a dispute");
        categories.put("others", "Others");

        return categories;
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
