package com.roommatch.repository;

import com.roommatch.model.MatchResultado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface MatchResultadoRepository extends JpaRepository<MatchResultado, Integer> {

    Optional<MatchResultado> findByUsuarioOrigenIdUsuarioAndUsuarioDestinoIdUsuario(
            Integer idUsuarioOrigen,
            Integer idUsuarioDestino
    );

    @Query("""
            SELECT m
            FROM MatchResultado m
            WHERE m.usuarioOrigen.idUsuario = :idUsuario
            AND m.porcentaje >= :porcentajeMinimo
            ORDER BY m.porcentaje DESC
            """)
    Page<MatchResultado> listarMatchesPorUsuario(
            @Param("idUsuario") Integer idUsuario,
            @Param("porcentajeMinimo") BigDecimal porcentajeMinimo,
            Pageable pageable
    );
}