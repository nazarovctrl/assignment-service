package uz.ccrew.assignmentservice.entity;

import uz.ccrew.assignmentservice.enums.PaymentType;
import uz.ccrew.assignmentservice.assignment.Assignment;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "requisite_assignments", uniqueConstraints = @UniqueConstraint(name = "requisite_assignments_u1", columnNames = "payment_id"))
@Check(name = "requisite_assignments_c1", constraints = "payment_type = 'CARD' and card_number is not null or payment_type = 'ACCOUNT' and account_number is not null")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequisiteAssignment extends Auditable {
    @Id
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Enumerated(EnumType.STRING)
    @Column
    private PaymentType paymentType;

    @Column
    private String cardNumber;

    @Column
    private String accountNumber;

    @Column(nullable = false)
    private Long paymentAmount;

    @Column
    private String paymentId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "assignment_id", foreignKey = @ForeignKey(name = "requisite_assignments_f1"))
    private Assignment assignment;
}
