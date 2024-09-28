package uz.ccrew.assignmentservice.assignment.service;

import uz.ccrew.assignmentservice.assignment.dto.*;

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

    void assignEmployee(AssignEmployeeDTO dto);
}
