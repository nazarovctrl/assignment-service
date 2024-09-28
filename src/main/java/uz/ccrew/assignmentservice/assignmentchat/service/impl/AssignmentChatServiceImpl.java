package uz.ccrew.assignmentservice.assignmentchat.service.impl;

import uz.ccrew.assignmentservice.base.AuthUtil;
import uz.ccrew.assignmentservice.chat.entity.Chat;
import uz.ccrew.assignmentservice.chat.service.MessageService;
import uz.ccrew.assignmentservice.chat.service.ChatUserService;
import uz.ccrew.assignmentservice.assignment.entity.Assignment;
import uz.ccrew.assignmentservice.assignmentchat.dto.MessageDTO;
import uz.ccrew.assignmentservice.chat.repository.ChatRepository;
import uz.ccrew.assignmentservice.assignmentchat.dto.SendMessageDTO;
import uz.ccrew.assignmentservice.assignment.repository.AssignmentRepository;
import uz.ccrew.assignmentservice.assignmentchat.service.AssignmentChatService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentChatServiceImpl implements AssignmentChatService {
    private final AuthUtil authUtil;
    private final MessageService messageService;
    private final ChatRepository chatRepository;
    private final ChatUserService chatUserService;
    private final AssignmentRepository assignmentRepository;

    @Override
    public Chat create(String chatName) {
        Chat chat = Chat.builder()
                .chatName(chatName)
                .build();
        chatRepository.save(chat);
        chatUserService.addUserToChat(authUtil.loadLoggedUser(), chat);
        return chat;
    }

    @Override
    public MessageDTO sendMessage(SendMessageDTO dto) {
        Assignment assignment = assignmentRepository.loadById(dto.assignmentId(), "Assignment not found");
        Chat chat = assignment.getChat();

        messageService.sendMessage(chat.getChatId(), dto.content());
        return null;
    }
}
