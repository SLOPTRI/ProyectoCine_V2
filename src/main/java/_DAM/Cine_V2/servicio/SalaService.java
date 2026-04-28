package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.sala.SalaInputDTO;
import _DAM.Cine_V2.dto.sala.SalaOutputDTO;
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
                .map(salaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SalaOutputDTO findById(Long id) {
        return salaRepository.findById(id)
                .map(salaMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada con ID: " + id));
    }

    public SalaOutputDTO save(SalaInputDTO salaDTO) {
        Sala sala = salaMapper.toEntity(salaDTO);
        Sala saved = salaRepository.save(sala);
        return salaMapper.toDTO(saved);
    }

    public SalaOutputDTO update(Long id, SalaInputDTO salaDTO) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada con ID: " + id));
        salaMapper.update(salaDTO, sala);
        return salaMapper.toDTO(salaRepository.save(sala));
    }

    public void deleteById(Long id) {
        if (!salaRepository.existsById(id)) {
            throw new RuntimeException("Sala no encontrada con ID: " + id);
        }
        salaRepository.deleteById(id);
    }
}
