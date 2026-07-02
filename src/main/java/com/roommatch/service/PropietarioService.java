package com.roommatch.service;

import com.roommatch.dto.PropietarioRequest;
import com.roommatch.dto.PropietarioResponse;
import com.roommatch.model.*;
import com.roommatch.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropietarioService {

    private final PropietarioRepository propietarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PlanPropietarioRepository planRepository;
    private final SuscripcionPropietarioRepository suscripcionRepository;
    private final NotificacionRepository notificacionRepository;

    public PropietarioService(
            PropietarioRepository propietarioRepository,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PlanPropietarioRepository planRepository,
            SuscripcionPropietarioRepository suscripcionRepository,
            NotificacionRepository notificacionRepository
    ) {
        this.propietarioRepository = propietarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.planRepository = planRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.notificacionRepository = notificacionRepository;
    }

    @Transactional
    public PropietarioResponse convertirmeEnPropietario(
            Integer idUsuario,
            PropietarioRequest request
    ) {
        if (propietarioRepository.existsByUsuarioIdUsuario(idUsuario)) {
            throw new IllegalArgumentException("Este usuario ya tiene perfil de propietario");
        }

        if (!request.getTipoPropietario().equalsIgnoreCase("persona")
                && !request.getTipoPropietario().equalsIgnoreCase("empresa")) {
            throw new IllegalArgumentException("El tipo de propietario debe ser persona o empresa");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Rol rolPropietario = rolRepository.findByNombreRol("PROPIETARIO")
                .orElseThrow(() -> new IllegalArgumentException("No existe el rol PROPIETARIO en la base de datos"));

        PlanPropietario planGratis = planRepository.findByNombrePlan("Gratis")
                .orElseThrow(() -> new IllegalArgumentException("No existe el plan Gratis en la base de datos"));

        usuario.setRol(rolPropietario);
        usuarioRepository.save(usuario);

        Propietario propietario = new Propietario();
        propietario.setUsuario(usuario);
        propietario.setTipoPropietario(request.getTipoPropietario().toLowerCase());
        propietario.setNombreComercial(request.getNombreComercial());
        propietario.setRuc(request.getRuc());
        propietario.setDescripcion(request.getDescripcion());
        propietario.setEstado("activo");
        propietario.setVerificado(false);

        Propietario propietarioGuardado = propietarioRepository.save(propietario);

        SuscripcionPropietario suscripcion = new SuscripcionPropietario();
        suscripcion.setPropietario(propietarioGuardado);
        suscripcion.setPlan(planGratis);
        suscripcion.setEstado("activo");

        SuscripcionPropietario suscripcionGuardada = suscripcionRepository.save(suscripcion);

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setTitulo("Perfil de propietario creado");
        notificacion.setMensaje("Ahora puedes publicar habitaciones en RoomMatch con el plan Gratis.");
        notificacion.setTipo("sistema");
        notificacion.setUrlDestino("/propietario");

        notificacionRepository.save(notificacion);

        return PropietarioResponse.fromEntity(propietarioGuardado, suscripcionGuardada);
    }

    public PropietarioResponse obtenerMiPerfilPropietario(Integer idUsuario) {
        Propietario propietario = propietarioRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Aún no tienes perfil de propietario"));

        SuscripcionPropietario suscripcion = suscripcionRepository
                .findFirstByPropietarioIdPropietarioAndEstadoOrderByFechaInicioDesc(
                        propietario.getIdPropietario(),
                        "activo"
                )
                .orElse(null);

        return PropietarioResponse.fromEntity(propietario, suscripcion);
    }
}