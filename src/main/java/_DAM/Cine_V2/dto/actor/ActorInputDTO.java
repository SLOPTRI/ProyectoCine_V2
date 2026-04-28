package _DAM.Cine_V2.dto.actor;

import jakarta.validation.constraints.NotBlank;

public record ActorInputDTO(
        @NotBlank(message = "El nombre no puede estar vacío") String nombre) {
}
