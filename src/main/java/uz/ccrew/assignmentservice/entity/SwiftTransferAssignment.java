package uz.ccrew.assignmentservice.entity;

import uz.ccrew.assignmentservice.enums.SwiftReceiverType;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "swift_transfer_assignments")
@Check(name = "swift_transfer_assignments_c1", constraints = """ 
        receiver_type = 'LEGAL' and legal_address is not null and receiver_organization_name is not null
        or receiver_type = 'PHYSICAL' and legal_address is null and receiver_organization_name is null
        """)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwiftTransferAssignment extends Auditable {
    @Id
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Enumerated(EnumType.STRING)
    @Column
    private SwiftReceiverType receiverType;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String swiftCode;

    @Column
    private String legalAddress;

    @Column
    private String receiverOrganizationName;

    @OneToOne
    @MapsId
    @JoinColumn(name = "assignment_id", foreignKey = @ForeignKey(name = "swift_transfer_assignments_f1"))
    private TransferAssignment transferAssignment;
}
