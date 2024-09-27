package uz.ccrew.assignmentservice.entity;

import uz.ccrew.assignmentservice.assignment.Assignment;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import java.time.LocalDate;

@Entity
@Table(name = "certificate_assignments")
@Check(name = "certificate_assignments_c1", constraints = "begin_date <= end_date")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateAssignment extends Auditable {
    @Id
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(nullable = false)
    private LocalDate beginDate;

    @Column(nullable = false)
    private LocalDate endDate;


    @MapsId
    @OneToOne
    @JoinColumn(name = "assignment_id", foreignKey = @ForeignKey(name = "certificate_assignments_f1"))
    private Assignment assignment;
}
