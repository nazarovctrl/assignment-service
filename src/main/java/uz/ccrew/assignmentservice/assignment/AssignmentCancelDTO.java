package uz.ccrew.assignmentservice.assignment;

import jakarta.validation.constraints.NotNull;

public record AssignmentCancelDTO(@NotNull(message = "Invalid assignmentId")
                                  Long assignmentId,
                                  @NotNull(message = "Invalid node")
                                  String note) {
}
