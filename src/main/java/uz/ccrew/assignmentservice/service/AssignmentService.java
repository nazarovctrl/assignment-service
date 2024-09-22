package uz.ccrew.assignmentservice.service;

import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;

import org.springframework.data.domain.Page;

public interface AssignmentService {
    Page<AssignmentSummaryDTO> findAllAssignments(int page, int size);
}
