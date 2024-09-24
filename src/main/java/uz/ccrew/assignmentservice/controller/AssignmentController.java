package uz.ccrew.assignmentservice.controller;

import uz.ccrew.assignmentservice.dto.Response;
import uz.ccrew.assignmentservice.dto.ResponseMaker;
import uz.ccrew.assignmentservice.service.AssignmentService;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentDetailedDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/assignment")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Tag(name = "Assignment Controller", description = "Assignment API")
public class AssignmentController {
    private final AssignmentService assignmentService;

    @GetMapping("/my/list")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Get all summary assignments")
    public ResponseEntity<Response<Page<AssignmentSummaryDTO>>> getSummaryList(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                                  @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        Page<AssignmentSummaryDTO> result = assignmentService.getSummaryList(page, size);
        return ResponseMaker.ok(result);
    }

    @GetMapping("/detailed/{assignmentId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Get detailed assignment")
    public ResponseEntity<Response<AssignmentDetailedDTO>> getDetailed(@PathVariable("assignmentId") Long id) {
        AssignmentDetailedDTO result = assignmentService.getDetailed(id);
        return ResponseMaker.ok(result);
    }
}
