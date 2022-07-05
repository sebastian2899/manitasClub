package com.manitasclub.app.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RegistroDiario.
 */
@Entity
@Table(name = "registro_diario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RegistroDiario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_ninio")
    private String nombreNinio;

    @Column(name = "nombre_acudiente")
    private String nombreAcudiente;

    @Column(name = "telefono_acudiente")
    private String telefonoAcudiente;

    @Column(name = "valor", precision = 21, scale = 2)
    private BigDecimal valor;

    @Column(name = "fecha_ingreso")
    private Instant fechaIngreso;

    @Column(name = "hora_entrada")
    private String horaEntrada;

    @Column(name = "hora_salida")
    private String horaSalida;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RegistroDiario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreNinio() {
        return this.nombreNinio;
    }

    public RegistroDiario nombreNinio(String nombreNinio) {
        this.setNombreNinio(nombreNinio);
        return this;
    }

    public void setNombreNinio(String nombreNinio) {
        this.nombreNinio = nombreNinio;
    }

    public String getNombreAcudiente() {
        return this.nombreAcudiente;
    }

    public RegistroDiario nombreAcudiente(String nombreAcudiente) {
        this.setNombreAcudiente(nombreAcudiente);
        return this;
    }

    public void setNombreAcudiente(String nombreAcudiente) {
        this.nombreAcudiente = nombreAcudiente;
    }

    public String getTelefonoAcudiente() {
        return this.telefonoAcudiente;
    }

    public RegistroDiario telefonoAcudiente(String telefonoAcudiente) {
        this.setTelefonoAcudiente(telefonoAcudiente);
        return this;
    }

    public void setTelefonoAcudiente(String telefonoAcudiente) {
        this.telefonoAcudiente = telefonoAcudiente;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public RegistroDiario valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Instant getFechaIngreso() {
        return this.fechaIngreso;
    }

    public RegistroDiario fechaIngreso(Instant fechaIngreso) {
        this.setFechaIngreso(fechaIngreso);
        return this;
    }

    public void setFechaIngreso(Instant fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getHoraEntrada() {
        return this.horaEntrada;
    }

    public RegistroDiario horaEntrada(String horaEntrada) {
        this.setHoraEntrada(horaEntrada);
        return this;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return this.horaSalida;
    }

    public RegistroDiario horaSalida(String horaSalida) {
        this.setHoraSalida(horaSalida);
        return this;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistroDiario)) {
            return false;
        }
        return id != null && id.equals(((RegistroDiario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroDiario{" +
            "id=" + getId() +
            ", nombreNinio='" + getNombreNinio() + "'" +
            ", nombreAcudiente='" + getNombreAcudiente() + "'" +
            ", telefonoAcudiente='" + getTelefonoAcudiente() + "'" +
            ", valor=" + getValor() +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            ", horaEntrada='" + getHoraEntrada() + "'" +
            ", horaSalida='" + getHoraSalida() + "'" +
            "}";
    }
}
