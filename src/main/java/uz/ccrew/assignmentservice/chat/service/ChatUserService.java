package uz.ccrew.assignmentservice.chat.service;

import java.util.UUID;

public interface ChatUserService {
    void addUserToChat(Long userId, UUID chatId);
}

