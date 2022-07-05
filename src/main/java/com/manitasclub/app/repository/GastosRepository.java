package com.manitasclub.app.repository;

import com.manitasclub.app.domain.Gastos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Gastos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GastosRepository extends JpaRepository<Gastos, Long> {}
