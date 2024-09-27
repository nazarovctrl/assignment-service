package uz.ccrew.assignmentservice.assignment.service;

import uz.ccrew.assignmentservice.entity.RequisiteAssignment;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentCreateDTO;
import uz.ccrew.assignmentservice.enums.PaymentType;

public interface AssignmentCreateService {

    RequisiteAssignment create(AssignmentCreateDTO dto);

    PaymentType getPaymentType(String cardNumber, String accountNumber);
}
