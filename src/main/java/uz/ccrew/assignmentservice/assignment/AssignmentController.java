package uz.ccrew.assignmentservice.assignment;

import uz.ccrew.assignmentservice.base.dto.Response;
import uz.ccrew.assignmentservice.base.dto.ResponseMaker;
import uz.ccrew.assignmentservice.assignment.dto.WithdrawDTO;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentCancelDTO;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentCreateDTO;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentColumnsDTO;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentSummaryDTO;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentCompleteDTO;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentDetailedDTO;
import uz.ccrew.assignmentservice.assignment.service.AssignmentService;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentStatusChangeDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Map;

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
        Page<AssignmentSummaryDTO> result = assignmentService.getSummary(page, size);
        return ResponseMaker.ok(result);
    }

    @GetMapping("/detailed/{assignmentId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Get detailed assignment")
    public ResponseEntity<Response<AssignmentDetailedDTO>> getDetailed(@PathVariable("assignmentId") Long id) {
        AssignmentDetailedDTO result = assignmentService.getDetailed(id);
        return ResponseMaker.ok(result);
    }

    @GetMapping("/get/categories")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Get all categories")
    public ResponseEntity<Response<Map<String, String>>> getAllCategories() {
        Map<String, String> categories = assignmentService.getAllCategories();
        return ResponseMaker.ok(categories);
    }

    @GetMapping("/get-columns/{category}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Get all columns for category")
    public ResponseEntity<Response<AssignmentColumnsDTO>> getColumns(@PathVariable("category") String category) {
        AssignmentColumnsDTO result = assignmentService.getColumns(category);
        return ResponseMaker.ok(result);
    }

    @PostMapping("/create/assignment")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Create assignment")
    public ResponseEntity<Response<AssignmentSummaryDTO>> createAssignment(@RequestBody @Valid AssignmentCreateDTO assignmentCreateDTO) {
        AssignmentSummaryDTO result = assignmentService.createAssignment(assignmentCreateDTO);
        return ResponseMaker.ok(result);
    }

    @PostMapping("/withdraw-again")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Withdraw again")
    public ResponseEntity<Response<AssignmentSummaryDTO>> withdrawAgain(@RequestBody @Valid WithdrawDTO dto) {
        AssignmentSummaryDTO result = assignmentService.withdrawAgain(dto);
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