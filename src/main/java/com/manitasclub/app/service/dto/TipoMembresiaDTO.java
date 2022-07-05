package com.manitasclub.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.manitasclub.app.domain.TipoMembresia} entity.
 */
public class TipoMembresiaDTO implements Serializable {

    private Long id;

    private String nombreMembresia;

    private BigDecimal valorMembresia;

    private String descripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreMembresia() {
        return nombreMembresia;
    }

    public void setNombreMembresia(String nombreMembresia) {
        this.nombreMembresia = nombreMembresia;
    }

    public BigDecimal getValorMembresia() {
        return valorMembresia;
    }

    public void setValorMembresia(BigDecimal valorMembresia) {
        this.valorMembresia = valorMembresia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoMembresiaDTO)) {
            return false;
        }

        TipoMembresiaDTO tipoMembresiaDTO = (TipoMembresiaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoMembresiaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoMembresiaDTO{" +
            "id=" + getId() +
            ", nombreMembresia='" + getNombreMembresia() + "'" +
            ", valorMembresia=" + getValorMembresia() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
