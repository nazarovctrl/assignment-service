package uz.ccrew.assignmentservice.assignment.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AssignmentColumnsDTO (List<String> columns){}
