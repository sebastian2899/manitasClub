package com.manitasclub.app.service;

import com.manitasclub.app.service.dto.MembresiaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.manitasclub.app.domain.Membresia}.
 */
public interface MembresiaService {
    /**
     * Save a membresia.
     *
     * @param membresiaDTO the entity to save.
     * @return the persisted entity.
     */
    MembresiaDTO save(MembresiaDTO membresiaDTO);

    /**
     * Updates a membresia.
     *
     * @param membresiaDTO the entity to update.
     * @return the persisted entity.
     */
    MembresiaDTO update(MembresiaDTO membresiaDTO);

    /**
     * Partially updates a membresia.
     *
     * @param membresiaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MembresiaDTO> partialUpdate(MembresiaDTO membresiaDTO);

    /**
     * Get all the membresias.
     *
     * @return the list of entities.
     */
    List<MembresiaDTO> findAll();

    /**
     * Get the "id" membresia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MembresiaDTO> findOne(Long id);

    /**
     * Delete the "id" membresia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
