package com.manitasclub.app.service.dto;

import com.manitasclub.app.domain.enumeration.EstadoCaja;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.manitasclub.app.domain.Caja} entity.
 */
public class CajaDTO implements Serializable {

    private Long id;

    private Instant fechaCreacion;

    private BigDecimal valorDia;

    private BigDecimal valorRegistrado;

    private BigDecimal diferencia;

    private EstadoCaja estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getValorDia() {
        return valorDia;
    }

    public void setValorDia(BigDecimal valorDia) {
        this.valorDia = valorDia;
    }

    public BigDecimal getValorRegistrado() {
        return valorRegistrado;
    }

    public void setValorRegistrado(BigDecimal valorRegistrado) {
        this.valorRegistrado = valorRegistrado;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    public EstadoCaja getEstado() {
        return estado;
    }

    public void setEstado(EstadoCaja estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CajaDTO)) {
            return false;
        }

        CajaDTO cajaDTO = (CajaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cajaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CajaDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", valorDia=" + getValorDia() +
            ", valorRegistrado=" + getValorRegistrado() +
            ", diferencia=" + getDiferencia() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
