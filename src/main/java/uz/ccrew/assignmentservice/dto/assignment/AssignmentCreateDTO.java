package uz.ccrew.assignmentservice.dto.assignment;

import uz.ccrew.assignmentservice.enums.TransferType;

import lombok.Builder;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.time.LocalDate;

@Builder
public record AssignmentCreateDTO(@NotBlank(message = "Invalid assignment category")
                                  String category,
                                  String fileId,
                                  @NotBlank(message = "Invalid details")
                                  String details,
                                  String accountNumberToPay,
                                  String cardNumberToPay,
                                  //swift
                                  String receiverCountry,
                                  String accountNumber,
                                  String swiftCode,
                                  Long amount,
                                  String receiverFullName,
                                  //swift legal
                                  String legalAddress,
                                  String receiverOrganizationName,
                                  //international transfer
                                  TransferType transferType,
                                  String receiverPhoneNumber,
                                  //certificate
                                  List<String> accountNumbers,
                                  List<String> cardNumbers,
                                  LocalDate beginDate,
                                  LocalDate endDate,
                                  //card refresh
                                  String identityFileId,
                                  //dispute
                                  List<String> photoIds) {
}
