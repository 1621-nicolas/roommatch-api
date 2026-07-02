package com.roommatch.controller;

import com.roommatch.dto.ApiResponse;
import com.roommatch.dto.PropietarioRequest;
import com.roommatch.dto.PropietarioResponse;
import com.roommatch.model.Usuario;
import com.roommatch.service.PropietarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/propietarios")
@CrossOrigin(origins = "http://localhost:4200")
public class PropietarioController {

    private final PropietarioService propietarioService;

    public PropietarioController(PropietarioService propietarioService) {
        this.propietarioService = propietarioService;
    }

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<PropietarioResponse>> convertirmeEnPropietario(
            Authentication authentication,
            @Valid @RequestBody PropietarioRequest request
    ) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();

            PropietarioResponse response = propietarioService.convertirmeEnPropietario(
                    usuario.getIdUsuario(),
                    request
            );

            return ResponseEntity.ok(
                    ApiResponse.success(response, "Perfil de propietario creado correctamente")
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.fail(e.getMessage())
            );
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PropietarioResponse>> obtenerMiPerfilPropietario(
            Authentication authentication
    ) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();

            PropietarioResponse response = propietarioService.obtenerMiPerfilPropietario(
                    usuario.getIdUsuario()
            );

            return ResponseEntity.ok(
                    ApiResponse.success(response, "Perfil de propietario obtenido correctamente")
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.fail(e.getMessage())
            );
        }
    }
}