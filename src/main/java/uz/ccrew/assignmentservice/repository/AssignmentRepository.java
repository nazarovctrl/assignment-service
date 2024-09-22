package uz.ccrew.assignmentservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.ccrew.assignmentservice.entity.Assignment;

public interface AssignmentRepository extends BasicRepository<Assignment, Long> {
    Page<Assignment> findAllByEmployee_Id(Pageable pageable, Long employeeId);
}
