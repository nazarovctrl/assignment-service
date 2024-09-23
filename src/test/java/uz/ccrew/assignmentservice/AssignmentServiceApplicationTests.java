package uz.ccrew.assignmentservice;

import uz.ccrew.assignmentservice.enums.PaymentType;
import uz.ccrew.assignmentservice.payment.PaymentService;
import uz.ccrew.assignmentservice.entity.RequisiteAssignment;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@ActiveProfiles("test")
class AssignmentServiceApplicationTests {
    @Autowired
    private PaymentService paymentService;

    @Test
    void contextLoads() {
        paymentService.withdraw(RequisiteAssignment.builder()
                        .paymentType(PaymentType.CARD)
                        .cardNumber("8600492939187759")
                        .paymentAmount(1000L)
                .build());
    }
}
