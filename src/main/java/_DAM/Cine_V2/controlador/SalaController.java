package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.input.SalaInputDTO;
import _DAM.Cine_V2.dto.output.SalaOutputDTO;
import _DAM.Cine_V2.servicio.SalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salas")
@RequiredArgsConstructor
public class SalaController {

    private final SalaService salaService;

    @GetMapping
    public ResponseEntity<List<SalaOutputDTO>> findAll() {
        return ResponseEntity.ok(salaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaOutputDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SalaOutputDTO> create(@Valid @RequestBody SalaInputDTO salaInputDTO) {
        return new ResponseEntity<>(salaService.save(salaInputDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaOutputDTO> update(@PathVariable Long id, @Valid @RequestBody SalaInputDTO salaInputDTO) {
        return ResponseEntity.ok(salaService.update(id, salaInputDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
