package uz.ccrew.assignmentservice.user.dto;

import uz.ccrew.assignmentservice.user.UserRole;

import lombok.Builder;

@Builder
public record UserDTO(Long id, String login, UserRole role, String fullName) {
}