package uz.ccrew.assignmentservice.service;

import uz.ccrew.assignmentservice.assignment.AssignmentCancelDTO;
import uz.ccrew.assignmentservice.assignment.AssignmentCompleteDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO;
import uz.ccrew.assignmentservice.assignment.AssignmentStatusChangeDTO;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface AssignmentService {
    Page<AssignmentSummaryDTO> getSummary(int page, int size);

    AssignmentDetailedDTO getDetailed(Long id);

    Map<String, String> getAllCategories();

    void cancel(AssignmentCancelDTO dto);

    void changeStatus(AssignmentStatusChangeDTO dto);

    void complete(AssignmentCompleteDTO dto);
}
