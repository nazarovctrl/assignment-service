package uz.ccrew.assignmentservice.assignment.dto;

import jakarta.validation.constraints.NotNull;

public record WithdrawDTO(@NotNull(message = "Invalid assignmentId")
                          Long assignmentId,
                          String accountNumber,
                          String cardNumber) {
}
