package uz.ccrew.assignmentservice.assignment.entity;

import uz.ccrew.assignmentservice.file.File;

import lombok.*;
import jakarta.persistence.*;

import java.util.UUID;
import java.util.Objects;
import java.io.Serializable;

@Entity
@Table(name = "dispute_assignment_photos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisputeAssignmentPhoto {
    @EmbeddedId
    private DisputeAssignmentPhotoId id;

    @ManyToOne
    @MapsId("assignmentId")
    @JoinColumn(name = "assignment_id", foreignKey = @ForeignKey(name = "dispute_assignment_photos_f1"))
    private Assignment assignment;

    @OneToOne
    @MapsId("photoId")
    @JoinColumn(name = "photo_id", foreignKey = @ForeignKey(name = "dispute_assignment_photos_f2"))
    private File photo;

    @Embeddable
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DisputeAssignmentPhotoId implements Serializable {
        private Long assignmentId;
        private UUID photoId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DisputeAssignmentPhotoId that = (DisputeAssignmentPhotoId) o;
            return Objects.equals(assignmentId, that.assignmentId) &&
                    Objects.equals(photoId, that.photoId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(assignmentId, photoId);
        }
    }
}
