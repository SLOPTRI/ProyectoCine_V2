package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.input.FuncionInputDTO;
import _DAM.Cine_V2.dto.output.FuncionOutputDTO;
import _DAM.Cine_V2.mapper.FuncionMapper;
import _DAM.Cine_V2.modelo.Funcion;
import _DAM.Cine_V2.modelo.Pelicula;
import _DAM.Cine_V2.modelo.Sala;
import _DAM.Cine_V2.repositorio.FuncionRepository;
import _DAM.Cine_V2.repositorio.PeliculaRepository;
import _DAM.Cine_V2.repositorio.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncionService {

    private final FuncionRepository funcionRepository;
    private final PeliculaRepository peliculaRepository;
    private final SalaRepository salaRepository;
    private final FuncionMapper funcionMapper;

    @Transactional(readOnly = true)
    public List<FuncionOutputDTO> findAll() {
        return funcionRepository.findAll().stream()
                .map(funcionMapper::toOutputDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuncionOutputDTO findById(Long id) {
        return funcionRepository.findById(id)
                .map(funcionMapper::toOutputDTO)
                .orElseThrow(() -> new RuntimeException("Funcion no encontrada con ID: " + id));
    }

    @Transactional
    public FuncionOutputDTO save(FuncionInputDTO funcionInputDTO) {
        Funcion funcion = funcionMapper.toEntity(funcionInputDTO);

        if (funcionInputDTO.peliculaId() != null) {
            Pelicula pelicula = peliculaRepository.findById(funcionInputDTO.peliculaId())
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Pelicula no encontrada con ID: " + funcionInputDTO.peliculaId()));
            funcion.setPelicula(pelicula);
        }

        if (funcionInputDTO.salaId() != null) {
            Sala sala = salaRepository.findById(funcionInputDTO.salaId())
                    .orElseThrow(() -> new RuntimeException("Sala no encontrada con ID: " + funcionInputDTO.salaId()));
            funcion.setSala(sala);
        }

        Funcion saved = funcionRepository.save(funcion);
        return funcionMapper.toOutputDTO(saved);
    }

    @Transactional
    public FuncionOutputDTO update(Long id, FuncionInputDTO funcionInputDTO) {
        if (!funcionRepository.existsById(id)) {
            throw new RuntimeException("Funcion no encontrada con ID: " + id);
        }
        Funcion funcion = funcionMapper.toEntity(funcionInputDTO);
        funcion.setId(id);

        if (funcionInputDTO.peliculaId() != null) {
            Pelicula pelicula = peliculaRepository.findById(funcionInputDTO.peliculaId())
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Pelicula no encontrada con ID: " + funcionInputDTO.peliculaId()));
            funcion.setPelicula(pelicula);
        }

        if (funcionInputDTO.salaId() != null) {
            Sala sala = salaRepository.findById(funcionInputDTO.salaId())
                    .orElseThrow(() -> new RuntimeException("Sala no encontrada con ID: " + funcionInputDTO.salaId()));
            funcion.setSala(sala);
        }

        Funcion saved = funcionRepository.save(funcion);
        return funcionMapper.toOutputDTO(saved);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!funcionRepository.existsById(id)) {
            throw new RuntimeException("Funcion no encontrada con ID: " + id);
        }
        funcionRepository.deleteById(id);
    }
}
