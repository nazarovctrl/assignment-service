package uz.ccrew.assignmentservice.service.impl;

import uz.ccrew.assignmentservice.entity.User;
import uz.ccrew.assignmentservice.util.AuthUtil;
import uz.ccrew.assignmentservice.entity.Assignment;
import uz.ccrew.assignmentservice.mapper.AssignmentMapper;
import uz.ccrew.assignmentservice.service.AssignmentService;
import uz.ccrew.assignmentservice.repository.AssignmentRepository;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AuthUtil authUtil;
    private final AssignmentMapper assignmentMapper;
    private final AssignmentRepository assignmentRepository;

    @Override
    public Page<AssignmentSummaryDTO> findAllAssignments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = authUtil.loadLoggedUser();

        Page<Assignment> assignments = assignmentRepository.findAllByCreatedBy_Id(user.getId(), pageable);
        List<AssignmentSummaryDTO> assignmentSummaries = assignmentMapper.toDTOList(assignments.getContent());

        return new PageImpl<>(assignmentSummaries, pageable, assignments.getTotalElements());
    }

    public Map<String, String> getCategories() {
        Map<String, String> categories = new HashMap<>();
        categories.put("swiftPhysical", "SWIFT transfers for physical");
        categories.put("swiftForLegalEntities", "SWIFT transfers for legal entities");
        categories.put("internationalTransfers", "International transfers");
        categories.put("certificates", "Certificate transfers");
        categories.put("cardRefresh", "Card reissue");
        categories.put("dispute", "Open a dispute");
        categories.put("others", "Others");

        return categories;
    }
}
