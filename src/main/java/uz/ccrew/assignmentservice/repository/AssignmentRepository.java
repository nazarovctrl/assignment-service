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
            select new uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO(
                  a.assigmentId,
                  a.category,
                  a.createdOn,
                  a.status,
                  r.paymentAmount,
                  a.details,
                  a.note
             )
             from Assignment a
             left join RequisiteAssignment r
               on a.assigmentId = r.assignmentId
            where a.assigmentId = :assigmentId
              and a.createdBy.id = :userId
            """)
    Optional<AssignmentDetailedDTO> findAssignmentDetailedByIdAndUserId(@Param("userId") Long userId, @Param("assigmentId") Long assigmentId);
}
