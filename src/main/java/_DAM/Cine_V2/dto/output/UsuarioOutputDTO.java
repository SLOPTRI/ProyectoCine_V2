package _DAM.Cine_V2.dto.output;

import java.util.Set;

public record UsuarioOutputDTO(
        Long id,
        String email,
        boolean enabled,
        Set<String> roles) {
}
