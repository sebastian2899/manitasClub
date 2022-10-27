package com.manitasclub.app.repository;

import com.manitasclub.app.domain.Abono;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Abono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbonoRepository extends JpaRepository<Abono, Long> {}
