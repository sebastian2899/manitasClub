package com.manitasclub.app.service;

import com.manitasclub.app.service.dto.AbonoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.manitasclub.app.domain.Abono}.
 */
public interface AbonoService {
    /**
     * Save a abono.
     *
     * @param abonoDTO the entity to save.
     * @return the persisted entity.
     */
    AbonoDTO save(AbonoDTO abonoDTO);

    /**
     * Updates a abono.
     *
     * @param abonoDTO the entity to update.
     * @return the persisted entity.
     */
    AbonoDTO update(AbonoDTO abonoDTO);

    /**
     * Partially updates a abono.
     *
     * @param abonoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AbonoDTO> partialUpdate(AbonoDTO abonoDTO);

    /**
     * Get all the abonos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AbonoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" abono.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AbonoDTO> findOne(Long id);

    /**
     * Delete the "id" abono.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
