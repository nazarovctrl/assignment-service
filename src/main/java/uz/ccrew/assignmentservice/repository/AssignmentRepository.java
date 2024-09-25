package uz.ccrew.assignmentservice.repository;

import uz.ccrew.assignmentservice.entity.Assignment;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.time.LocalDateTime;

public interface AssignmentRepository extends BasicRepository<Assignment, Long> {
    Page<Assignment> findAllByCreatedBy_Id(Long userId, Pageable pageable);

    List<Assignment> findAllByStatusAndModifiedOnLessThan(AssignmentStatus status, LocalDateTime dateTime);
}
