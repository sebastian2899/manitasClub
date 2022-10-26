package com.manitasclub.app.domain;

import com.manitasclub.app.service.dto.AcudienteDTO;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ninio.
 */
@Entity
@Table(name = "ninio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ninio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "doucumento_identidad")
    private String doucumentoIdentidad;

    @Column(name = "fecha_nacimiento")
    private Instant fechaNacimiento;

    @Column(name = "edad")
    private Long edad;

    @Column(name = "observacion")
    private Boolean observacion;

    @Column(name = "descripcion_observacion")
    private String descripcionObservacion;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "foto_content_type")
    private String fotoContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private Acudiente acudiente;

    @Column(name = "id_acudiente")
    private Long idAcudiente;

    @Transient
    private AcudienteDTO acudiente2;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ninio id(Long id) {
        this.setId(id);
        return this;
    }

    public AcudienteDTO getAcudiente2() {
        return acudiente2;
    }

    public void setAcudiente2(AcudienteDTO acudiente2) {
        this.acudiente2 = acudiente2;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return this.nombres;
    }

    public Ninio nombres(String nombres) {
        this.setNombres(nombres);
        return this;
    }

    public Long getIdAcudiente() {
        return idAcudiente;
    }

    public void setIdAcudiente(Long idAcudiente) {
        this.idAcudiente = idAcudiente;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Ninio apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDoucumentoIdentidad() {
        return this.doucumentoIdentidad;
    }

    public Ninio doucumentoIdentidad(String doucumentoIdentidad) {
        this.setDoucumentoIdentidad(doucumentoIdentidad);
        return this;
    }

    public void setDoucumentoIdentidad(String doucumentoIdentidad) {
        this.doucumentoIdentidad = doucumentoIdentidad;
    }

    public Instant getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Ninio fechaNacimiento(Instant fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(Instant fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getEdad() {
        return this.edad;
    }

    public Ninio edad(Long edad) {
        this.setEdad(edad);
        return this;
    }

    public void setEdad(Long edad) {
        this.edad = edad;
    }

    public Boolean getObservacion() {
        return this.observacion;
    }

    public Ninio observacion(Boolean observacion) {
        this.setObservacion(observacion);
        return this;
    }

    public void setObservacion(Boolean observacion) {
        this.observacion = observacion;
    }

    public String getDescripcionObservacion() {
        return this.descripcionObservacion;
    }

    public Ninio descripcionObservacion(String descripcionObservacion) {
        this.setDescripcionObservacion(descripcionObservacion);
        return this;
    }

    public void setDescripcionObservacion(String descripcionObservacion) {
        this.descripcionObservacion = descripcionObservacion;
    }

    public byte[] getFoto() {
        return this.foto;
    }

    public Ninio foto(byte[] foto) {
        this.setFoto(foto);
        return this;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return this.fotoContentType;
    }

    public Ninio fotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
        return this;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public Acudiente getAcudiente() {
        return this.acudiente;
    }

    public void setAcudiente(Acudiente acudiente) {
        this.acudiente = acudiente;
    }

    public Ninio acudiente(Acudiente acudiente) {
        this.setAcudiente(acudiente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ninio)) {
            return false;
        }
        return id != null && id.equals(((Ninio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ninio{" +
            "id=" + getId() +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", doucumentoIdentidad='" + getDoucumentoIdentidad() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", edad=" + getEdad() +
            ", observacion='" + getObservacion() + "'" +
            ", descripcionObservacion='" + getDescripcionObservacion() + "'" +
            ", foto='" + getFoto() + "'" +
            ", fotoContentType='" + getFotoContentType() + "'" +
            "}";
    }
}
