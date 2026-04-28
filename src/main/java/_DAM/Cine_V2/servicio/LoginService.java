package _DAM.Cine_V2.servicio;

import _DAM.Cine_V2.dto.login.LoginRequest;
import _DAM.Cine_V2.dto.login.LoginResponse;
import _DAM.Cine_V2.dto.login.RegistrerRequest;
import _DAM.Cine_V2.mapper.UsuarioMapper;
import _DAM.Cine_V2.repositorio.RolRepository;
import _DAM.Cine_V2.repositorio.UsuarioRepository;

public class LoginService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.email()).orElseThrow();
        return null;
    }

    public void register(RegistrerRequest request) {

    }
}