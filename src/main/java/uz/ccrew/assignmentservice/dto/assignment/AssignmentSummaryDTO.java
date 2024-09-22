package uz.ccrew.assignmentservice.dto.assignment;

import uz.ccrew.assignmentservice.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.enums.Category;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AssignmentSummaryDTO(Category category,
                                   LocalDateTime date,
                                   AssignmentStatus status) {}
