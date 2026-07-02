package com.roommatch.dto;

import com.roommatch.model.Propietario;
import com.roommatch.model.SuscripcionPropietario;

import java.time.LocalDateTime;

public class PropietarioResponse {

    private Integer idPropietario;
    private Integer idUsuario;
    private String nombreUsuario;
    private String email;
    private String tipoPropietario;
    private String nombreComercial;
    private String ruc;
    private String descripcion;
    private Boolean verificado;
    private String estado;
    private LocalDateTime fechaRegistro;
    private String planActual;

    public static PropietarioResponse fromEntity(
            Propietario propietario,
            SuscripcionPropietario suscripcion
    ) {
        PropietarioResponse response = new PropietarioResponse();

        response.setIdPropietario(propietario.getIdPropietario());
        response.setTipoPropietario(propietario.getTipoPropietario());
        response.setNombreComercial(propietario.getNombreComercial());
        response.setRuc(propietario.getRuc());
        response.setDescripcion(propietario.getDescripcion());
        response.setVerificado(propietario.getVerificado());
        response.setEstado(propietario.getEstado());
        response.setFechaRegistro(propietario.getFechaRegistro());

        if (propietario.getUsuario() != null) {
            response.setIdUsuario(propietario.getUsuario().getIdUsuario());
            response.setNombreUsuario(
                    propietario.getUsuario().getNombres() + " " +
                    propietario.getUsuario().getApellidos()
            );
            response.setEmail(propietario.getUsuario().getEmail());
        }

        if (suscripcion != null && suscripcion.getPlan() != null) {
            response.setPlanActual(suscripcion.getPlan().getNombrePlan());
        }

        return response;
    }

    public Integer getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(Integer idPropietario) {
        this.idPropietario = idPropietario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
  
    public String getTipoPropietario() {
        return tipoPropietario;
    }

    public void setTipoPropietario(String tipoPropietario) {
        this.tipoPropietario = tipoPropietario;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getPlanActual() {
        return planActual;
    }

    public void setPlanActual(String planActual) {
        this.planActual = planActual;
    }
}