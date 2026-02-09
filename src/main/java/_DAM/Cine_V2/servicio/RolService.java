package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.input.RolInputDTO;
import _DAM.Cine_V2.dto.output.RolOutputDTO;
import _DAM.Cine_V2.mapper.RolMapper;
import _DAM.Cine_V2.modelo.Rol;
import _DAM.Cine_V2.repositorio.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    public List<RolOutputDTO> findAll() {
        return rolRepository.findAll().stream()
                .map(rolMapper::toOutputDTO)
                .collect(Collectors.toList());
    }

    public RolOutputDTO findById(Long id) {
        return rolRepository.findById(id)
                .map(rolMapper::toOutputDTO)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    public RolOutputDTO save(RolInputDTO rolInputDTO) {
        Rol rol = rolMapper.toEntity(rolInputDTO);
        Rol saved = rolRepository.save(rol);
        return rolMapper.toOutputDTO(saved);
    }

    public RolOutputDTO update(Long id, RolInputDTO rolInputDTO) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado con ID: " + id);
        }
        Rol rol = rolMapper.toEntity(rolInputDTO);
        rol.setId(id);
        Rol saved = rolRepository.save(rol);
        return rolMapper.toOutputDTO(saved);
    }

    public void deleteById(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado con ID: " + id);
        }
        rolRepository.deleteById(id);
    }
}
