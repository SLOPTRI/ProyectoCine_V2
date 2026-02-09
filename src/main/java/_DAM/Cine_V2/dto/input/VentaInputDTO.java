package _DAM.Cine_V2.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record VentaInputDTO(
        @NotBlank(message = "El método de pago es obligatorio") String metodoPago,
        @NotNull(message = "El usuario es obligatorio") Long usuarioId,
        @NotNull(message = "Debe haber al menos una entrada") Set<EntradaInputDTO> entradas) {
}
