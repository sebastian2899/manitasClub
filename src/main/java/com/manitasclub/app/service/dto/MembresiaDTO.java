package com.manitasclub.app.service.dto;

import com.manitasclub.app.domain.enumeration.EstadoMembresia;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.manitasclub.app.domain.Membresia} entity.
 */
@Schema(description = "not an ignored comment")
public class MembresiaDTO implements Serializable {

    private Long id;

    private Instant fechaCreacion;

    private Instant fechaInicio;

    private Instant fechaFin;

    private Long cantidad;

    private EstadoMembresia estado;

    private String descripcion;

    private TipoMembresiaDTO tipo;

    private NinioDTO ninio;

    private BigDecimal precioMembresia;

    private Long idNinio;

    private Long idTipo;

    private BigDecimal deuda;

    private BigDecimal valorPagado;

    public BigDecimal getPrecioMembresia() {
        return precioMembresia;
    }

    public void setPrecioMembresia(BigDecimal precioMembresia) {
        this.precioMembresia = precioMembresia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public Long getIdNinio() {
        return idNinio;
    }

    public BigDecimal getDeuda() {
        return deuda;
    }

    public void setDeuda(BigDecimal deuda) {
        this.deuda = deuda;
    }

    public BigDecimal getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public void setIdNinio(Long idNinio) {
        this.idNinio = idNinio;
    }

    public Long getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Long idTipo) {
        this.idTipo = idTipo;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public EstadoMembresia getEstado() {
        return estado;
    }

    public void setEstado(EstadoMembresia estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoMembresiaDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoMembresiaDTO tipo) {
        this.tipo = tipo;
    }

    public NinioDTO getNinio() {
        return ninio;
    }

    public void setNinio(NinioDTO ninio) {
        this.ninio = ninio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembresiaDTO)) {
            return false;
        }

        MembresiaDTO membresiaDTO = (MembresiaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, membresiaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembresiaDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", cantidad=" + getCantidad() +
            ", estado='" + getEstado() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", tipo=" + getTipo() +
            ", ninio=" + getNinio() +
            "}";
    }
}
