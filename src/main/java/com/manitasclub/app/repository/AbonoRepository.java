package com.manitasclub.app.repository;

import com.manitasclub.app.domain.Abono;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Abono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbonoRepository extends JpaRepository<Abono, Long> {
    @Query("SELECT a FROM Abono a WHERE a.idMembresia = :idMembresia")
    List<Abono> findByMembresia(@Param("idMembresia") Long idMembresia);
}
