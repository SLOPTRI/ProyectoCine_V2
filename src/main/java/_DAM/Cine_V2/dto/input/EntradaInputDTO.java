package _DAM.Cine_V2.dto.input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EntradaInputDTO(
                @Min(1) int fila,
                @Min(1) int asiento,
                @NotNull(message = "La función es obligatoria") Long funcionId,
                Long ventaId) {
}
