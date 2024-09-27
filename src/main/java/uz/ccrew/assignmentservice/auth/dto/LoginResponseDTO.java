package uz.ccrew.assignmentservice.auth.dto;

public record LoginResponseDTO(String accessToken,
                               String refreshToken) {
}
