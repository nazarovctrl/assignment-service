package uz.ccrew.assignmentservice.repository;

import uz.ccrew.assignmentservice.entity.File;
import uz.ccrew.assignmentservice.entity.User;
import uz.ccrew.assignmentservice.enums.UserRole;
import uz.ccrew.assignmentservice.enums.Category;
import uz.ccrew.assignmentservice.entity.Assignment;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.entity.CardRefreshAssignment;

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
public class CardRefreshAssignmentRepositoryTest {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private CardRefreshAssignmentRepository cardRefreshAssignmentRepository;

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

        Assignment assignment = Assignment.builder()
                .fileId(file.getFileId())
                .category(Category.SWIFT_PHYSICAL)
                .details("Details")
                .status(AssignmentStatus.IN_REVIEW)
                .build();
        assignment.setCreatedBy(user);

        assignmentRepository.save(assignment);
        return assignment;
    }

    @Transactional
    @Test
    void saveOk() {
        Assignment assignment = createAssignment();

        File identityFile = File.builder()
                .fileId(UUID.randomUUID())
                .url("http:80/localhost/test/file/url")
                .build();
        fileRepository.save(identityFile);

        CardRefreshAssignment cardRefreshAssignment = CardRefreshAssignment.builder()
                .assignment(assignment)
                .identityFileId(identityFile.getFileId())
                .build();

        assertDoesNotThrow(() -> cardRefreshAssignmentRepository.save(cardRefreshAssignment));
    }

    @Transactional
    @Test
    void saveExp() {
        Assignment assignment = createAssignment();

        CardRefreshAssignment cardRefreshAssignment = CardRefreshAssignment.builder()
                .assignment(assignment)
                .identityFileId(null)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            cardRefreshAssignmentRepository.save(cardRefreshAssignment);
            cardRefreshAssignmentRepository.flush();
        });
    }
}