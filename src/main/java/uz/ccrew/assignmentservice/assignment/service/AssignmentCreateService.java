package uz.ccrew.assignmentservice.assignment.service;

import uz.ccrew.assignmentservice.assignment.entity.RequisiteAssignment;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentCreateDTO;
import uz.ccrew.assignmentservice.payment.PaymentType;

public interface AssignmentCreateService {

    RequisiteAssignment create(AssignmentCreateDTO dto);

    PaymentType getPaymentType(String cardNumber, String accountNumber);
}
