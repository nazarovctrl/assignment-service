package uz.ccrew.assignmentservice.chat.service;

import uz.ccrew.assignmentservice.assignmentchat.dto.MessageDTO;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDTO sendMessage(UUID chatId, String content);

    List<MessageDTO> getList(UUID chatId);
}
