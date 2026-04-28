package _DAM.Cine_V2.dto.Auth;

public record LoginResponseDTO(
        String email,
        String message,
        String token
) {}
