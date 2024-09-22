package uz.ccrew.assignmentservice.repository;

import uz.ccrew.assignmentservice.entity.*;
import uz.ccrew.assignmentservice.enums.Category;
import uz.ccrew.assignmentservice.enums.UserRole;
import uz.ccrew.assignmentservice.enums.PaymentType;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;

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
public class AssigmentPaymentRepositoryTest {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssigmentPaymentRepository assigmentPaymentRepository;
    @Autowired
    private RequisiteAssignmentRepository requisiteAssignmentRepository;

    RequisiteAssignment createRequisiteAssignment() {
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

        RequisiteAssignment requisiteAssignment = RequisiteAssignment.builder()
                .assignment(assignment)
                .paymentType(PaymentType.ACCOUNT)
                .accountNumber("123123")
                .paymentAmount(10000L)
                .build();
        requisiteAssignmentRepository.save(requisiteAssignment);
        return requisiteAssignment;
    }

    @Transactional
    @Test
    void saveOk() {
        RequisiteAssignment requisiteAssignment = createRequisiteAssignment();

        AssigmentPayment assigmentPayment = AssigmentPayment.builder()
                .requisiteAssignment(requisiteAssignment)
                .paymentId(UUID.randomUUID())
                .build();

        assertDoesNotThrow(() -> assigmentPaymentRepository.save(assigmentPayment));
    }

    @Transactional
    @Test
    void saveExp() {
        RequisiteAssignment requisiteAssignment = createRequisiteAssignment();

        AssigmentPayment assigmentPayment = AssigmentPayment.builder()
                .requisiteAssignment(requisiteAssignment)
                .paymentId(null)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            assigmentPaymentRepository.save(assigmentPayment);
            assigmentPaymentRepository.flush();
        });
    }
}