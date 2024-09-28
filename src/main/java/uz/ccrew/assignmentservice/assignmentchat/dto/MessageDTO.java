package uz.ccrew.assignmentservice.assignmentchat.dto;

import java.util.UUID;
import java.time.LocalDateTime;

public record MessageDTO(UUID messageId,
                         UUID chatId,
                         Long senderId,
                         String content,
                         LocalDateTime sentTime) {
}
