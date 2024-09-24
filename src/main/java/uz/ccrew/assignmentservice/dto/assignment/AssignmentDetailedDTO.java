package uz.ccrew.assignmentservice.dto.assignment;

import uz.ccrew.assignmentservice.enums.Category;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;

import java.time.LocalDateTime;

public record AssignmentDetailedDTO(Long assigmentId,
                                    Category category,
                                    LocalDateTime date,
                                    AssignmentStatus status,
                                    Long paymentAmount,
                                    String details,
                                    String note) {}
