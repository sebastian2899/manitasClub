package com.manitasclub.app.repository;

import com.manitasclub.app.domain.Ninio;
import java.time.Instant;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;

public class NinioSpecification {

    public static Specification<Ninio> nombreContains(String nombre) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("nombres")), "%" + nombre.toUpperCase() + "%");
    }

    public static Specification<Ninio> apellidosContains(String apellidos) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("apellidos")), "%" + apellidos.toUpperCase() + "%");
    }

    public static Specification<Ninio> edad(Long edad) {
        return (root, query, cb) -> cb.equal(root.get("edad"), +edad);
    }

    public static Specification<Ninio> fechaNacimiento(Instant fechaNacimiento) {
        return (root, query, cb) -> cb.like(root.get("fechaNacimiento"), "%" + fechaNacimiento + "%");
    }

    public static Specification<Ninio> observacion(Boolean discapacidad) {
        return (root, query, cb) -> cb.equal(root.get("observacion"), discapacidad);
    }
}
