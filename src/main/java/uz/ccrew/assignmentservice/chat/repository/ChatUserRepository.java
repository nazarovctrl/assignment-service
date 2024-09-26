package uz.ccrew.assignmentservice.chat.repository;

import uz.ccrew.assignmentservice.chat.entity.ChatUser;
import uz.ccrew.assignmentservice.repository.BasicRepository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChatUserRepository extends BasicRepository<ChatUser, ChatUser.ChatUserId> {
    @Query("""
            select w.user.login
              from ChatUser w
             where w.chat.chatId = ?1
               and w.user.id <> ?2
            """)
    List<String> findMessageReceiverByChatId(UUID chatId, Long senderId);
}
