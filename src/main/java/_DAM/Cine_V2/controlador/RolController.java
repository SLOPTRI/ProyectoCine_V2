package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.input.RolInputDTO;
import _DAM.Cine_V2.dto.output.RolOutputDTO;
import _DAM.Cine_V2.servicio.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @GetMapping
    public ResponseEntity<List<RolOutputDTO>> findAll() {
        return ResponseEntity.ok(rolService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolOutputDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RolOutputDTO> create(@Valid @RequestBody RolInputDTO rolInputDTO) {
        return new ResponseEntity<>(rolService.save(rolInputDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolOutputDTO> update(@PathVariable Long id, @Valid @RequestBody RolInputDTO rolInputDTO) {
        return ResponseEntity.ok(rolService.update(id, rolInputDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
