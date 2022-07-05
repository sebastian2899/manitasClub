package com.manitasclub.app.repository;

import com.manitasclub.app.domain.TipoMembresia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoMembresia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoMembresiaRepository extends JpaRepository<TipoMembresia, Long> {}
