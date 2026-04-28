package _DAM.Cine_V2.dto.director;

import jakarta.validation.constraints.NotBlank;

public record DirectorInputDTO(
        @NotBlank(message = "El nombre no puede estar vacío") String nombre) {
}
