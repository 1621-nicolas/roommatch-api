package com.roommatch.service;

import com.roommatch.dto.ImagenHabitacionRequest;
import com.roommatch.dto.ImagenHabitacionResponse;
import com.roommatch.model.Habitacion;
import com.roommatch.model.ImagenHabitacion;
import com.roommatch.model.Propietario;
import com.roommatch.repository.HabitacionRepository;
import com.roommatch.repository.ImagenHabitacionRepository;
import com.roommatch.repository.PropietarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImagenHabitacionService {

    private final ImagenHabitacionRepository imagenRepository;
    private final HabitacionRepository habitacionRepository;
    private final PropietarioRepository propietarioRepository;

    public ImagenHabitacionService(
            ImagenHabitacionRepository imagenRepository,
            HabitacionRepository habitacionRepository,
            PropietarioRepository propietarioRepository
    ) {
        this.imagenRepository = imagenRepository;
        this.habitacionRepository = habitacionRepository;
        this.propietarioRepository = propietarioRepository;
    }

    @Transactional
    public ImagenHabitacionResponse agregarImagen(
            Integer idUsuario,
            Integer idHabitacion,
            ImagenHabitacionRequest request
    ) {
        Propietario propietario = propietarioRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("No tienes perfil de propietario"));

        Habitacion habitacion = habitacionRepository
                .findByIdHabitacionAndPropietarioIdPropietario(
                        idHabitacion,
                        propietario.getIdPropietario()
                )
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada o no te pertenece"));

        long cantidadImagenes = imagenRepository.countByHabitacionIdHabitacion(idHabitacion);

        if (cantidadImagenes >= 5) {
            throw new IllegalArgumentException("Solo puedes registrar hasta 5 imágenes por habitación");
        }

        boolean esPrincipal = Boolean.TRUE.equals(request.getPrincipal());

        if (esPrincipal) {
            imagenRepository.desmarcarImagenesPrincipales(idHabitacion);
        }

        ImagenHabitacion imagen = new ImagenHabitacion();
        imagen.setHabitacion(habitacion);
        imagen.setUrlImagen(request.getUrlImagen());
        imagen.setOrden(request.getOrden() != null ? request.getOrden() : (int) cantidadImagenes + 1);
        imagen.setPrincipal(esPrincipal || cantidadImagenes == 0);

        ImagenHabitacion imagenGuardada = imagenRepository.save(imagen);

        return ImagenHabitacionResponse.fromEntity(imagenGuardada);
    }

    public List<ImagenHabitacionResponse> listarImagenesPorHabitacion(Integer idHabitacion) {
        return imagenRepository
                .findByHabitacionIdHabitacionOrderByOrdenAsc(idHabitacion)
                .stream()
                .map(ImagenHabitacionResponse::fromEntity)
                .toList();
    }

    @Transactional
    public ImagenHabitacionResponse marcarComoPrincipal(
            Integer idUsuario,
            Integer idImagen
    ) {
        Propietario propietario = propietarioRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("No tienes perfil de propietario"));

        ImagenHabitacion imagen = imagenRepository
                .findByIdImagenAndHabitacionPropietarioIdPropietario(
                        idImagen,
                        propietario.getIdPropietario()
                )
                .orElseThrow(() -> new IllegalArgumentException("Imagen no encontrada o no te pertenece"));

        Integer idHabitacion = imagen.getHabitacion().getIdHabitacion();

        imagenRepository.desmarcarImagenesPrincipales(idHabitacion);

        imagen.setPrincipal(true);

        ImagenHabitacion imagenActualizada = imagenRepository.save(imagen);

        return ImagenHabitacionResponse.fromEntity(imagenActualizada);
    }

    @Transactional
    public void eliminarImagen(
            Integer idUsuario,
            Integer idImagen
    ) {
        Propietario propietario = propietarioRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("No tienes perfil de propietario"));

        ImagenHabitacion imagen = imagenRepository
                .findByIdImagenAndHabitacionPropietarioIdPropietario(
                        idImagen,
                        propietario.getIdPropietario()
                )
                .orElseThrow(() -> new IllegalArgumentException("Imagen no encontrada o no te pertenece"));

        imagenRepository.delete(imagen);
    }
}