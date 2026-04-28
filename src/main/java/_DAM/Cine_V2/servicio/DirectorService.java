package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.director.DirectorInputDTO;
import _DAM.Cine_V2.dto.director.DirectorOutputDTO;
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
                .map(directorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DirectorOutputDTO findById(Long id) {
        return directorRepository.findById(id)
                .map(directorMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Director no encontrado con ID: " + id));
    }

    public DirectorOutputDTO save(DirectorInputDTO directorDTO) {
        Director director = directorMapper.toEntity(directorDTO);
        Director saved = directorRepository.save(director);
        return directorMapper.toDTO(saved);
    }

    public DirectorOutputDTO update(Long id, DirectorInputDTO directorDTO) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Director no encontrado con ID: " + id));
        directorMapper.update(directorDTO, director);
        return directorMapper.toDTO(directorRepository.save(director));
    }

    public void deleteById(Long id) {
        if (!directorRepository.existsById(id)) {
            throw new RuntimeException("Director no encontrado con ID: " + id);
        }
        directorRepository.deleteById(id);
    }
}
