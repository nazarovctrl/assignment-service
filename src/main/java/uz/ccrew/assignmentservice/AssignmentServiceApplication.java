package uz.ccrew.assignmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@SpringBootApplication
public class AssignmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentServiceApplication.class, args);
    }
}
