package uz.ccrew.assignmentservice.controller;

import uz.ccrew.assignmentservice.dto.Response;
import uz.ccrew.assignmentservice.dto.ResponseMaker;
import uz.ccrew.assignmentservice.service.AssignmentService;
import uz.ccrew.assignmentservice.assignment.AssignmentCancelDTO;
import uz.ccrew.assignmentservice.assignment.AssignmentCompleteDTO;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentSummaryDTO;
import uz.ccrew.assignmentservice.assignment.AssignmentStatusChangeDTO;

import jakarta.validation.Valid;
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
    public ResponseEntity<Response<Page<AssignmentSummaryDTO>>> getAllAssignments(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                                  @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        Page<AssignmentSummaryDTO> result = assignmentService.findAllAssignments(page, size);
        return ResponseMaker.ok(result);
    }

    @PatchMapping("/cancel")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @Operation(summary = "Cancel assignment when status ACCEPTED")
    public ResponseEntity<Response<?>> cancel(@RequestBody @Valid AssignmentCancelDTO dto) {
        assignmentService.cancel(dto);
        return ResponseMaker.okMessage("Canceled");
    }

    @PatchMapping("/change-status")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','MANAGER')")
    @Operation(summary = "Change status")
    public ResponseEntity<Response<?>> changeStatus(@RequestBody @Valid AssignmentStatusChangeDTO dto) {
        assignmentService.changeStatus(dto);
        return ResponseMaker.okMessage("Status changed");
    }

    @PatchMapping("/complete")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE')")
    @Operation(summary = "Complete")
    public ResponseEntity<Response<?>> complete(@RequestBody @Valid AssignmentCompleteDTO dto) {
        assignmentService.complete(dto);
        return ResponseMaker.okMessage("Assignment completed");
    }
}
