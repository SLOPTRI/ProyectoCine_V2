package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.pelicula.PeliculaInputDTO;
import _DAM.Cine_V2.dto.pelicula.PeliculaOutputDTO;
import _DAM.Cine_V2.servicio.PeliculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/peliculas")
@RequiredArgsConstructor
public class PeliculaController {

    private final PeliculaService peliculaService;

    @GetMapping
    public ResponseEntity<List<PeliculaOutputDTO>> findAll() {
        return ResponseEntity.ok(peliculaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeliculaOutputDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(peliculaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PeliculaOutputDTO> create(@Valid @RequestBody PeliculaInputDTO peliculaDTO) {
        return new ResponseEntity<>(peliculaService.save(peliculaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeliculaOutputDTO> update(@PathVariable Long id,
            @Valid @RequestBody PeliculaInputDTO peliculaDTO) {
        return ResponseEntity.ok(peliculaService.update(id, peliculaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        peliculaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
