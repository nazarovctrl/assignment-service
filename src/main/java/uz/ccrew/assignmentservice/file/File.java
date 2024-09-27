package uz.ccrew.assignmentservice.file;

import uz.ccrew.assignmentservice.base.Auditable;

import lombok.*;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "files")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File extends Auditable {
    @Id
    private UUID fileId;

    @Column(nullable = false)
    private String url;
}