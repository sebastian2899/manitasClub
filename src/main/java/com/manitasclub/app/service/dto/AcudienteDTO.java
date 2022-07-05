package com.manitasclub.app.service.dto;

import com.manitasclub.app.domain.enumeration.TipoIdentificacion;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.manitasclub.app.domain.Acudiente} entity.
 */
public class AcudienteDTO implements Serializable {

    private Long id;

    private String nombre;

    private String apellido;

    private TipoIdentificacion tipoIdentifiacacion;

    private String direccion;

    private String telefono;

    private String email;

    private String parentesco;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public TipoIdentificacion getTipoIdentifiacacion() {
        return tipoIdentifiacacion;
    }

    public void setTipoIdentifiacacion(TipoIdentificacion tipoIdentifiacacion) {
        this.tipoIdentifiacacion = tipoIdentifiacacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AcudienteDTO)) {
            return false;
        }

        AcudienteDTO acudienteDTO = (AcudienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, acudienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcudienteDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", tipoIdentifiacacion='" + getTipoIdentifiacacion() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", email='" + getEmail() + "'" +
            ", parentesco='" + getParentesco() + "'" +
            "}";
    }
}
