package uz.ccrew.assignmentservice.chat.entity;

import uz.ccrew.assignmentservice.entity.Auditable;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message extends Auditable {
    @Id
    @UuidGenerator
    private UUID messageId;

    @Column(name = "chat_id", nullable = false)
    private UUID chatId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private String content;


    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", insertable = false, updatable = false),
            @JoinColumn(name = "sender_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    }, foreignKey = @ForeignKey(name = "chat_messages_f1"))
    private ChatUser chatUser;
}
