package com.manitasclub.app.domain;

import com.manitasclub.app.domain.enumeration.TipoIdentificacion;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Acudiente.
 */
@Entity
@Table(name = "acudiente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Acudiente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_identifiacacion")
    private TipoIdentificacion tipoIdentifiacacion;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "parentesco")
    private String parentesco;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Acudiente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Acudiente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Acudiente apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public TipoIdentificacion getTipoIdentifiacacion() {
        return this.tipoIdentifiacacion;
    }

    public Acudiente tipoIdentifiacacion(TipoIdentificacion tipoIdentifiacacion) {
        this.setTipoIdentifiacacion(tipoIdentifiacacion);
        return this;
    }

    public void setTipoIdentifiacacion(TipoIdentificacion tipoIdentifiacacion) {
        this.tipoIdentifiacacion = tipoIdentifiacacion;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Acudiente direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Acudiente telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public Acudiente email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentesco() {
        return this.parentesco;
    }

    public Acudiente parentesco(String parentesco) {
        this.setParentesco(parentesco);
        return this;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Acudiente)) {
            return false;
        }
        return id != null && id.equals(((Acudiente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Acudiente{" +
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
