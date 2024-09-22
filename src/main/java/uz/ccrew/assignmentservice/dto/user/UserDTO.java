package uz.ccrew.assignmentservice.dto.user;

import uz.ccrew.assignmentservice.enums.UserRole;

import lombok.Builder;

@Builder
public record UserDTO(Long id, String login, UserRole role) {
}