package uz.ccrew.assignmentservice.assignment;

import uz.ccrew.assignmentservice.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.ccrew.assignmentservice.repository.BasicRepository;

import java.util.Optional;

import java.util.List;
import java.time.LocalDateTime;

public interface AssignmentRepository extends BasicRepository<Assignment, Long> {
    Page<Assignment> findAllByCreatedBy_Id(Long userId, Pageable pageable);

    @Query("""
           select new uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO(
                  a.assignmentId,
                  a.category,
                  a.createdOn,
                  a.status,
                  r.paymentAmount,
                  a.details,
                  a.note)
             from Assignment a
             left join RequisiteAssignment r
               on a.assignmentId = r.assignmentId
            where a.assignmentId = :assigmentId
              and a.createdBy.id = :userId
            """)
    Optional<AssignmentDetailedDTO> findAssignmentDetailedByIdAndUserId(@Param("userId") Long userId, @Param("assigmentId") Long assigmentId);

    List<Assignment> findAllByStatusAndModifiedOnLessThan(AssignmentStatus status, LocalDateTime dateTime);
}
