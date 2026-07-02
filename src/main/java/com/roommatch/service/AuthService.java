package com.roommatch.service;

import com.roommatch.dto.LoginRequest;
import com.roommatch.dto.RegistroRequest;
import com.roommatch.dto.UsuarioResponse;
import com.roommatch.model.Rol;
import com.roommatch.model.Usuario;
import com.roommatch.repository.RolRepository;
import com.roommatch.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.roommatch.dto.LoginResponse;
import com.roommatch.security.JwtService;
import com.roommatch.util.ApiConstants;


@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
private final JwtService jwtService;
    public AuthService(
            UsuarioRepository usuarioRepository,
        RolRepository rolRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService
    ) {
       this.usuarioRepository = usuarioRepository;
    this.rolRepository = rolRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    }

    public UsuarioResponse registrarUsuario(RegistroRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Rol rolUsuario = rolRepository.findByNombreRol("USUARIO")
                .orElseThrow(() -> new IllegalArgumentException("No existe el rol USUARIO en la base de datos"));

        Usuario usuario = new Usuario();

        usuario.setRol(rolUsuario);
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setEdad(request.getEdad());
        usuario.setOcupacion(request.getOcupacion());
        usuario.setUniversidad(request.getUniversidad());
        usuario.setFoto(request.getFoto());
        usuario.setEstado("activo");

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return UsuarioResponse.fromEntity(usuarioGuardado);
    }
   public LoginResponse login(LoginRequest request) {

    Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("El correo no está registrado"));

    if (!usuario.getEstado().equalsIgnoreCase(ApiConstants.ESTADO_ACTIVO)) {
        throw new IllegalArgumentException("El usuario no se encuentra activo");
    }

    boolean passwordValida = passwordEncoder.matches(
            request.getPassword(),
            usuario.getPasswordHash()
    );

    if (!passwordValida) {
        throw new IllegalArgumentException("La contraseña es incorrecta");
    }

    String token = jwtService.generarToken(usuario);

    UsuarioResponse usuarioResponse = UsuarioResponse.fromEntity(usuario);

    return new LoginResponse(token, "Bearer", usuarioResponse);
}
}