package uz.ccrew.assignmentservice.assignmentchat;

import uz.ccrew.assignmentservice.base.dto.Response;
import uz.ccrew.assignmentservice.base.dto.ResponseMaker;
import uz.ccrew.assignmentservice.assignmentchat.dto.MessageDTO;
import uz.ccrew.assignmentservice.assignmentchat.dto.SendMessageDTO;
import uz.ccrew.assignmentservice.assignmentchat.service.AssignmentChatService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/assignment-chat")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Tag(name = "Assignment Chat Controller", description = "Assignment Chat API")
public class AssignmentChatController {
    private final AssignmentChatService assignmentChatService;

    @PostMapping("/send-message")
    @PreAuthorize("hasAnyAuthority('CUSTOMER','EMPLOYEE')")
    @Operation(summary = "Send message to assignment chat")
    public ResponseEntity<Response<MessageDTO>> sendMessage(@RequestBody @Valid SendMessageDTO dto) {
        MessageDTO result = assignmentChatService.sendMessage(dto);
        return ResponseMaker.ok(result);
    }
}
