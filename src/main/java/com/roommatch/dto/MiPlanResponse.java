package com.roommatch.dto;

import com.roommatch.model.SuscripcionPropietario;

import java.time.LocalDateTime;

public class MiPlanResponse {

    private Integer idSuscripcion;
    private Integer idPropietario;
    private String propietario;
    private Integer idPlan;
    private String nombrePlan;
    private Integer limiteHabitaciones;
    private Boolean permiteDestacar;
    private Boolean permiteEstadisticas;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;

    public static MiPlanResponse fromEntity(SuscripcionPropietario suscripcion) {
        MiPlanResponse response = new MiPlanResponse();

        response.setIdSuscripcion(suscripcion.getIdSuscripcion());
        response.setFechaInicio(suscripcion.getFechaInicio());
        response.setFechaFin(suscripcion.getFechaFin());
        response.setEstado(suscripcion.getEstado());

        if (suscripcion.getPropietario() != null) {
            response.setIdPropietario(suscripcion.getPropietario().getIdPropietario());
            response.setPropietario(suscripcion.getPropietario().getNombreComercial());
        }

        if (suscripcion.getPlan() != null) {
            response.setIdPlan(suscripcion.getPlan().getIdPlan());
            response.setNombrePlan(suscripcion.getPlan().getNombrePlan());
            response.setLimiteHabitaciones(suscripcion.getPlan().getLimiteHabitaciones());
            response.setPermiteDestacar(suscripcion.getPlan().getPermiteDestacar());
            response.setPermiteEstadisticas(suscripcion.getPlan().getPermiteEstadisticas());
        }

        return response;
    }

    public Integer getIdSuscripcion() {
        return idSuscripcion;
    }

    public void setIdSuscripcion(Integer idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }

    public Integer getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(Integer idPropietario) {
        this.idPropietario = idPropietario;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public Integer getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Integer idPlan) {
        this.idPlan = idPlan;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public Integer getLimiteHabitaciones() {
        return limiteHabitaciones;
    }

    public void setLimiteHabitaciones(Integer limiteHabitaciones) {
        this.limiteHabitaciones = limiteHabitaciones;
    }

    public Boolean getPermiteDestacar() {
        return permiteDestacar;
    }

    public void setPermiteDestacar(Boolean permiteDestacar) {
        this.permiteDestacar = permiteDestacar;
    }

    public Boolean getPermiteEstadisticas() {
        return permiteEstadisticas;
    }

    public void setPermiteEstadisticas(Boolean permiteEstadisticas) {
        this.permiteEstadisticas = permiteEstadisticas;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}