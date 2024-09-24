package uz.ccrew.assignmentservice.repository;

import uz.ccrew.assignmentservice.entity.Assignment;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AssignmentRepository extends BasicRepository<Assignment, Long> {
    Page<Assignment> findAllByCreatedBy_Id(Long userId, Pageable pageable);

    @Query("""
            SELECT new uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO(
                a.assigmentId,
                a.category,
                a.createdOn,
                a.status,
                r.paymentAmount,
                a.details,
                a.note
            )
            FROM Assignment a
            LEFT JOIN RequisiteAssignment r ON a.assigmentId = r.assignmentId
            WHERE a.assigmentId = :assigmentId
            AND a.createdBy.id = :userId
            """)
    Optional<AssignmentDetailedDTO> findAssignmentDetailedByIdAndUserId(@Param("userId") Long userId, @Param("assigmentId") Long assigmentId);
}
