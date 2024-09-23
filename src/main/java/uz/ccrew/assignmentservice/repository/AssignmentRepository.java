package uz.ccrew.assignmentservice.repository;

import uz.ccrew.assignmentservice.entity.Assignment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssignmentRepository extends BasicRepository<Assignment, Long> {
    Page<Assignment> findAllByCreatedBy_Id(Long userId, Pageable pageable);
}
