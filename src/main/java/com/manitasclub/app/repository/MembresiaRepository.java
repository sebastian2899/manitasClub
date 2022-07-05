package com.manitasclub.app.repository;

import com.manitasclub.app.domain.Membresia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Membresia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembresiaRepository extends JpaRepository<Membresia, Long> {}
