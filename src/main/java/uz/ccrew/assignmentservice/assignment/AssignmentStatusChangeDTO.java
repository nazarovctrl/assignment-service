package uz.ccrew.assignmentservice.assignment;

import uz.ccrew.assignmentservice.enums.AssignmentStatus;

import jakarta.validation.constraints.NotNull;

public record AssignmentStatusChangeDTO(@NotNull(message = "Invalid assigmentId")
                                        Long assignmentId,
                                        @NotNull(message = "Invalid status")
                                        AssignmentStatus status) {
}
