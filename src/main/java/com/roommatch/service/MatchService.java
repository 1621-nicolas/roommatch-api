package com.roommatch.service;

import com.roommatch.dto.MatchResponse;
import com.roommatch.model.MatchResultado;
import com.roommatch.model.PerfilConvivencia;
import com.roommatch.model.Usuario;
import com.roommatch.repository.MatchResultadoRepository;
import com.roommatch.repository.PerfilConvivenciaRepository;
import com.roommatch.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {

    private final MatchResultadoRepository matchRepository;
    private final PerfilConvivenciaRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public MatchService(
            MatchResultadoRepository matchRepository,
            PerfilConvivenciaRepository perfilRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.matchRepository = matchRepository;
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public List<MatchResponse> calcularMatches(Integer idUsuarioOrigen) {

        Usuario usuarioOrigen = usuarioRepository.findById(idUsuarioOrigen)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        PerfilConvivencia perfilOrigen = perfilRepository.findByUsuarioIdUsuario(idUsuarioOrigen)
                .orElseThrow(() -> new IllegalArgumentException("Debes completar tu perfil de convivencia antes de calcular matches"));

        List<PerfilConvivencia> perfiles = perfilRepository.findAll();

        List<MatchResponse> resultados = new ArrayList<>();

        for (PerfilConvivencia perfilDestino : perfiles) {

            Integer idUsuarioDestino = perfilDestino.getUsuario().getIdUsuario();

            if (idUsuarioDestino.equals(idUsuarioOrigen)) {
                continue;
            }

            if (!perfilDestino.getUsuario().getEstado().equalsIgnoreCase("activo")) {
                continue;
            }

            ResultadoCompatibilidad resultado = calcularCompatibilidad(perfilOrigen, perfilDestino);

            MatchResultado match = matchRepository
                    .findByUsuarioOrigenIdUsuarioAndUsuarioDestinoIdUsuario(idUsuarioOrigen, idUsuarioDestino)
                    .orElse(new MatchResultado());

            match.setUsuarioOrigen(usuarioOrigen);
            match.setUsuarioDestino(perfilDestino.getUsuario());
            match.setPorcentaje(resultado.porcentaje());
            match.setCoincidencias(resultado.coincidencias());
            match.setDiferencias(resultado.diferencias());

            MatchResultado matchGuardado = matchRepository.save(match);

            resultados.add(MatchResponse.fromEntity(matchGuardado));
        }

        resultados.sort((a, b) -> b.getPorcentaje().compareTo(a.getPorcentaje()));

        return resultados;
    }

    public Page<MatchResponse> listarMisMatches(
            Integer idUsuario,
            BigDecimal porcentajeMinimo,
            Pageable pageable
    ) {
        Page<MatchResultado> matches = matchRepository.listarMatchesPorUsuario(
                idUsuario,
                porcentajeMinimo,
                pageable
        );

        return matches.map(MatchResponse::fromEntity);
    }

    private ResultadoCompatibilidad calcularCompatibilidad(
            PerfilConvivencia origen,
            PerfilConvivencia destino
    ) {
        int puntos = 0;
        int total = 10;

        List<String> coincidencias = new ArrayList<>();
        List<String> diferencias = new ArrayList<>();

        if (origen.getDistritoPreferido().equalsIgnoreCase(destino.getDistritoPreferido())) {
            puntos++;
            coincidencias.add("Distrito preferido");
        } else {
            diferencias.add("Distrito diferente");
        }

        if (presupuestosCompatibles(origen, destino)) {
            puntos++;
            coincidencias.add("Presupuesto compatible");
        } else {
            diferencias.add("Presupuesto diferente");
        }

        if (diferenciaNumericaAceptable(origen.getLimpieza(), destino.getLimpieza())) {
            puntos++;
            coincidencias.add("Nivel de limpieza");
        } else {
            diferencias.add("Nivel de limpieza diferente");
        }

        if (diferenciaNumericaAceptable(origen.getRuido(), destino.getRuido())) {
            puntos++;
            coincidencias.add("Tolerancia al ruido");
        } else {
            diferencias.add("Tolerancia al ruido diferente");
        }

        if (diferenciaNumericaAceptable(origen.getSociabilidad(), destino.getSociabilidad())) {
            puntos++;
            coincidencias.add("Sociabilidad");
        } else {
            diferencias.add("Sociabilidad diferente");
        }

        if (textoIgual(origen.getHorario(), destino.getHorario())) {
            puntos++;
            coincidencias.add("Horario");
        } else {
            diferencias.add("Horario diferente");
        }

        if (textoIgual(origen.getVisitas(), destino.getVisitas())) {
            puntos++;
            coincidencias.add("Visitas");
        } else {
            diferencias.add("Preferencia de visitas diferente");
        }

        if (textoIgual(origen.getMascotas(), destino.getMascotas())) {
            puntos++;
            coincidencias.add("Mascotas");
        } else {
            diferencias.add("Preferencia sobre mascotas diferente");
        }

        if (textoIgual(origen.getFumar(), destino.getFumar())) {
            puntos++;
            coincidencias.add("Fumar");
        } else {
            diferencias.add("Preferencia sobre fumar diferente");
        }

        if (textoIgual(origen.getConvivencia(), destino.getConvivencia())) {
            puntos++;
            coincidencias.add("Tipo de convivencia");
        } else {
            diferencias.add("Tipo de convivencia diferente");
        }

        BigDecimal porcentaje = BigDecimal.valueOf((puntos * 100.0) / total)
                .setScale(2, RoundingMode.HALF_UP);

        return new ResultadoCompatibilidad(
                porcentaje,
                String.join(", ", coincidencias),
                String.join(", ", diferencias)
        );
    }

    private boolean presupuestosCompatibles(
            PerfilConvivencia origen,
            PerfilConvivencia destino
    ) {
        return origen.getPresupuestoMin().compareTo(destino.getPresupuestoMax()) <= 0
                && origen.getPresupuestoMax().compareTo(destino.getPresupuestoMin()) >= 0;
    }

    private boolean diferenciaNumericaAceptable(Integer valor1, Integer valor2) {
        return Math.abs(valor1 - valor2) <= 1;
    }

    private boolean textoIgual(String valor1, String valor2) {
        if (valor1 == null || valor2 == null) {
            return false;
        }

        return valor1.equalsIgnoreCase(valor2);
    }

    private record ResultadoCompatibilidad(
            BigDecimal porcentaje,
            String coincidencias,
            String diferencias
    ) {
    }
}