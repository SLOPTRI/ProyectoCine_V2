package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.input.DirectorInputDTO;
import _DAM.Cine_V2.dto.output.DirectorOutputDTO;
import _DAM.Cine_V2.mapper.DirectorMapper;
import _DAM.Cine_V2.modelo.Director;
import _DAM.Cine_V2.repositorio.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final DirectorMapper directorMapper;

    public List<DirectorOutputDTO> findAll() {
        return directorRepository.findAll().stream()
                .map(directorMapper::toOutputDTO)
                .collect(Collectors.toList());
    }

    public DirectorOutputDTO findById(Long id) {
        return directorRepository.findById(id)
                .map(directorMapper::toOutputDTO)
                .orElseThrow(() -> new RuntimeException("Director no encontrado con ID: " + id));
    }

    public DirectorOutputDTO save(DirectorInputDTO directorInputDTO) {
        Director director = directorMapper.toEntity(directorInputDTO);
        Director saved = directorRepository.save(director);
        return directorMapper.toOutputDTO(saved);
    }

    public DirectorOutputDTO update(Long id, DirectorInputDTO directorInputDTO) {
        if (!directorRepository.existsById(id)) {
            throw new RuntimeException("Director no encontrado con ID: " + id);
        }
        Director director = directorMapper.toEntity(directorInputDTO);
        director.setId(id);
        Director saved = directorRepository.save(director);
        return directorMapper.toOutputDTO(saved);
    }

    public void deleteById(Long id) {
        if (!directorRepository.existsById(id)) {
            throw new RuntimeException("Director no encontrado con ID: " + id);
        }
        directorRepository.deleteById(id);
    }
}
