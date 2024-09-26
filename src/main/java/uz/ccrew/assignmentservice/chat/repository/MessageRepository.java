package uz.ccrew.assignmentservice.chat.repository;

import uz.ccrew.assignmentservice.chat.entity.Message;
import uz.ccrew.assignmentservice.repository.BasicRepository;

import java.util.UUID;

public interface MessageRepository extends BasicRepository<Message, UUID> {
}
