package uz.ccrew.assignmentservice.chat.entity;

import uz.ccrew.assignmentservice.user.User;
import uz.ccrew.assignmentservice.base.Auditable;

import lombok.*;
import jakarta.persistence.*;

import java.util.UUID;
import java.util.Objects;
import java.io.Serializable;

@Entity
@Table(name = "chat_users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser extends Auditable {
    @EmbeddedId
    private ChatUserId id;


    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id", foreignKey = @ForeignKey(name = "chat_users_f1"))
    private Chat chat;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "chat_users_f2"))
    private User user;

    @Embeddable
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatUserId implements Serializable {
        private UUID chatId;
        private Long userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChatUserId that = (ChatUserId) o;
            return Objects.equals(chatId, that.chatId) &&
                    Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(chatId, userId);
        }
    }
}
