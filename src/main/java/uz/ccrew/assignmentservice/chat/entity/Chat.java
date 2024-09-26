package uz.ccrew.assignmentservice.chat.entity;

import uz.ccrew.assignmentservice.entity.Auditable;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "chats")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat extends Auditable {
    @Id
    @UuidGenerator
    private UUID chatId;

    @Column
    private String chatName;
}
