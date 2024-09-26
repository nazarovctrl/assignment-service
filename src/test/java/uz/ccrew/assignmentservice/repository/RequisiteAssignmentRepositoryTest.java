package uz.ccrew.assignmentservice.repository;

import uz.ccrew.assignmentservice.entity.*;
import uz.ccrew.assignmentservice.file.File;
import uz.ccrew.assignmentservice.enums.UserRole;
import uz.ccrew.assignmentservice.enums.Category;
import uz.ccrew.assignmentservice.chat.entity.Chat;
import uz.ccrew.assignmentservice.enums.PaymentType;
import uz.ccrew.assignmentservice.file.FileRepository;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.chat.repository.ChatRepository;


import org.junit.jupiter.api.Test;
import jakarta.transaction.Transactional;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class RequisiteAssignmentRepositoryTest {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private RequisiteAssignmentRepository requisiteAssignmentRepository;

    Assignment createAssignment() {
        File file = File.builder()
                .fileId(UUID.randomUUID())
                .url("http:80/localhost/test/file/url")
                .build();
        fileRepository.save(file);

        User user = User.builder()
                .login("azimjon")
                .role(UserRole.CUSTOMER)
                .password("123")
                .credentialsModifiedDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Chat chat = Chat.builder()
                .chatName("test").build();
        chatRepository.save(chat);

        Assignment assignment = Assignment.builder()
                .fileId(file.getFileId())
                .category(Category.SWIFT_PHYSICAL)
                .details("Details")
                .status(AssignmentStatus.IN_REVIEW)
                .chatId(chat.getChatId())
                .build();
        assignment.setCreatedBy(user);

        assignmentRepository.save(assignment);
        return assignment;
    }

    @Transactional
    @Test
    void saveOk() {
        Assignment assignment = createAssignment();
        RequisiteAssignment requisiteAssignment = RequisiteAssignment.builder()
                .assignment(assignment)
                .paymentType(PaymentType.ACCOUNT)
                .accountNumber("123123")
                .paymentAmount(10000L)
                .build();

        assertDoesNotThrow(() -> requisiteAssignmentRepository.save(requisiteAssignment));
    }

    @Transactional
    @Test
    void saveExp() {
        Assignment assignment = createAssignment();
        RequisiteAssignment requisiteAssignment = RequisiteAssignment.builder()
                .assignment(assignment)
                .paymentType(PaymentType.ACCOUNT)
                .cardNumber("123123")
                .paymentAmount(10000L)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            requisiteAssignmentRepository.save(requisiteAssignment);
            requisiteAssignmentRepository.flush();
        });
    }
}