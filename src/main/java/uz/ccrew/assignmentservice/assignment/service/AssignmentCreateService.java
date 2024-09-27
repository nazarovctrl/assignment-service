package uz.ccrew.assignmentservice.assignment.service;

import uz.ccrew.assignmentservice.payment.PaymentType;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentCreateDTO;
import uz.ccrew.assignmentservice.assignment.entity.RequisiteAssignment;

public interface AssignmentCreateService {
    RequisiteAssignment create(AssignmentCreateDTO dto);

    PaymentType getPaymentType(String cardNumber, String accountNumber);
}
