package com.manitasclub.app.domain;

import com.manitasclub.app.domain.enumeration.EstadoCaja;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Caja.
 */
@Entity
@Table(name = "caja")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Caja implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "valor_dia", precision = 21, scale = 2)
    private BigDecimal valorDia;

    @Column(name = "valor_registrado", precision = 21, scale = 2)
    private BigDecimal valorRegistrado;

    @Column(name = "diferencia", precision = 21, scale = 2)
    private BigDecimal diferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCaja estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Caja id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Caja fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getValorDia() {
        return this.valorDia;
    }

    public Caja valorDia(BigDecimal valorDia) {
        this.setValorDia(valorDia);
        return this;
    }

    public void setValorDia(BigDecimal valorDia) {
        this.valorDia = valorDia;
    }

    public BigDecimal getValorRegistrado() {
        return this.valorRegistrado;
    }

    public Caja valorRegistrado(BigDecimal valorRegistrado) {
        this.setValorRegistrado(valorRegistrado);
        return this;
    }

    public void setValorRegistrado(BigDecimal valorRegistrado) {
        this.valorRegistrado = valorRegistrado;
    }

    public BigDecimal getDiferencia() {
        return this.diferencia;
    }

    public Caja diferencia(BigDecimal diferencia) {
        this.setDiferencia(diferencia);
        return this;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    public EstadoCaja getEstado() {
        return this.estado;
    }

    public Caja estado(EstadoCaja estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoCaja estado) {
        this.estado = estado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Caja)) {
            return false;
        }
        return id != null && id.equals(((Caja) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Caja{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", valorDia=" + getValorDia() +
            ", valorRegistrado=" + getValorRegistrado() +
            ", diferencia=" + getDiferencia() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
