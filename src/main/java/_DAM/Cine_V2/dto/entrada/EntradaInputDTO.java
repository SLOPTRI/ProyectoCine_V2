package _DAM.Cine_V2.dto.entrada;

import jakarta.validation.constraints.Min;

public record EntradaInputDTO(
        Long ventaId,
        Long funcionId,
        @Min(1) int fila,
        @Min(1) int asiento) {
}
