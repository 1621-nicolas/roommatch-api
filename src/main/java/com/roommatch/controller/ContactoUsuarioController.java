package com.roommatch.controller;

import com.roommatch.dto.ApiResponse;
import com.roommatch.dto.ContactoUsuarioRequest;
import com.roommatch.dto.ContactoUsuarioResponse;
import com.roommatch.model.Usuario;
import com.roommatch.service.ContactoUsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contactos")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactoUsuarioController {

    private final ContactoUsuarioService contactoService;

    public ContactoUsuarioController(ContactoUsuarioService contactoService) {
        this.contactoService = contactoService;
    }

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<ContactoUsuarioResponse>> guardarMiContacto(
            Authentication authentication,
            @Valid @RequestBody ContactoUsuarioRequest request
    ) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();

            ContactoUsuarioResponse response = contactoService.guardarOModificarMiContacto(
                    usuario.getIdUsuario(),
                    request
            );

            return ResponseEntity.ok(
                    ApiResponse.success(response, "Datos de contacto guardados correctamente")
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.fail(e.getMessage())
            );
        }
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ContactoUsuarioResponse>> actualizarMiContacto(
            Authentication authentication,
            @Valid @RequestBody ContactoUsuarioRequest request
    ) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();

            ContactoUsuarioResponse response = contactoService.guardarOModificarMiContacto(
                    usuario.getIdUsuario(),
                    request
            );

            return ResponseEntity.ok(
                    ApiResponse.success(response, "Datos de contacto actualizados correctamente")
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.fail(e.getMessage())
            );
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ContactoUsuarioResponse>> obtenerMiContacto(
            Authentication authentication
    ) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();

            ContactoUsuarioResponse response = contactoService.obtenerMiContacto(
                    usuario.getIdUsuario()
            );

            return ResponseEntity.ok(
                    ApiResponse.success(response, "Datos de contacto obtenidos correctamente")
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.fail(e.getMessage())
            );
        }
    }

    @GetMapping("/desbloqueado/{idUsuario}")
    public ResponseEntity<ApiResponse<ContactoUsuarioResponse>> verContactoDesbloqueado(
            Authentication authentication,
            @PathVariable Integer idUsuario
    ) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();

            ContactoUsuarioResponse response = contactoService.verContactoDesbloqueado(
                    usuario.getIdUsuario(),
                    idUsuario
            );

            return ResponseEntity.ok(
                    ApiResponse.success(response, "Contacto desbloqueado obtenido correctamente")
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.fail(e.getMessage())
            );
        }
    }
}