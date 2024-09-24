package uz.ccrew.assignmentservice.assignment;

import jakarta.validation.constraints.NotNull;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;

public record AssignmentStatusChangeDTO(@NotNull(message = "Invalid assigmentId")
                                        Long assignmentId,
                                        @NotNull(message = "Invalid status")
                                        AssignmentStatus status) {
}
