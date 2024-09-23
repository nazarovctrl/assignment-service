package uz.ccrew.assignmentservice.file;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class FileRepositoryTest {
    @Autowired
    private FileRepository fileRepository;

    @Test
    void saveOk() {
        File file = File.builder()
                .fileId(UUID.randomUUID())
                .url("http:80/localhost/test/file/url")
                .build();
        assertDoesNotThrow(() -> fileRepository.save(file));
    }

    @Test
    void saveExp() {
        File file = File.builder()
                .fileId(UUID.randomUUID())
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> fileRepository.save(file));
    }
}