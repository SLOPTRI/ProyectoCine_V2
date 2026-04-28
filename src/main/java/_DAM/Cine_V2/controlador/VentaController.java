package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.venta.VentaInputDTO;
import _DAM.Cine_V2.dto.venta.VentaOutputDTO;
import _DAM.Cine_V2.servicio.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<VentaOutputDTO>> findAll() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaOutputDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<VentaOutputDTO> create(@Valid @RequestBody VentaInputDTO ventaDTO) {
        return new ResponseEntity<>(ventaService.save(ventaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaOutputDTO> update(@PathVariable Long id, @Valid @RequestBody VentaInputDTO ventaDTO) {
        return ResponseEntity.ok(ventaService.update(id, ventaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ventaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
