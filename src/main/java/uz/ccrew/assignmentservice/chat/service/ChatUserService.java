package uz.ccrew.assignmentservice.chat.service;

import uz.ccrew.assignmentservice.user.User;
import uz.ccrew.assignmentservice.chat.entity.Chat;

public interface ChatUserService {
    void addUserToChat(User user, Chat chat);
}

