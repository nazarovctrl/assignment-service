package uz.ccrew.assignmentservice.service;

import uz.ccrew.assignmentservice.assignment.AssignmentCancelDTO;
import uz.ccrew.assignmentservice.assignment.AssignmentCompleteDTO;
import uz.ccrew.assignmentservice.assignment.AssignmentStatusChangeDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;

import org.springframework.data.domain.Page;

public interface AssignmentService {
    Page<AssignmentSummaryDTO> findAllAssignments(int page, int size);

    void cancel(AssignmentCancelDTO dto);

    void changeStatus(AssignmentStatusChangeDTO dto);

    void complete(AssignmentCompleteDTO dto);
}
