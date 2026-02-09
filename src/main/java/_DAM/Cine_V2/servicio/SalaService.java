package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.input.SalaInputDTO;
import _DAM.Cine_V2.dto.output.SalaOutputDTO;
import _DAM.Cine_V2.mapper.SalaMapper;
import _DAM.Cine_V2.modelo.Sala;
import _DAM.Cine_V2.repositorio.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;
    private final SalaMapper salaMapper;

    public List<SalaOutputDTO> findAll() {
        return salaRepository.findAll().stream()
                .map(salaMapper::toOutputDTO)
                .collect(Collectors.toList());
    }

    public SalaOutputDTO findById(Long id) {
        return salaRepository.findById(id)
                .map(salaMapper::toOutputDTO)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada con ID: " + id));
    }

    public SalaOutputDTO save(SalaInputDTO salaInputDTO) {
        Sala sala = salaMapper.toEntity(salaInputDTO);
        Sala saved = salaRepository.save(sala);
        return salaMapper.toOutputDTO(saved);
    }

    public SalaOutputDTO update(Long id, SalaInputDTO salaInputDTO) {
        if (!salaRepository.existsById(id)) {
            throw new RuntimeException("Sala no encontrada con ID: " + id);
        }
        Sala sala = salaMapper.toEntity(salaInputDTO);
        sala.setId(id);
        Sala saved = salaRepository.save(sala);
        return salaMapper.toOutputDTO(saved);
    }

    public void deleteById(Long id) {
        if (!salaRepository.existsById(id)) {
            throw new RuntimeException("Sala no encontrada con ID: " + id);
        }
        salaRepository.deleteById(id);
    }
}
