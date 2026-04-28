package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.entrada.EntradaInputDTO;
import _DAM.Cine_V2.dto.entrada.EntradaOutputDTO;
import _DAM.Cine_V2.servicio.EntradaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entradas")
@RequiredArgsConstructor
public class EntradaController {

    private final EntradaService entradaService;

    @GetMapping
    public ResponseEntity<List<EntradaOutputDTO>> findAll() {
        return ResponseEntity.ok(entradaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaOutputDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(entradaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EntradaOutputDTO> create(@Valid @RequestBody EntradaInputDTO entradaDTO) {
        return new ResponseEntity<>(entradaService.save(entradaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntradaOutputDTO> update(@PathVariable Long id,
            @Valid @RequestBody EntradaInputDTO entradaDTO) {
        return ResponseEntity.ok(entradaService.update(id, entradaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        entradaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
