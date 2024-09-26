package uz.ccrew.assignmentservice.chat.service;

import java.util.UUID;

public interface MessageService {
    void sendMessage(UUID chatId, String content);
}
