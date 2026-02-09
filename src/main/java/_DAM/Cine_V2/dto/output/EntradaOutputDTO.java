package _DAM.Cine_V2.dto.output;

import _DAM.Cine_V2.modelo.EstadoEntrada;

public record EntradaOutputDTO(
        Long id,
        String codigo,
        int fila,
        int asiento,
        EstadoEntrada estado,
        Long funcionId,
        Long ventaId) {
}
