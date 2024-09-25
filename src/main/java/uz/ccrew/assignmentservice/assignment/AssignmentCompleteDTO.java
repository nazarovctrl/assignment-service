package uz.ccrew.assignmentservice.assignment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record AssignmentCompleteDTO(@NotNull(message = "Invalid assignmentId")
                                    Long assignmentId,
                                    @NotBlank(message = "Invalid fileId")
                                    String fileId,
                                    @NotBlank(message = "Invalid note")
                                    String note) {
}
