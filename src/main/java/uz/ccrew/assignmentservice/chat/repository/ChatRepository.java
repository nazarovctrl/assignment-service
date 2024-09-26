package uz.ccrew.assignmentservice.chat.repository;

import uz.ccrew.assignmentservice.chat.entity.Chat;
import uz.ccrew.assignmentservice.repository.BasicRepository;

import java.util.UUID;

public interface ChatRepository extends BasicRepository<Chat, UUID> {
}
