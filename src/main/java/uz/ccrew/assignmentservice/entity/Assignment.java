package uz.ccrew.assignmentservice.entity;

import uz.ccrew.assignmentservice.enums.Category;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import java.util.UUID;

@Entity
@Table(name = "assignments", indexes = @Index(name = "assigment_i1", columnList = "created_by"))
@Check(name = "assignments_c1", constraints = "created_by is not null and created_on is not null")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assignment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assigmentId;

    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "response_file_id")
    private UUID responseFileId;

    @Enumerated(EnumType.STRING)
    @Column
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column
    private AssignmentStatus status;

    @Column(nullable = false)
    private String details;

    @Column
    private String note;


    @OneToOne
    @JoinColumn(name = "file_id", foreignKey = @ForeignKey(name = "assignments_f1"), insertable = false, updatable = false)
    private File file;

    @ManyToOne
    @JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "assignments_f2"), insertable = false, updatable = false)
    private User employee;

    @OneToOne
    @JoinColumn(name = "response_file_id", foreignKey = @ForeignKey(name = "assignments_f3"), insertable = false, updatable = false)
    private File responseFile;
}