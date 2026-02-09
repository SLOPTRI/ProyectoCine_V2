package _DAM.Cine_V2.dto.output;

import java.util.Set;

public record PeliculaOutputDTO(
        Long id,
        String titulo,
        int duracion,
        int edadMinima,
        Long directorId,
        Set<Long> actorIds) {
}
