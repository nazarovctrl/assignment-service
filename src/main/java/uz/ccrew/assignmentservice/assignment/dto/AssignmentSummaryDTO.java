package uz.ccrew.assignmentservice.assignment.dto;

import uz.ccrew.assignmentservice.assignment.enums.Category;
import uz.ccrew.assignmentservice.assignment.enums.AssignmentStatus;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AssignmentSummaryDTO(Long assigmentId,
                                   Category category,
                                   LocalDateTime date,
                                   AssignmentStatus status) {}
