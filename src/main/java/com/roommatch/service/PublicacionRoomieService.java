package com.roommatch.service;

import com.roommatch.dto.PublicacionRoomieRequest;
import com.roommatch.dto.PublicacionRoomieResponse;
import com.roommatch.model.PublicacionRoomie;
import com.roommatch.model.Usuario;
import com.roommatch.repository.PublicacionRoomieRepository;
import com.roommatch.repository.UsuarioRepository;
import com.roommatch.util.ApiConstants;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class PublicacionRoomieService {

    private final PublicacionRoomieRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;

    public PublicacionRoomieService(
            PublicacionRoomieRepository publicacionRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.publicacionRepository = publicacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public PublicacionRoomieResponse crearPublicacion(
            Integer idUsuario,
            PublicacionRoomieRequest request
    ) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        validarTipoPublicacion(request.getTipoPublicacion());
        validarPresupuesto(request);

        PublicacionRoomie publicacion = new PublicacionRoomie();
        publicacion.setUsuario(usuario);
        copiarDatos(request, publicacion);
        publicacion.setEstado("activa");

        PublicacionRoomie guardada = publicacionRepository.save(publicacion);

        return PublicacionRoomieResponse.fromEntity(guardada);
    }

    public Page<PublicacionRoomieResponse> listarPublicaciones(
            String tipo,
            String distrito,
            BigDecimal presupuestoMin,
            BigDecimal presupuestoMax,
            Pageable pageable
    ) {
        if (tipo != null && !tipo.trim().isEmpty()) {
            validarTipoPublicacion(tipo);
        } else {
            tipo = null;
        }

        return publicacionRepository.buscarPublicaciones(
                tipo,
                distrito,
                presupuestoMin,
                presupuestoMax,
                pageable
        ).map(PublicacionRoomieResponse::fromEntity);
    }

    public PublicacionRoomieResponse obtenerPublicacion(Integer idPublicacion) {
        PublicacionRoomie publicacion = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));

        if (!publicacion.getEstado().equalsIgnoreCase("activa")) {
            throw new IllegalArgumentException("La publicación no está disponible");
        }

        return PublicacionRoomieResponse.fromEntity(publicacion);
    }

    public Page<PublicacionRoomieResponse> listarMisPublicaciones(
            Integer idUsuario,
            Pageable pageable
    ) {
        return publicacionRepository
                .findByUsuarioIdUsuarioOrderByFechaPublicacionDesc(idUsuario, pageable)
                .map(PublicacionRoomieResponse::fromEntity);
    }

    @Transactional
    public PublicacionRoomieResponse actualizarPublicacion(
            Integer idUsuario,
            Integer idPublicacion,
            PublicacionRoomieRequest request
    ) {
        validarTipoPublicacion(request.getTipoPublicacion());
        validarPresupuesto(request);

        PublicacionRoomie publicacion = publicacionRepository
                .findByIdPublicacionAndUsuarioIdUsuario(idPublicacion, idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada o no te pertenece"));

        copiarDatos(request, publicacion);

        PublicacionRoomie actualizada = publicacionRepository.save(publicacion);

        return PublicacionRoomieResponse.fromEntity(actualizada);
    }

    @Transactional
    public PublicacionRoomieResponse pausarPublicacion(
            Integer idUsuario,
            Integer idPublicacion
    ) {
        PublicacionRoomie publicacion = obtenerPublicacionPropia(idUsuario, idPublicacion);
publicacion.setEstado(ApiConstants.ESTADO_PAUSADA);
        return PublicacionRoomieResponse.fromEntity(publicacionRepository.save(publicacion));
    }

    @Transactional
    public PublicacionRoomieResponse activarPublicacion(
            Integer idUsuario,
            Integer idPublicacion
    ) {
        PublicacionRoomie publicacion = obtenerPublicacionPropia(idUsuario, idPublicacion);
publicacion.setEstado(ApiConstants.ESTADO_ACTIVA);
        return PublicacionRoomieResponse.fromEntity(publicacionRepository.save(publicacion));
    }

    @Transactional
    public PublicacionRoomieResponse cerrarPublicacion(
            Integer idUsuario,
            Integer idPublicacion
    ) {
        PublicacionRoomie publicacion = obtenerPublicacionPropia(idUsuario, idPublicacion);
publicacion.setEstado(ApiConstants.ESTADO_CERRADA);
        return PublicacionRoomieResponse.fromEntity(publicacionRepository.save(publicacion));
    }

    @Transactional
    public void eliminarPublicacion(
            Integer idUsuario,
            Integer idPublicacion
    ) {
        PublicacionRoomie publicacion = obtenerPublicacionPropia(idUsuario, idPublicacion);
publicacion.setEstado(ApiConstants.ESTADO_ELIMINADO);
        publicacionRepository.save(publicacion);
    }

    private PublicacionRoomie obtenerPublicacionPropia(Integer idUsuario, Integer idPublicacion) {
        return publicacionRepository
                .findByIdPublicacionAndUsuarioIdUsuario(idPublicacion, idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada o no te pertenece"));
    }

    private void copiarDatos(PublicacionRoomieRequest request, PublicacionRoomie publicacion) {
        publicacion.setTipoPublicacion(request.getTipoPublicacion().toLowerCase());
        publicacion.setTitulo(request.getTitulo());
        publicacion.setDescripcion(request.getDescripcion());
        publicacion.setDistrito(request.getDistrito());
        publicacion.setPresupuestoMin(request.getPresupuestoMin());
        publicacion.setPresupuestoMax(request.getPresupuestoMax());
    }

    private void validarTipoPublicacion(String tipo) {
    Set<String> tiposPermitidos = Set.of(
            ApiConstants.TIPO_BUSCO_ROOMIE,
            ApiConstants.TIPO_BUSCO_CUARTO,
            ApiConstants.TIPO_BUSCO_COMPARTIR
    );

    if (tipo == null || !tiposPermitidos.contains(tipo.toLowerCase())) {
        throw new IllegalArgumentException(
                "Tipo de publicación no válido. Usa: busco_roomie, busco_cuarto o busco_compartir"
        );
    }
}
    private void validarPresupuesto(PublicacionRoomieRequest request) {
        if (request.getPresupuestoMin() != null
                && request.getPresupuestoMax() != null
                && request.getPresupuestoMax().compareTo(request.getPresupuestoMin()) < 0) {
            throw new IllegalArgumentException("El presupuesto máximo no puede ser menor que el presupuesto mínimo");
        }
    }
}