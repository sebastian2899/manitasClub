package com.manitasclub.app.repository;

import com.manitasclub.app.domain.Ninio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ninio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NinioRepository extends JpaRepository<Ninio, Long>, JpaSpecificationExecutor<Ninio> {}
