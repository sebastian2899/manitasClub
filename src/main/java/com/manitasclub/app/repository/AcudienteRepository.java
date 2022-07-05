package com.manitasclub.app.repository;

import com.manitasclub.app.domain.Acudiente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Acudiente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcudienteRepository extends JpaRepository<Acudiente, Long> {}
