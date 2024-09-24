package uz.ccrew.assignmentservice.service;

import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface AssignmentService {
    Page<AssignmentSummaryDTO> findAllAssignments(int page, int size);

    Map<String, String> getAllCategories();
}
