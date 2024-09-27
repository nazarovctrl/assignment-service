package uz.ccrew.assignmentservice.assignment;

import uz.ccrew.assignmentservice.base.Mapper;
import uz.ccrew.assignmentservice.assignment.entity.Assignment;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentSummaryDTO;

import org.springframework.stereotype.Component;

@Component
public class AssignmentMapper implements Mapper<AssignmentSummaryDTO, AssignmentSummaryDTO, Assignment> {
    @Override
    public Assignment toEntity(AssignmentSummaryDTO assignmentSummaryDTO) {
        return null;
    }

    @Override
    public AssignmentSummaryDTO toDTO(Assignment assignment) {
        return AssignmentSummaryDTO.builder()
                .assigmentId(assignment.getAssignmentId())
                .category(assignment.getCategory())
                .date(assignment.getCreatedOn())
                .status(assignment.getStatus())
                .build();
    }
}
