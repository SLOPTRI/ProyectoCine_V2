package _DAM.Cine_V2.dto.venta;

import _DAM.Cine_V2.dto.entrada.EntradaOutputDTO;
import java.time.LocalDateTime;
import java.util.Set;

public record VentaOutputDTO(
        Long id,
        LocalDateTime fecha,
        double importeTotal,
        String metodoPago,
        String estado,
        Set<EntradaOutputDTO> entradas,
        Long usuarioId) {
}
