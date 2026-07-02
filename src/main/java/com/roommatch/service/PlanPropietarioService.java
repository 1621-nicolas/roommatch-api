package com.roommatch.service;

import com.roommatch.dto.MiPlanResponse;
import com.roommatch.dto.PlanPropietarioResponse;
import com.roommatch.model.*;
import com.roommatch.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlanPropietarioService {

    private final PlanPropietarioRepository planRepository;
    private final PropietarioRepository propietarioRepository;
    private final SuscripcionPropietarioRepository suscripcionRepository;
    private final NotificacionRepository notificacionRepository;

    public PlanPropietarioService(
            PlanPropietarioRepository planRepository,
            PropietarioRepository propietarioRepository,
            SuscripcionPropietarioRepository suscripcionRepository,
            NotificacionRepository notificacionRepository
    ) {
        this.planRepository = planRepository;
        this.propietarioRepository = propietarioRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.notificacionRepository = notificacionRepository;
    }

    public List<PlanPropietarioResponse> listarPlanesActivos() {
        return planRepository.findByEstadoOrderByPrecioMensualAsc("activo")
                .stream()
                .map(PlanPropietarioResponse::fromEntity)
                .toList();
    }

    public MiPlanResponse obtenerMiPlan(Integer idUsuario) {
        Propietario propietario = propietarioRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("No tienes perfil de propietario"));

        SuscripcionPropietario suscripcion = suscripcionRepository
                .findFirstByPropietarioIdPropietarioAndEstadoOrderByFechaInicioDesc(
                        propietario.getIdPropietario(),
                        "activo"
                )
                .orElseThrow(() -> new IllegalArgumentException("No tienes una suscripción activa"));

        return MiPlanResponse.fromEntity(suscripcion);
    }

    @Transactional
    public MiPlanResponse cambiarPlan(Integer idUsuario, Integer idPlanNuevo) {

        Propietario propietario = propietarioRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("No tienes perfil de propietario"));

        PlanPropietario nuevoPlan = planRepository.findById(idPlanNuevo)
                .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));

        if (!nuevoPlan.getEstado().equalsIgnoreCase("activo")) {
            throw new IllegalArgumentException("El plan seleccionado no está activo");
        }

        SuscripcionPropietario suscripcionActual = suscripcionRepository
                .findFirstByPropietarioIdPropietarioAndEstadoOrderByFechaInicioDesc(
                        propietario.getIdPropietario(),
                        "activo"
                )
                .orElseThrow(() -> new IllegalArgumentException("No tienes una suscripción activa"));

        suscripcionActual.setEstado("cancelado");
        suscripcionActual.setFechaFin(LocalDateTime.now());
        suscripcionRepository.save(suscripcionActual);

        SuscripcionPropietario nuevaSuscripcion = new SuscripcionPropietario();
        nuevaSuscripcion.setPropietario(propietario);
        nuevaSuscripcion.setPlan(nuevoPlan);
        nuevaSuscripcion.setEstado("activo");

        SuscripcionPropietario guardada = suscripcionRepository.save(nuevaSuscripcion);

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(propietario.getUsuario());
        notificacion.setTitulo("Plan actualizado");
        notificacion.setMensaje("Tu plan cambió correctamente a " + nuevoPlan.getNombrePlan() + ".");
        notificacion.setTipo("sistema");
        notificacion.setUrlDestino("/propietario/plan");

        notificacionRepository.save(notificacion);

        return MiPlanResponse.fromEntity(guardada);
    }
}