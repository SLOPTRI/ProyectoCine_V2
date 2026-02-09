package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.input.UsuarioInputDTO;
import _DAM.Cine_V2.dto.output.UsuarioOutputDTO;
import _DAM.Cine_V2.mapper.UsuarioMapper;
import _DAM.Cine_V2.modelo.Rol;
import _DAM.Cine_V2.modelo.Usuario;
import _DAM.Cine_V2.repositorio.RolRepository;
import _DAM.Cine_V2.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;

    public List<UsuarioOutputDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toOutputDTO)
                .collect(Collectors.toList());
    }

    public UsuarioOutputDTO findById(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toOutputDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Transactional
    public UsuarioOutputDTO save(UsuarioInputDTO usuarioInputDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioInputDTO);

        // Handle Roles
        if (usuarioInputDTO.roles() != null && !usuarioInputDTO.roles().isEmpty()) {
            Set<Rol> roles = new HashSet<>();
            for (String rolNombre : usuarioInputDTO.roles()) {
                Rol rol = rolRepository.findByNombre(rolNombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
                roles.add(rol);
            }
            usuario.setRoles(roles);
        }

        // Handle password (basic for now)
        if (usuarioInputDTO.password() != null && !usuarioInputDTO.password().isBlank()) {
            usuario.setPassword(usuarioInputDTO.password()); // In real app, B.crypt here
        }

        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toOutputDTO(saved);
    }

    @Transactional
    public UsuarioOutputDTO update(Long id, UsuarioInputDTO usuarioInputDTO) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        Usuario usuario = usuarioMapper.toEntity(usuarioInputDTO);
        usuario.setId(id);

        // Handle Roles
        if (usuarioInputDTO.roles() != null && !usuarioInputDTO.roles().isEmpty()) {
            Set<Rol> roles = new HashSet<>();
            for (String rolNombre : usuarioInputDTO.roles()) {
                Rol rol = rolRepository.findByNombre(rolNombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
                roles.add(rol);
            }
            usuario.setRoles(roles);
        }

        // Handle password (basic for now)
        if (usuarioInputDTO.password() != null && !usuarioInputDTO.password().isBlank()) {
            usuario.setPassword(usuarioInputDTO.password()); // In real app, B.crypt here
        }

        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toOutputDTO(saved);
    }

    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
