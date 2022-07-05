package com.manitasclub.app.domain;

import com.manitasclub.app.domain.enumeration.EstadoCaja;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TotalGastos.
 */
@Entity
@Table(name = "total_gastos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TotalGastos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "valor_inicial", precision = 21, scale = 2)
    private BigDecimal valorInicial;

    @Column(name = "valor_total_gastos", precision = 21, scale = 2)
    private BigDecimal valorTotalGastos;

    @Column(name = "diferencia", precision = 21, scale = 2)
    private BigDecimal diferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCaja estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TotalGastos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public TotalGastos fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getValorInicial() {
        return this.valorInicial;
    }

    public TotalGastos valorInicial(BigDecimal valorInicial) {
        this.setValorInicial(valorInicial);
        return this;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    public BigDecimal getValorTotalGastos() {
        return this.valorTotalGastos;
    }

    public TotalGastos valorTotalGastos(BigDecimal valorTotalGastos) {
        this.setValorTotalGastos(valorTotalGastos);
        return this;
    }

    public void setValorTotalGastos(BigDecimal valorTotalGastos) {
        this.valorTotalGastos = valorTotalGastos;
    }

    public BigDecimal getDiferencia() {
        return this.diferencia;
    }

    public TotalGastos diferencia(BigDecimal diferencia) {
        this.setDiferencia(diferencia);
        return this;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    public EstadoCaja getEstado() {
        return this.estado;
    }

    public TotalGastos estado(EstadoCaja estado) {
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
        if (!(o instanceof TotalGastos)) {
            return false;
        }
        return id != null && id.equals(((TotalGastos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TotalGastos{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", valorInicial=" + getValorInicial() +
            ", valorTotalGastos=" + getValorTotalGastos() +
            ", diferencia=" + getDiferencia() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
