package uz.ccrew.assignmentservice.dto.assignment;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AssignmentCreateDTO(String category,
                                  String fileId,
                                  String details,
                                  String receiverCountry,
                                  String fullName,
                                  String phoneNumber,
                                  Long amount,
                                  String accountNumber,
                                  String swiftCode,
                                  String legalPersonAddress,
                                  String receiverOrganizationName,
                                  String transferType,
                                  String cardNumber,
                                  String chatId,
                                  LocalDate beginDate,
                                  LocalDate endDate) {}
