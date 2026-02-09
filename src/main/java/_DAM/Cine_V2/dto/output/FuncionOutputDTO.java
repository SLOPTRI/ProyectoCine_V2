package _DAM.Cine_V2.dto.output;

import java.time.LocalDateTime;

public record FuncionOutputDTO(
        Long id,
        LocalDateTime fechaHora,
        double precio,
        Long peliculaId,
        Long salaId) {
}
