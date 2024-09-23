package uz.ccrew.assignmentservice.repository;

import uz.ccrew.assignmentservice.entity.*;
import uz.ccrew.assignmentservice.file.File;
import uz.ccrew.assignmentservice.enums.Category;
import uz.ccrew.assignmentservice.enums.UserRole;
import uz.ccrew.assignmentservice.file.FileRepository;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;

import org.junit.jupiter.api.Test;
import jakarta.transaction.Transactional;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
public class CertificateAssignmentAccountRepositoryTest {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private CertificateAssignmentRepository certificateAssignmentRepository;
    @Autowired
    private CertificateAssignmentAccountRepository certificateAssignmentAccountRepository;

    CertificateAssignment createCertificateAssignment() {
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

        CertificateAssignment certificateAssignment = CertificateAssignment.builder()
                .assignment(assignment)
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .build();
        certificateAssignmentRepository.save(certificateAssignment);
        return certificateAssignment;
    }

    @Transactional
    @Test
    void saveOk() {
        CertificateAssignment certificateAssignment = createCertificateAssignment();

        CertificateAssignmentAccount certificateAssignmentCard = CertificateAssignmentAccount.builder()
                .id(new CertificateAssignmentAccount.CertificateAssignmentAccountId(certificateAssignment.getAssignmentId(), "12312312"))
                .certificateAssignment(certificateAssignment)
                .build();

        assertDoesNotThrow(() -> certificateAssignmentAccountRepository.save(certificateAssignmentCard));
    }

    @Transactional
    @Test
    void saveExp() {
        CertificateAssignment certificateAssignment = createCertificateAssignment();

        CertificateAssignmentAccount certificateAssignmentCard = CertificateAssignmentAccount.builder()
                .id(new CertificateAssignmentAccount.CertificateAssignmentAccountId(certificateAssignment.getAssignmentId(), null))
                .certificateAssignment(certificateAssignment)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            certificateAssignmentAccountRepository.save(certificateAssignmentCard);
            certificateAssignmentAccountRepository.flush();
        });
    }
}