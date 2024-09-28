package uz.ccrew.assignmentservice.assignment.dto;

import uz.ccrew.assignmentservice.assignment.enums.Category;
import uz.ccrew.assignmentservice.assignmentchat.dto.MessageDTO;
import uz.ccrew.assignmentservice.assignment.enums.AssignmentStatus;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.time.LocalDateTime;

@Setter
@Getter
public class AssignmentDetailedDTO {
    private Long assigmentId;
    private Category category;
    private LocalDateTime date;
    private AssignmentStatus status;
    private Long paymentAmount;
    private String details;
    private String note;
    private List<MessageDTO> comments;

    public AssignmentDetailedDTO(Long assigmentId, Category category, LocalDateTime date, AssignmentStatus status, Long paymentAmount, String details, String note) {
        this.assigmentId = assigmentId;
        this.category = category;
        this.date = date;
        this.status = status;
        this.paymentAmount = paymentAmount;
        this.details = details;
        this.note = note;
    }
}
