package uz.ccrew.assignmentservice.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.Objects;
import java.io.Serializable;

@Entity
@Table(name = "certificate_assignment_cards")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateAssignmentCard extends Auditable {
    @EmbeddedId
    private CertificateAssignmentCardId id;

    @ManyToOne
    @MapsId("assignmentId")
    @JoinColumn(name = "assignment_id", foreignKey = @ForeignKey(name = "certificate_assignment_cards_f1"))
    private CertificateAssignment certificateAssignment;

    @Embeddable
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CertificateAssignmentCardId implements Serializable {
        private Long assignmentId;
        private String cardNumber;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CertificateAssignmentCardId that = (CertificateAssignmentCardId) o;
            return Objects.equals(assignmentId, that.assignmentId) &&
                    Objects.equals(cardNumber, that.cardNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(assignmentId, cardNumber);
        }
    }
}
