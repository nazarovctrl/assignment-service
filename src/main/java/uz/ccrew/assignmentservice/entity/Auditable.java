package uz.ccrew.assignmentservice.entity;

import lombok.Getter;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class Auditable {
    @ManyToOne
    @JoinColumn(name = "created_by")
    @CreatedBy
    protected User createdBy;

    @CreatedDate
    protected LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    @LastModifiedBy
    protected User modifiedBy;

    @LastModifiedDate
    protected LocalDateTime modifiedDate;
}