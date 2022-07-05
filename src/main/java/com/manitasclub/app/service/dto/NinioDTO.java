package com.manitasclub.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.manitasclub.app.domain.Ninio} entity.
 */
public class NinioDTO implements Serializable {

    private Long id;

    private String nombres;

    private String apellidos;

    private String doucumentoIdentidad;

    private Instant fechaNacimiento;

    private Long edad;

    private Boolean observacion;

    private String descripcionObservacion;

    @Lob
    private byte[] foto;

    private String fotoContentType;
    private AcudienteDTO acudiente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDoucumentoIdentidad() {
        return doucumentoIdentidad;
    }

    public void setDoucumentoIdentidad(String doucumentoIdentidad) {
        this.doucumentoIdentidad = doucumentoIdentidad;
    }

    public Instant getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Instant fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getEdad() {
        return edad;
    }

    public void setEdad(Long edad) {
        this.edad = edad;
    }

    public Boolean getObservacion() {
        return observacion;
    }

    public void setObservacion(Boolean observacion) {
        this.observacion = observacion;
    }

    public String getDescripcionObservacion() {
        return descripcionObservacion;
    }

    public void setDescripcionObservacion(String descripcionObservacion) {
        this.descripcionObservacion = descripcionObservacion;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return fotoContentType;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public AcudienteDTO getAcudiente() {
        return acudiente;
    }

    public void setAcudiente(AcudienteDTO acudiente) {
        this.acudiente = acudiente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NinioDTO)) {
            return false;
        }

        NinioDTO ninioDTO = (NinioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ninioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NinioDTO{" +
            "id=" + getId() +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", doucumentoIdentidad='" + getDoucumentoIdentidad() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", edad=" + getEdad() +
            ", observacion='" + getObservacion() + "'" +
            ", descripcionObservacion='" + getDescripcionObservacion() + "'" +
            ", foto='" + getFoto() + "'" +
            ", acudiente=" + getAcudiente() +
            "}";
    }
}
