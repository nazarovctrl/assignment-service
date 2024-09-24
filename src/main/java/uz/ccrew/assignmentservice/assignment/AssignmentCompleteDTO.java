package uz.ccrew.assignmentservice.assignment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssignmentCompleteDTO(@NotNull(message = "Invalid assignmentId")
                                    Long assignmentId,
                                    @NotBlank(message = "Invalid fileId")
                                    String fileId,
                                    @NotBlank(message = "Invalid note")
                                    String note) {
}
