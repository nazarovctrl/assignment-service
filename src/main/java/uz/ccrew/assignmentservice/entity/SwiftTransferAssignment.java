package uz.ccrew.assignmentservice.entity;

import uz.ccrew.assignmentservice.enums.SwiftReceiverType;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "swift_transfer_assignments")
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
    private String legalPersonAddress;

    @Column
    private String receiverOrganizationName;

    @OneToOne
    @MapsId
    @JoinColumn(name = "assignment_id", foreignKey = @ForeignKey(name = "swift_transfer_assignments_f1"))
    private TransferAssignment assignment;
}
