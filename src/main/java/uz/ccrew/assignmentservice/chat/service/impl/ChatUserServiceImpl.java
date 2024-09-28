package uz.ccrew.assignmentservice.chat.service.impl;

import uz.ccrew.assignmentservice.user.User;
import uz.ccrew.assignmentservice.chat.entity.Chat;
import uz.ccrew.assignmentservice.chat.entity.ChatUser;
import uz.ccrew.assignmentservice.chat.service.ChatUserService;
import uz.ccrew.assignmentservice.chat.repository.ChatUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatUserServiceImpl implements ChatUserService {
    private final ChatUserRepository chatUserRepository;

    @Override
    public void addUserToChat(User user, Chat chat) {
        ChatUser chatUser = ChatUser.builder()
                .id(new ChatUser.ChatUserId(chat.getChatId(), user.getId()))
                .chat(chat)
                .user(user)
                .build();

        chatUserRepository.save(chatUser);
    }
}
