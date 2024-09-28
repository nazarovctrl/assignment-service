package uz.ccrew.assignmentservice.assignment.dto;

import uz.ccrew.assignmentservice.user.User;
import uz.ccrew.assignmentservice.user.dto.UserDTO;
import uz.ccrew.assignmentservice.assignment.enums.Category;
import uz.ccrew.assignmentservice.assignment.enums.AssignmentStatus;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class AssignmentShortDTO {
    private Category category;
    private Long userId;
    private String fullName;
    private String phoneNumber;
    private Long assignmentId;
    private LocalDateTime createdOn;
    private AssignmentStatus status;
    private UserDTO employee;

    public AssignmentShortDTO(Category category, Long userId, String fullName, String phoneNumber, Long assignmentId, LocalDateTime createdOn, AssignmentStatus status, User employee) {
        this.category = category;
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.assignmentId = assignmentId;
        this.createdOn = createdOn;
        this.status = status;
        if (employee != null) {
            this.employee = UserDTO.builder()
                    .id(employee.getId())
                    .login(employee.getLogin())
                    .role(employee.getRole())
                    .fullName(employee.getFullName())
                    .build();
        }
    }
}
