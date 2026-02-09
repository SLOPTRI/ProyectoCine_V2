package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.input.EntradaInputDTO;
import _DAM.Cine_V2.dto.input.VentaInputDTO;
import _DAM.Cine_V2.dto.output.VentaOutputDTO;
import _DAM.Cine_V2.mapper.EntradaMapper;
import _DAM.Cine_V2.mapper.VentaMapper;
import _DAM.Cine_V2.modelo.*;
import _DAM.Cine_V2.repositorio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final FuncionRepository funcionRepository;
    // We don't necessarily need EntradaService if we implement logic here, but
    // using repository approach
    private final EntradaRepository entradaRepository;
    private final VentaMapper ventaMapper;
    private final EntradaMapper entradaMapper;

    public List<VentaOutputDTO> findAll() {
        return ventaRepository.findAll().stream()
                .map(ventaMapper::toOutputDTO)
                .collect(Collectors.toList());
    }

    public VentaOutputDTO findById(Long id) {
        return ventaRepository.findById(id)
                .map(ventaMapper::toOutputDTO)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    @Transactional
    public VentaOutputDTO save(VentaInputDTO ventaInputDTO) {
        Venta venta = ventaMapper.toEntity(ventaInputDTO);

        if (ventaInputDTO.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(ventaInputDTO.usuarioId())
                    .orElseThrow(
                            () -> new RuntimeException("Usuario no encontrado con ID: " + ventaInputDTO.usuarioId()));
            venta.setUsuario(usuario);
        }

        // If we want to create tickets along with sale:
        if (ventaInputDTO.entradas() != null) {
            Set<Entrada> entradasEntities = new HashSet<>();
            for (EntradaInputDTO eDTO : ventaInputDTO.entradas()) {
                // Check function
                if (eDTO.funcionId() == null)
                    throw new RuntimeException("Entrada sin funcion ID");
                Funcion funcion = funcionRepository.findById(eDTO.funcionId())
                        .orElseThrow(() -> new RuntimeException("Funcion no encontrada " + eDTO.funcionId()));

                // Check availability (Naive check, assuming no concurrency issues for this
                // exercise)
                boolean occupied = entradaRepository.findByFuncionId(funcion.getId()).stream()
                        .anyMatch(e -> e.getFila() == eDTO.fila() && e.getAsiento() == eDTO.asiento()
                                && e.getEstado() != EstadoEntrada.CANCELADA);

                if (occupied) {
                    throw new RuntimeException("Asiento ocupado: " + eDTO.fila() + "-" + eDTO.asiento());
                }

                Entrada entrada = entradaMapper.toEntity(eDTO);
                entrada.setFuncion(funcion);
                entrada.setVenta(venta);
                if (entrada.getEstado() == null)
                    entrada.setEstado(EstadoEntrada.VENDIDA);
                entradasEntities.add(entrada);
            }
            venta.setEntradas(entradasEntities);
        }

        Venta saved = ventaRepository.save(venta);
        return ventaMapper.toOutputDTO(saved);
    }

    @Transactional
    public VentaOutputDTO update(Long id, VentaInputDTO ventaInputDTO) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        Venta venta = ventaMapper.toEntity(ventaInputDTO);
        venta.setId(id);

        if (ventaInputDTO.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(ventaInputDTO.usuarioId())
                    .orElseThrow(
                            () -> new RuntimeException("Usuario no encontrado con ID: " + ventaInputDTO.usuarioId()));
            venta.setUsuario(usuario);
        }

        // Handling entradas update is complex, for now assuming we replace them or add
        // new ones logic needs to be defined.
        // For simplicity reusing the same logic as save but this might duplicate if not
        // careful.
        // In a real scenario, we might want to update existing tickets or add new ones.
        // Here we will just clear and re-add if provided (careful with existing IDs)
        // OR better, just update the Venta fields and leave Entradas management to
        // EntradaService or specific Venta methods.
        // For this refactoring, I will replicate the 'save' logic for relations but
        // usually update handles relations more carefully.

        if (ventaInputDTO.entradas() != null) {
            Set<Entrada> entradasEntities = new HashSet<>();
            for (EntradaInputDTO eDTO : ventaInputDTO.entradas()) {
                Funcion funcion = funcionRepository.findById(eDTO.funcionId())
                        .orElseThrow(() -> new RuntimeException("Funcion no encontrada " + eDTO.funcionId()));
                Entrada entrada = entradaMapper.toEntity(eDTO);
                entrada.setFuncion(funcion);
                entrada.setVenta(venta);
                if (entrada.getEstado() == null)
                    entrada.setEstado(EstadoEntrada.VENDIDA);
                entradasEntities.add(entrada);
            }
            venta.setEntradas(entradasEntities);
        }

        Venta saved = ventaRepository.save(venta);
        return ventaMapper.toOutputDTO(saved);
    }

    public void deleteById(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        ventaRepository.deleteById(id);
    }
}
