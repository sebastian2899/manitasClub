package com.manitasclub.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.manitasclub.app.domain.RegistroDiario} entity.
 */
public class RegistroDiarioDTO implements Serializable {

    private Long id;

    private String nombreNinio;

    private String nombreAcudiente;

    private String telefonoAcudiente;

    private BigDecimal valor;

    private Instant fechaIngreso;

    private String horaEntrada;

    private String horaSalida;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreNinio() {
        return nombreNinio;
    }

    public void setNombreNinio(String nombreNinio) {
        this.nombreNinio = nombreNinio;
    }

    public String getNombreAcudiente() {
        return nombreAcudiente;
    }

    public void setNombreAcudiente(String nombreAcudiente) {
        this.nombreAcudiente = nombreAcudiente;
    }

    public String getTelefonoAcudiente() {
        return telefonoAcudiente;
    }

    public void setTelefonoAcudiente(String telefonoAcudiente) {
        this.telefonoAcudiente = telefonoAcudiente;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Instant getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Instant fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistroDiarioDTO)) {
            return false;
        }

        RegistroDiarioDTO registroDiarioDTO = (RegistroDiarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, registroDiarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroDiarioDTO{" +
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
