package uz.ccrew.assignmentservice.dto.auth;

public record LoginResponseDTO(String accessToken,
                               String refreshToken) {
}
