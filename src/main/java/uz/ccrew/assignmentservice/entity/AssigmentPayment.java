package uz.ccrew.assignmentservice.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import java.util.UUID;

@Entity
@Table(name = "assignment_payments", uniqueConstraints = {@UniqueConstraint(name = "assignment_payments_u1", columnNames = "payment_id")})
@Check(name = "assignment_payments_c1", constraints = "created_on is not null")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssigmentPayment extends Auditable {
    @Id
    private Long assignmentId;

    @Column(nullable = false)
    private UUID paymentId;


    @MapsId
    @ManyToOne
    @JoinColumn(name = "assignment_id", foreignKey = @ForeignKey(name = "assignment_payments_f1"))
    private RequisiteAssignment requisiteAssignment;
}
