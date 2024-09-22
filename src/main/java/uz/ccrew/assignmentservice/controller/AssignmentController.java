package uz.ccrew.assignmentservice.controller;


import uz.ccrew.assignmentservice.dto.Response;
import uz.ccrew.assignmentservice.dto.ResponseMaker;
import uz.ccrew.assignmentservice.service.AssignmentService;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/assignment")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Tag(name = "Assignment Controller", description = "Assignment API")
public class AssignmentController {
    private final AssignmentService assignmentService;

    @GetMapping("/findSummary")
    public ResponseEntity<Response<Page<AssignmentSummaryDTO>>> getAllAssignments(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                                  @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        Page<AssignmentSummaryDTO> result = assignmentService.findAllAssignments(page, size);
        return ResponseMaker.ok(result);
    }
}
