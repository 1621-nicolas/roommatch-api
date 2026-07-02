package com.roommatch.controller;

import com.roommatch.dto.ApiResponse;
import com.roommatch.dto.PublicacionRoomieRequest;
import com.roommatch.dto.PublicacionRoomieResponse;
import com.roommatch.model.Usuario;
import com.roommatch.service.PublicacionRoomieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/publicaciones-roomie")
@CrossOrigin(origins = "http://localhost:4200")
public class PublicacionRoomieController {

    private final PublicacionRoomieService publicacionService;

    public PublicacionRoomieController(PublicacionRoomieService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PublicacionRoomieResponse>> crearPublicacion(
            Authentication authentication,
            @Valid @RequestBody PublicacionRoomieRequest request
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        PublicacionRoomieResponse response = publicacionService.crearPublicacion(
                usuario.getIdUsuario(),
                request
        );

        return ResponseEntity.ok(
                ApiResponse.success(response, "Publicación roomie creada correctamente")
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PublicacionRoomieResponse>>> listarPublicaciones(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String distrito,
            @RequestParam(required = false) BigDecimal presupuestoMin,
            @RequestParam(required = false) BigDecimal presupuestoMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<PublicacionRoomieResponse> publicaciones =
                publicacionService.listarPublicaciones(
                        tipo,
                        distrito,
                        presupuestoMin,
                        presupuestoMax,
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(publicaciones, "Publicaciones roomie obtenidas correctamente")
        );
    }

    @GetMapping("/{idPublicacion}")
    public ResponseEntity<ApiResponse<PublicacionRoomieResponse>> obtenerPublicacion(
            @PathVariable Integer idPublicacion
    ) {
        PublicacionRoomieResponse response =
                publicacionService.obtenerPublicacion(idPublicacion);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Publicación roomie obtenida correctamente")
        );
    }

    @GetMapping("/mis")
    public ResponseEntity<ApiResponse<Page<PublicacionRoomieResponse>>> listarMisPublicaciones(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Pageable pageable = PageRequest.of(page, size);

        Page<PublicacionRoomieResponse> publicaciones =
                publicacionService.listarMisPublicaciones(
                        usuario.getIdUsuario(),
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(publicaciones, "Mis publicaciones obtenidas correctamente")
        );
    }

    @PutMapping("/{idPublicacion}")
    public ResponseEntity<ApiResponse<PublicacionRoomieResponse>> actualizarPublicacion(
            Authentication authentication,
            @PathVariable Integer idPublicacion,
            @Valid @RequestBody PublicacionRoomieRequest request
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        PublicacionRoomieResponse response = publicacionService.actualizarPublicacion(
                usuario.getIdUsuario(),
                idPublicacion,
                request
        );

        return ResponseEntity.ok(
                ApiResponse.success(response, "Publicación roomie actualizada correctamente")
        );
    }

    @PutMapping("/{idPublicacion}/pausar")
    public ResponseEntity<ApiResponse<PublicacionRoomieResponse>> pausarPublicacion(
            Authentication authentication,
            @PathVariable Integer idPublicacion
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        PublicacionRoomieResponse response = publicacionService.pausarPublicacion(
                usuario.getIdUsuario(),
                idPublicacion
        );

        return ResponseEntity.ok(
                ApiResponse.success(response, "Publicación pausada correctamente")
        );
    }

    @PutMapping("/{idPublicacion}/activar")
    public ResponseEntity<ApiResponse<PublicacionRoomieResponse>> activarPublicacion(
            Authentication authentication,
            @PathVariable Integer idPublicacion
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        PublicacionRoomieResponse response = publicacionService.activarPublicacion(
                usuario.getIdUsuario(),
                idPublicacion
        );

        return ResponseEntity.ok(
                ApiResponse.success(response, "Publicación activada correctamente")
        );
    }

    @PutMapping("/{idPublicacion}/cerrar")
    public ResponseEntity<ApiResponse<PublicacionRoomieResponse>> cerrarPublicacion(
            Authentication authentication,
            @PathVariable Integer idPublicacion
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        PublicacionRoomieResponse response = publicacionService.cerrarPublicacion(
                usuario.getIdUsuario(),
                idPublicacion
        );

        return ResponseEntity.ok(
                ApiResponse.success(response, "Publicación cerrada correctamente")
        );
    }

    @DeleteMapping("/{idPublicacion}")
    public ResponseEntity<ApiResponse<Void>> eliminarPublicacion(
            Authentication authentication,
            @PathVariable Integer idPublicacion
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        publicacionService.eliminarPublicacion(
                usuario.getIdUsuario(),
                idPublicacion
        );

        return ResponseEntity.ok(
                ApiResponse.success(null, "Publicación eliminada correctamente")
        );
    }
}