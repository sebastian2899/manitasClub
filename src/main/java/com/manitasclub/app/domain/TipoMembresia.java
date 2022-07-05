package com.manitasclub.app.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TipoMembresia.
 */
@Entity
@Table(name = "tipo_membresia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TipoMembresia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_membresia")
    private String nombreMembresia;

    @Column(name = "valor_membresia", precision = 21, scale = 2)
    private BigDecimal valorMembresia;

    @Column(name = "descripcion")
    private String descripcion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoMembresia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreMembresia() {
        return this.nombreMembresia;
    }

    public TipoMembresia nombreMembresia(String nombreMembresia) {
        this.setNombreMembresia(nombreMembresia);
        return this;
    }

    public void setNombreMembresia(String nombreMembresia) {
        this.nombreMembresia = nombreMembresia;
    }

    public BigDecimal getValorMembresia() {
        return this.valorMembresia;
    }

    public TipoMembresia valorMembresia(BigDecimal valorMembresia) {
        this.setValorMembresia(valorMembresia);
        return this;
    }

    public void setValorMembresia(BigDecimal valorMembresia) {
        this.valorMembresia = valorMembresia;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public TipoMembresia descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoMembresia)) {
            return false;
        }
        return id != null && id.equals(((TipoMembresia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoMembresia{" +
            "id=" + getId() +
            ", nombreMembresia='" + getNombreMembresia() + "'" +
            ", valorMembresia=" + getValorMembresia() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
