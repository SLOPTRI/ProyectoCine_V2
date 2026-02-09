package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.input.ActorInputDTO;
import _DAM.Cine_V2.dto.output.ActorOutputDTO;
import _DAM.Cine_V2.servicio.ActorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/actores")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    @GetMapping
    public ResponseEntity<List<ActorOutputDTO>> findAll() {
        return ResponseEntity.ok(actorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorOutputDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(actorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ActorOutputDTO> create(@Valid @RequestBody ActorInputDTO actorInputDTO) {
        return new ResponseEntity<>(actorService.save(actorInputDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorOutputDTO> update(@PathVariable Long id,
            @Valid @RequestBody ActorInputDTO actorInputDTO) {
        return ResponseEntity.ok(actorService.update(id, actorInputDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        actorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
