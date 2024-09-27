package uz.ccrew.assignmentservice.assignment.service;

import uz.ccrew.assignmentservice.assignment.dto.AssignmentCancelDTO;
import uz.ccrew.assignmentservice.assignment.dto.WithdrawDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentCreateDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentColumnsDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentCompleteDTO;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentStatusChangeDTO;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface AssignmentService {
    Page<AssignmentSummaryDTO> getSummary(int page, int size);

    AssignmentDetailedDTO getDetailed(Long id);

    Map<String, String> getAllCategories();

    AssignmentColumnsDTO getColumns(String category);

    AssignmentSummaryDTO createAssignment(AssignmentCreateDTO assignmentCreateDTO);

    AssignmentSummaryDTO withdrawAgain(WithdrawDTO dto);

    void cancel(AssignmentCancelDTO dto);

    void changeStatus(AssignmentStatusChangeDTO dto);

    void complete(AssignmentCompleteDTO dto);
}
