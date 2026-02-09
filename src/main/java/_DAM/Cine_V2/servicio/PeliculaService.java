package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.input.PeliculaInputDTO;
import _DAM.Cine_V2.dto.output.PeliculaOutputDTO;
import _DAM.Cine_V2.mapper.PeliculaMapper;
import _DAM.Cine_V2.modelo.Actor;
import _DAM.Cine_V2.modelo.Director;
import _DAM.Cine_V2.modelo.Pelicula;
import _DAM.Cine_V2.repositorio.ActorRepository;
import _DAM.Cine_V2.repositorio.DirectorRepository;
import _DAM.Cine_V2.repositorio.PeliculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeliculaService {

    private final PeliculaRepository peliculaRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;
    private final PeliculaMapper peliculaMapper;

    @Transactional(readOnly = true)
    public List<PeliculaOutputDTO> findAll() {
        return peliculaRepository.findAll().stream()
                .map(peliculaMapper::toOutputDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PeliculaOutputDTO findById(Long id) {
        return peliculaRepository.findById(id)
                .map(peliculaMapper::toOutputDTO)
                .orElseThrow(() -> new RuntimeException("Pelicula no encontrada con ID: " + id));
    }

    @Transactional
    public PeliculaOutputDTO save(PeliculaInputDTO peliculaInputDTO) {
        Pelicula pelicula = peliculaMapper.toEntity(peliculaInputDTO);

        if (peliculaInputDTO.directorId() != null) {
            Director director = directorRepository.findById(peliculaInputDTO.directorId())
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Director no encontrado con ID: " + peliculaInputDTO.directorId()));
            pelicula.setDirector(director);
        }

        if (peliculaInputDTO.actorIds() != null && !peliculaInputDTO.actorIds().isEmpty()) {
            List<Actor> actores = actorRepository.findAllById(peliculaInputDTO.actorIds());
            if (actores.size() != peliculaInputDTO.actorIds().size()) {
                throw new RuntimeException("Algunos actores no fueron encontrados");
            }
            pelicula.setActores(new HashSet<>(actores));
        }

        Pelicula saved = peliculaRepository.save(pelicula);
        return peliculaMapper.toOutputDTO(saved);
    }

    @Transactional
    public PeliculaOutputDTO update(Long id, PeliculaInputDTO peliculaInputDTO) {
        if (!peliculaRepository.existsById(id)) {
            throw new RuntimeException("Pelicula no encontrada con ID: " + id);
        }

        Pelicula pelicula = peliculaMapper.toEntity(peliculaInputDTO);
        pelicula.setId(id); // Set ID to ensure update

        // Re-use logic to set relationships (could be extracted to a helper method)
        if (peliculaInputDTO.directorId() != null) {
            Director director = directorRepository.findById(peliculaInputDTO.directorId())
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Director no encontrado con ID: " + peliculaInputDTO.directorId()));
            pelicula.setDirector(director);
        }

        if (peliculaInputDTO.actorIds() != null && !peliculaInputDTO.actorIds().isEmpty()) {
            List<Actor> actores = actorRepository.findAllById(peliculaInputDTO.actorIds());
            if (actores.size() != peliculaInputDTO.actorIds().size()) {
                throw new RuntimeException("Algunos actores no fueron encontrados");
            }
            pelicula.setActores(new HashSet<>(actores));
        }

        Pelicula saved = peliculaRepository.save(pelicula);
        return peliculaMapper.toOutputDTO(saved);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!peliculaRepository.existsById(id)) {
            throw new RuntimeException("Pelicula no encontrada con ID: " + id);
        }
        peliculaRepository.deleteById(id);
    }
}
