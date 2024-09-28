package uz.ccrew.assignmentservice.assignmentchat.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record SendMessageDTO(@NotNull(message = "Invalid assignmentId")
                             Long assignmentId,
                             @NotBlank(message = "Invalid content")
                             String content) {
}
