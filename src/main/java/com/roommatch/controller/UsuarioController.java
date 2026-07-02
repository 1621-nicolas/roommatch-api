package com.roommatch.controller;

import com.roommatch.dto.ApiResponse;
import com.roommatch.dto.UsuarioResponse;
import com.roommatch.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtenerUsuarioActual(
            Authentication authentication
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        UsuarioResponse response = UsuarioResponse.fromEntity(usuario);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Usuario autenticado correctamente")
        );
    }
}