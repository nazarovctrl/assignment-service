package uz.ccrew.assignmentservice.assignment.dto;

import uz.ccrew.assignmentservice.assignment.enums.Category;
import uz.ccrew.assignmentservice.assignment.enums.AssignmentStatus;

import java.time.LocalDateTime;

public record AssignmentDetailedDTO(Long assigmentId,
                                    Category category,
                                    LocalDateTime date,
                                    AssignmentStatus status,
                                    Long paymentAmount,
                                    String details,
                                    String note) {}
