package uz.ccrew.assignmentservice.payment;

import uz.ccrew.assignmentservice.entity.RequisiteAssignment;

public interface PaymentService {
    String withdraw(RequisiteAssignment requisite);

    void reverse(String paymentId);
}
