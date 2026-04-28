package _DAM.Cine_V2.controlador;

import _DAM.Cine_V2.dto.Auth.LoginRequestDTO;
import _DAM.Cine_V2.dto.Auth.LoginResponseDTO;
import _DAM.Cine_V2.dto.Auth.RegisterRequestDTO;
import _DAM.Cine_V2.repositorio.UsuarioRepository;
import _DAM.Cine_V2.servicio.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO req) {
        usuarioService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado correctamente");
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequestDTO req) {
        usuarioService.registerAdmin(req);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO req) {
        return ResponseEntity.ok(usuarioService.login(req));
    }

    @GetMapping("/ok")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Todo ok");
    }
}
