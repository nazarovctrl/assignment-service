package uz.ccrew.assignmentservice.service;

import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO;

import org.springframework.data.domain.Page;

public interface AssignmentService {
    Page<AssignmentSummaryDTO> getSummaryList(int page, int size);

    AssignmentDetailedDTO getDetailed(Long id);
}
