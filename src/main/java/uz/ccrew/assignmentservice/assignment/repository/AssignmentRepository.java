package uz.ccrew.assignmentservice.assignment.repository;

import uz.ccrew.assignmentservice.base.BasicRepository;
import uz.ccrew.assignmentservice.assignment.entity.Assignment;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentShortDTO;
import uz.ccrew.assignmentservice.assignment.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentDetailedDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.util.List;
import java.time.LocalDateTime;

public interface AssignmentRepository extends BasicRepository<Assignment, Long> {
    Page<Assignment> findAllByCreatedBy_Id(Long userId, Pageable pageable);

    @Query("""
            select new uz.ccrew.assignmentservice.assignment.dto.AssignmentDetailedDTO(
                   a.assignmentId,
                   a.category,
                   a.createdOn,
                   a.status,
                   r.paymentAmount,
                   a.details,
                   a.note,
                   f.url)
              from Assignment a
              left join RequisiteAssignment r
                on a.assignmentId = r.assignmentId
              left join File f
                on f.fileId = a.responseFileId 
             where a.assignmentId = :assigmentId
               and a.createdBy.id = :userId
             """)
    Optional<AssignmentDetailedDTO> findAssignmentDetailedByIdAndUserId(@Param("userId") Long userId, @Param("assigmentId") Long assigmentId);

    List<Assignment> findAllByStatusAndModifiedOnLessThan(AssignmentStatus status, LocalDateTime dateTime);


    @Query("""
            select new uz.ccrew.assignmentservice.assignment.dto.AssignmentShortDTO(
                   w.category,
                   w.createdBy.id,
                   w.createdBy.fullName,
                   w.createdBy.login,
                   w.assignmentId,
                   w.createdOn,
                   w.status,
                   em)
              from Assignment w
              left join w.employee em
             where w.status <> 'IN_REVIEW'
            """)
    Page<AssignmentShortDTO> getAssigmentShortPage(Pageable pageable);
}
