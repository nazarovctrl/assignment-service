package uz.ccrew.assignmentservice.repository;

import uz.ccrew.assignmentservice.enums.*;
import uz.ccrew.assignmentservice.entity.*;

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
public class SwiftTransferAssignmentRepositoryTest {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private TransferAssignmentRepository transferAssignmentRepository;
    @Autowired
    private SwiftTransferAssignmentRepository swiftTransferAssignmentRepository;

    TransferAssignment createTransferAssignment() {
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


        TransferAssignment transferAssignment = TransferAssignment.builder()
                .amount(1000L)
                .type(TransferType.SWIFT)
                .receiverCountry("Uzb")
                .receiverFullName("Azimjon")
                .assignment(assignment)
                .build();
        transferAssignmentRepository.save(transferAssignment);

        return transferAssignment;
    }

    @Transactional
    @Test
    void saveOk() {
        TransferAssignment transferAssignment = createTransferAssignment();

        SwiftTransferAssignment swiftTransferAssignment = SwiftTransferAssignment.builder()
                .assignment(transferAssignment)
                .swiftCode("CODE")
                .accountNumber("123123")
                .receiverType(SwiftReceiverType.PHYSICAL)
                .build();

        assertDoesNotThrow(() -> swiftTransferAssignmentRepository.save(swiftTransferAssignment));
    }

    @Transactional
    @Test
    void saveEXp() {
        TransferAssignment transferAssignment = createTransferAssignment();

        SwiftTransferAssignment swiftTransferAssignment = SwiftTransferAssignment.builder()
                .assignment(transferAssignment)
                .swiftCode("CODE")
                .accountNumber("123123")
                .receiverType(SwiftReceiverType.LEGAL)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            swiftTransferAssignmentRepository.save(swiftTransferAssignment);
            swiftTransferAssignmentRepository.flush();
        });
    }
}