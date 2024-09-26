package uz.ccrew.assignmentservice.chat.service.impl;

import uz.ccrew.assignmentservice.chat.entity.ChatUser;
import uz.ccrew.assignmentservice.chat.service.ChatUserService;
import uz.ccrew.assignmentservice.chat.repository.ChatUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatUserServiceImpl implements ChatUserService {
    private final ChatUserRepository chatUserRepository;

    @Override
    public void addUserToChat(Long userId, UUID chatId) {
        ChatUser chatUser = ChatUser.builder()
                .id(new ChatUser.ChatUserId(chatId, userId))
                .build();

        chatUserRepository.save(chatUser);
    }
}
