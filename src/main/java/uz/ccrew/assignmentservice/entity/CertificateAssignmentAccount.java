package uz.ccrew.assignmentservice.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.Objects;
import java.io.Serializable;

@Entity
@Table(name = "certificate_assignment_accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateAssignmentAccount extends Auditable {
    @EmbeddedId
    private CertificateAssignmentAccountId id;

    @ManyToOne
    @MapsId("assignmentId")
    @JoinColumn(name = "assignment_id", foreignKey = @ForeignKey(name = "certificate_assignment_accounts_f1"))
    private CertificateAssignment certificateAssignment;

    @Embeddable
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CertificateAssignmentAccountId implements Serializable {
        private Long assignmentId;
        private String accountNumber;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CertificateAssignmentAccountId that = (CertificateAssignmentAccountId) o;
            return Objects.equals(assignmentId, that.assignmentId) &&
                    Objects.equals(accountNumber, that.accountNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(assignmentId, accountNumber);
        }
    }
}
