package uz.ccrew.assignmentservice.assignmentchat.dto;

import lombok.Builder;

import java.util.UUID;
import java.time.LocalDateTime;

@Builder
public record MessageDTO(UUID messageId,
                         UUID chatId,
                         Long senderId,
                         String content,
                         LocalDateTime sentTime) {
}
