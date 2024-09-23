package uz.ccrew.assignmentservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.ccrew.assignmentservice.entity.Assignment;

public interface AssignmentRepository extends BasicRepository<Assignment, Long> {
    Page<Assignment> findAllByCreatedBy_Id(Long userId, Pageable pageable);
}
