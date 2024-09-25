package uz.ccrew.assignmentservice.repository;

import uz.ccrew.assignmentservice.entity.Assignment;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AssignmentRepository extends BasicRepository<Assignment, Long> {
    Page<Assignment> findAllByCreatedBy_Id(Long userId, Pageable pageable);

    @Override
    List<Assignment> findAll();

    List<Assignment> findAllByStatusAndModifiedOnLessThan(AssignmentStatus status, LocalDateTime dateTime);
}
