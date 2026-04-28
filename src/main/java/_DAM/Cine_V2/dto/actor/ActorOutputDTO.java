package _DAM.Cine_V2.dto.actor;

import jakarta.validation.constraints.NotBlank;

public record ActorOutputDTO(
        Long id,
        String nombre) {
}
