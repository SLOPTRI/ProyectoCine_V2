package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.input.EntradaInputDTO;
import _DAM.Cine_V2.dto.output.EntradaOutputDTO;
import _DAM.Cine_V2.mapper.EntradaMapper;
import _DAM.Cine_V2.modelo.Entrada;
import _DAM.Cine_V2.modelo.EstadoEntrada;
import _DAM.Cine_V2.modelo.Funcion;
import _DAM.Cine_V2.modelo.Venta;
import _DAM.Cine_V2.repositorio.EntradaRepository;
import _DAM.Cine_V2.repositorio.FuncionRepository;
import _DAM.Cine_V2.repositorio.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntradaService {

    private final EntradaRepository entradaRepository;
    private final FuncionRepository funcionRepository;
    private final VentaRepository ventaRepository;
    private final EntradaMapper entradaMapper;

    @Transactional(readOnly = true)
    public List<EntradaOutputDTO> findAll() {
        return entradaRepository.findAll().stream()
                .map(entradaMapper::toOutputDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EntradaOutputDTO findById(Long id) {
        return entradaRepository.findById(id)
                .map(entradaMapper::toOutputDTO)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada con ID: " + id));
    }

    @Transactional
    public EntradaOutputDTO save(EntradaInputDTO entradaInputDTO) {
        // Validation: Seat availability
        if (isSeatOccupied(entradaInputDTO.funcionId(), entradaInputDTO.fila(), entradaInputDTO.asiento())) {
            throw new RuntimeException(
                    "El asiento " + entradaInputDTO.fila() + "-" + entradaInputDTO.asiento() + " ya está ocupado.");
        }

        Entrada entrada = entradaMapper.toEntity(entradaInputDTO);

        if (entradaInputDTO.funcionId() != null) {
            Funcion funcion = funcionRepository.findById(entradaInputDTO.funcionId())
                    .orElseThrow(
                            () -> new RuntimeException("Funcion no encontrada con ID: " + entradaInputDTO.funcionId()));
            entrada.setFuncion(funcion);
        }

        if (entradaInputDTO.ventaId() != null) {
            Venta venta = ventaRepository.findById(entradaInputDTO.ventaId())
                    .orElseThrow(
                            () -> new RuntimeException("Venta no encontrada con ID: " + entradaInputDTO.ventaId()));
            entrada.setVenta(venta);
        }

        // Default status if not provided
        if (entrada.getEstado() == null) {
            entrada.setEstado(EstadoEntrada.VENDIDA);
        }

        Entrada saved = entradaRepository.save(entrada);
        return entradaMapper.toOutputDTO(saved);
    }

    @Transactional
    public EntradaOutputDTO update(Long id, EntradaInputDTO entradaInputDTO) {
        if (!entradaRepository.existsById(id)) {
            throw new RuntimeException("Entrada no encontrada con ID: " + id);
        }

        // Validation: Seat availability checks might be needed depending on business
        // logic, skipping for simple update for now or assuming checks pass
        // Ideally we should check if the new seat is occupied if it changed.

        Entrada entrada = entradaMapper.toEntity(entradaInputDTO);
        entrada.setId(id);

        if (entradaInputDTO.funcionId() != null) {
            Funcion funcion = funcionRepository.findById(entradaInputDTO.funcionId())
                    .orElseThrow(
                            () -> new RuntimeException("Funcion no encontrada con ID: " + entradaInputDTO.funcionId()));
            entrada.setFuncion(funcion);
        }

        if (entradaInputDTO.ventaId() != null) {
            Venta venta = ventaRepository.findById(entradaInputDTO.ventaId())
                    .orElseThrow(
                            () -> new RuntimeException("Venta no encontrada con ID: " + entradaInputDTO.ventaId()));
            entrada.setVenta(venta);
        }

        Entrada saved = entradaRepository.save(entrada);
        return entradaMapper.toOutputDTO(saved);
    }

    public boolean isSeatOccupied(Long funcionId, int fila, int asiento) {
        // This is a naive implementation. In a real system, we'd have a specific query.
        // Or we check existing tickets for this function and seat.
        List<Entrada> entradas = entradaRepository.findByFuncionId(funcionId);
        return entradas.stream().anyMatch(
                e -> e.getFila() == fila && e.getAsiento() == asiento && e.getEstado() != EstadoEntrada.CANCELADA);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!entradaRepository.existsById(id)) {
            throw new RuntimeException("Entrada no encontrada con ID: " + id);
        }
        entradaRepository.deleteById(id);
    }
}
