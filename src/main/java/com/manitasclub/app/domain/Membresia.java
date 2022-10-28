package com.manitasclub.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manitasclub.app.domain.enumeration.EstadoMembresia;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Entity
@Table(name = "membresia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Membresia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "fecha_inicio")
    private Instant fechaInicio;

    @Column(name = "fecha_fin")
    private Instant fechaFin;

    @Column(name = "cantidad")
    private Long cantidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoMembresia estado;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "id_membresia")
    public BigDecimal getPrecioMembresia() {
        return precioMembresia;
    }

    @Column(name = "deuda")
    public BigDecimal deuda;

    @Column(name = "valor_pagado")
    public BigDecimal valorPagado;

    public void setPrecioMembresia(BigDecimal precioMembresia) {
        this.precioMembresia = precioMembresia;
    }

    @OneToOne
    private TipoMembresia tipo;

    @JsonIgnoreProperties(value = { "acudiente" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Ninio ninio;

    @Column(name = "id_ninio")
    private Long idNinio;

    @Column(name = "id_tipo_membresia")
    private Long idTipo;

    @Column(name = "precio_membresia")
    private BigDecimal precioMembresia;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Membresia id(Long id) {
        this.setId(id);
        return this;
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

    public Long getIdNinio() {
        return idNinio;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Membresia fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public Membresia fechaInicio(Instant fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaFin() {
        return this.fechaFin;
    }

    public Membresia fechaFin(Instant fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getCantidad() {
        return this.cantidad;
    }

    public Membresia cantidad(Long cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public EstadoMembresia getEstado() {
        return this.estado;
    }

    public Membresia estado(EstadoMembresia estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoMembresia estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Membresia descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoMembresia getTipo() {
        return this.tipo;
    }

    public void setTipo(TipoMembresia tipoMembresia) {
        this.tipo = tipoMembresia;
    }

    public Membresia tipo(TipoMembresia tipoMembresia) {
        this.setTipo(tipoMembresia);
        return this;
    }

    public Ninio getNinio() {
        return this.ninio;
    }

    public void setNinio(Ninio ninio) {
        this.ninio = ninio;
    }

    public Membresia ninio(Ninio ninio) {
        this.setNinio(ninio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Membresia)) {
            return false;
        }
        return id != null && id.equals(((Membresia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Membresia{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", cantidad=" + getCantidad() +
            ", estado='" + getEstado() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
