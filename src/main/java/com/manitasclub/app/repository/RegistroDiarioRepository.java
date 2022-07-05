package com.manitasclub.app.repository;

import com.manitasclub.app.domain.RegistroDiario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RegistroDiario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistroDiarioRepository extends JpaRepository<RegistroDiario, Long> {}
