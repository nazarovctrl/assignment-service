package uz.ccrew.assignmentservice.dto.assignment;

import lombok.Builder;

import java.util.List;

@Builder
public record AssignmentColumnsDTO (List<String> columns){}
