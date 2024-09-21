package uz.ccrew.assignmentservice.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "assigments")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Assigment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assigmentId;
}
