package com.manitasclub.app.service.dto;

import com.manitasclub.app.domain.enumeration.EstadoCaja;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.manitasclub.app.domain.TotalGastos} entity.
 */
public class TotalGastosDTO implements Serializable {

    private Long id;

    private Instant fechaCreacion;

    private BigDecimal valorInicial;

    private BigDecimal valorTotalGastos;

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

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    public BigDecimal getValorTotalGastos() {
        return valorTotalGastos;
    }

    public void setValorTotalGastos(BigDecimal valorTotalGastos) {
        this.valorTotalGastos = valorTotalGastos;
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
        if (!(o instanceof TotalGastosDTO)) {
            return false;
        }

        TotalGastosDTO totalGastosDTO = (TotalGastosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, totalGastosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TotalGastosDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", valorInicial=" + getValorInicial() +
            ", valorTotalGastos=" + getValorTotalGastos() +
            ", diferencia=" + getDiferencia() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
