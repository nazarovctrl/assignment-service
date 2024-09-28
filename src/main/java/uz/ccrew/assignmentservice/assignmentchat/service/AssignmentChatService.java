package uz.ccrew.assignmentservice.assignmentchat.service;

import uz.ccrew.assignmentservice.chat.entity.Chat;
import uz.ccrew.assignmentservice.assignmentchat.dto.MessageDTO;
import uz.ccrew.assignmentservice.assignmentchat.dto.SendMessageDTO;

public interface AssignmentChatService {
    Chat create(String chatName);

    MessageDTO sendMessage(SendMessageDTO dto);
}
