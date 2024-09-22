package uz.ccrew.assignmentservice.service.impl;

import uz.ccrew.assignmentservice.entity.File;
import uz.ccrew.assignmentservice.entity.User;
import uz.ccrew.assignmentservice.enums.Category;
import uz.ccrew.assignmentservice.enums.UserRole;
import uz.ccrew.assignmentservice.entity.Assignment;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.repository.FileRepository;
import uz.ccrew.assignmentservice.repository.UserRepository;
import uz.ccrew.assignmentservice.service.AssignmentService;
import uz.ccrew.assignmentservice.repository.AssignmentRepository;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;

import org.junit.jupiter.api.*;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.UUID;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AssignmentServiceImplTest {

    private Assignment assignment;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AssignmentRepository assignmentRepository;

    @BeforeAll
    void setup() {
        File file = File.builder()
                .fileId(UUID.randomUUID())
                .url("http://localhost/test/file/url")
                .build();
        fileRepository.save(file);

        User customer = User.builder()
                .id(1L)
                .login("anton")
                .password("123")
                .role(UserRole.CUSTOMER)
                .credentialsModifiedDate(LocalDateTime.now())
                .build();
        userRepository.save(customer);

        User employee = User.builder()
                .id(2L)
                .login("maksud")
                .password("123")
                .role(UserRole.EMPLOYEE)
                .credentialsModifiedDate(LocalDateTime.now())
                .build();
        userRepository.save(employee);

        assignment = Assignment.builder()
                .assigmentId(1L)
                .fileId(file.getFileId())
                .employeeId(employee.getId())
                .category(Category.SWIFT_PHYSICAL)
                .details("Details 1")
                .status(AssignmentStatus.IN_REVIEW)
                .build();
        assignment.setCreatedBy(customer);
        assignment.setCreatedOn(LocalDateTime.now());
        assignmentRepository.save(assignment);
    }

    @AfterAll
    void cleanup() {
        assignmentRepository.deleteAll();
        userRepository.deleteAll();
        fileRepository.deleteAll();
    }

    @Test
    @WithUserDetails("anton")
    public void findAllAssignments() {
        Page<AssignmentSummaryDTO> result = assignmentService.findAllAssignments(0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals(assignment.getCategory().name(), result.getContent().getFirst().category().name());
    }
}
