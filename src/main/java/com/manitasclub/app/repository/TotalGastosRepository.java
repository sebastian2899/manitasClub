package com.manitasclub.app.repository;

import com.manitasclub.app.domain.TotalGastos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TotalGastos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TotalGastosRepository extends JpaRepository<TotalGastos, Long> {}
