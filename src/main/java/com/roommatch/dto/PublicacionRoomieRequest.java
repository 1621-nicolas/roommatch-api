package com.roommatch.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class PublicacionRoomieRequest {

    @NotBlank(message = "El tipo de publicación es obligatorio")
    private String tipoPublicacion;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede superar 150 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 800, message = "La descripción no puede superar 800 caracteres")
    private String descripcion;

    @NotBlank(message = "El distrito es obligatorio")
    @Size(max = 100, message = "El distrito no puede superar 100 caracteres")
    private String distrito;

    @DecimalMin(value = "0.0", message = "El presupuesto mínimo no puede ser negativo")
    private BigDecimal presupuestoMin;

    @DecimalMin(value = "0.0", message = "El presupuesto máximo no puede ser negativo")
    private BigDecimal presupuestoMax;

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
}