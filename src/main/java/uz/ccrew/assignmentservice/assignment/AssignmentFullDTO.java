package uz.ccrew.assignmentservice.assignment;

import uz.ccrew.assignmentservice.assignment.enums.Category;
import uz.ccrew.assignmentservice.assignment.enums.AssignmentStatus;

import lombok.Builder;

import java.util.List;
import java.time.LocalDateTime;

@Builder
public record AssignmentFullDTO(Category category,
                                Long userId,
                                String fullName,
                                String phoneNumber,
                                String email,
                                Long assignmentId,
                                LocalDateTime createdOn,
                                LocalDateTime progressStartedOn,
                                List<String> fileUrls,
                                AssignmentStatus status
) {
}
