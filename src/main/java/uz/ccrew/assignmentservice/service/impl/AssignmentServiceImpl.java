package uz.ccrew.assignmentservice.service.impl;

import uz.ccrew.assignmentservice.entity.User;
import uz.ccrew.assignmentservice.util.AuthUtil;
import uz.ccrew.assignmentservice.entity.Assignment;
import uz.ccrew.assignmentservice.exp.NotFoundException;
import uz.ccrew.assignmentservice.mapper.AssignmentMapper;
import uz.ccrew.assignmentservice.service.AssignmentService;
import uz.ccrew.assignmentservice.repository.AssignmentRepository;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AuthUtil authUtil;
    private final AssignmentMapper assignmentMapper;
    private final AssignmentRepository assignmentRepository;

    @Override
    public Page<AssignmentSummaryDTO> getSummary(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("assignmentId").descending());
        User user = authUtil.loadLoggedUser();

        Page<Assignment> assignments = assignmentRepository.findAllByCreatedBy_Id(user.getId(), pageable);
        List<AssignmentSummaryDTO> assignmentSummaries = assignmentMapper.toDTOList(assignments.getContent());

        return new PageImpl<>(assignmentSummaries, pageable, assignments.getTotalElements());
    }

    @Override
    public AssignmentDetailedDTO getDetailed(Long id) {
        User user = authUtil.loadLoggedUser();
        Optional<AssignmentDetailedDTO> detailedDTO = assignmentRepository.findAssignmentDetailedByIdAndUserId(user.getId(), id);
        if (detailedDTO.isEmpty()) {
            throw new NotFoundException("Detailed Assignment Not Found");
        }
        return detailedDTO.get();
    }
}
