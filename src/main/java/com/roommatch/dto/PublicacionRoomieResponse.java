package com.roommatch.dto;

import com.roommatch.model.PublicacionRoomie;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PublicacionRoomieResponse {

    private Integer idPublicacion;

    private Integer idUsuario;
    private String nombreUsuario;
    private String emailUsuario;

    private String tipoPublicacion;
    private String titulo;
    private String descripcion;
    private String distrito;
    private BigDecimal presupuestoMin;
    private BigDecimal presupuestoMax;
    private String estado;

    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaActualizacion;

    public static PublicacionRoomieResponse fromEntity(PublicacionRoomie publicacion) {
        PublicacionRoomieResponse response = new PublicacionRoomieResponse();

        response.setIdPublicacion(publicacion.getIdPublicacion());
        response.setTipoPublicacion(publicacion.getTipoPublicacion());
        response.setTitulo(publicacion.getTitulo());
        response.setDescripcion(publicacion.getDescripcion());
        response.setDistrito(publicacion.getDistrito());
        response.setPresupuestoMin(publicacion.getPresupuestoMin());
        response.setPresupuestoMax(publicacion.getPresupuestoMax());
        response.setEstado(publicacion.getEstado());
        response.setFechaPublicacion(publicacion.getFechaPublicacion());
        response.setFechaActualizacion(publicacion.getFechaActualizacion());

        if (publicacion.getUsuario() != null) {
            response.setIdUsuario(publicacion.getUsuario().getIdUsuario());
            response.setNombreUsuario(
                    publicacion.getUsuario().getNombres() + " " +
                    publicacion.getUsuario().getApellidos()
            );
            response.setEmailUsuario(publicacion.getUsuario().getEmail());
        }

        return response;
    }

    public Integer getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Integer idPublicacion) {
        this.idPublicacion = idPublicacion;
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

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getTipoPublicacion() {
        return tipoPublicacion;
    }

    public void setTipoPublicacion(String tipoPublicacion) {
        this.tipoPublicacion = tipoPublicacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public BigDecimal getPresupuestoMin() {
        return presupuestoMin;
    }

    public void setPresupuestoMin(BigDecimal presupuestoMin) {
        this.presupuestoMin = presupuestoMin;
    }

    public BigDecimal getPresupuestoMax() {
        return presupuestoMax;
    }

    public void setPresupuestoMax(BigDecimal presupuestoMax) {
        this.presupuestoMax = presupuestoMax;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}