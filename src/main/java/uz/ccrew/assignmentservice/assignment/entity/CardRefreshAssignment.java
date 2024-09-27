package uz.ccrew.assignmentservice.assignment.entity;

import uz.ccrew.assignmentservice.file.File;
import uz.ccrew.assignmentservice.base.Auditable;

import lombok.*;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "card_refresh_assignments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRefreshAssignment extends Auditable {
    @Id
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(name = "identity_file_id", nullable = false)
    private UUID identityFileId;


    @MapsId
    @OneToOne
    @JoinColumn(name = "assignment_id", foreignKey = @ForeignKey(name = "card_refresh_assignments_f1"))
    private Assignment assignment;

    @OneToOne
    @JoinColumn(name = "identity_file_id", foreignKey = @ForeignKey(name = "card_refresh_assignments_f2"), insertable = false, updatable = false)
    private File identityFile;
}
