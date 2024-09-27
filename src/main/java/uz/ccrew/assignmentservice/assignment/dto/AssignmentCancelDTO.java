package uz.ccrew.assignmentservice.assignment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record AssignmentCancelDTO(@NotNull(message = "Invalid assignmentId")
                                  Long assignmentId,
                                  @NotBlank(message = "Invalid node")
                                  String note) {
}
