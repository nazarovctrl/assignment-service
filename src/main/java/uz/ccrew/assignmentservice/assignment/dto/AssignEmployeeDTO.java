package uz.ccrew.assignmentservice.assignment.dto;

import jakarta.validation.constraints.NotNull;

public record AssignEmployeeDTO(@NotNull(message = "Invalid assignmentId")
                                Long assignmentId,
                                Long employeeId) {
}
