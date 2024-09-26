package uz.ccrew.assignmentservice.dto.assignment;

import lombok.Builder;

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
                                  String receiverOrganizationName) {}
