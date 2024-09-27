package uz.ccrew.assignmentservice.chat.service.impl;

import uz.ccrew.assignmentservice.user.User;
import uz.ccrew.assignmentservice.base.AuthUtil;
import uz.ccrew.assignmentservice.chat.entity.Message;
import uz.ccrew.assignmentservice.chat.entity.ChatUser;
import uz.ccrew.assignmentservice.chat.service.MessageService;
import uz.ccrew.assignmentservice.notifcation.NotificationService;
import uz.ccrew.assignmentservice.chat.repository.MessageRepository;
import uz.ccrew.assignmentservice.chat.repository.ChatUserRepository;

import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final AuthUtil authUtil;
    private final MessageRepository messageRepository;
    private final ChatUserRepository chatUserRepository;
    private final NotificationService notificationService;

    @Transactional
    @Override
    public void sendMessage(UUID chatId, String content) {
        User user = authUtil.loadLoggedUser();
        chatUserRepository.loadById(new ChatUser.ChatUserId(chatId, user.getId()), "Chat not found");

        Message message = Message.builder()
                .chatId(chatId)
                .senderId(user.getId())
                .content(content)
                .build();

        List<String> users = chatUserRepository.findMessageReceiverByChatId(chatId, user.getId());
        notificationService.sendNotification(users, content);

        messageRepository.save(message);
    }
}
