package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.input.ActorInputDTO;
import _DAM.Cine_V2.dto.output.ActorOutputDTO;
import _DAM.Cine_V2.mapper.ActorMapper;
import _DAM.Cine_V2.modelo.Actor;
import _DAM.Cine_V2.repositorio.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;

    public List<ActorOutputDTO> findAll() {
        return actorRepository.findAll().stream()
                .map(actorMapper::toOutputDTO)
                .collect(Collectors.toList());
    }

    public ActorOutputDTO findById(Long id) {
        return actorRepository.findById(id)
                .map(actorMapper::toOutputDTO)
                .orElseThrow(() -> new RuntimeException("Actor no encontrado con ID: " + id));
    }

    public ActorOutputDTO save(ActorInputDTO actorInputDTO) {
        Actor actor = actorMapper.toEntity(actorInputDTO);
        Actor saved = actorRepository.save(actor);
        return actorMapper.toOutputDTO(saved);
    }

    public ActorOutputDTO update(Long id, ActorInputDTO actorInputDTO) {
        if (!actorRepository.existsById(id)) {
            throw new RuntimeException("Actor no encontrado con ID: " + id);
        }
        Actor actor = actorMapper.toEntity(actorInputDTO);
        actor.setId(id);
        Actor saved = actorRepository.save(actor);
        return actorMapper.toOutputDTO(saved);
    }

    public void deleteById(Long id) {
        if (!actorRepository.existsById(id)) {
            throw new RuntimeException("Actor no encontrado con ID: " + id);
        }
        actorRepository.deleteById(id);
    }
}
