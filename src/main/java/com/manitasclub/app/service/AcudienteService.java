package com.manitasclub.app.service;

import com.manitasclub.app.service.dto.AcudienteDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.manitasclub.app.domain.Acudiente}.
 */
public interface AcudienteService {
    /**
     * Save a acudiente.
     *
     * @param acudienteDTO the entity to save.
     * @return the persisted entity.
     */
    AcudienteDTO save(AcudienteDTO acudienteDTO);

    /**
     * Updates a acudiente.
     *
     * @param acudienteDTO the entity to update.
     * @return the persisted entity.
     */
    AcudienteDTO update(AcudienteDTO acudienteDTO);

    /**
     * Partially updates a acudiente.
     *
     * @param acudienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AcudienteDTO> partialUpdate(AcudienteDTO acudienteDTO);

    /**
     * Get all the acudientes.
     *
     * @return the list of entities.
     */
    List<AcudienteDTO> findAll();

    /**
     * Get the "id" acudiente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AcudienteDTO> findOne(Long id);

    /**
     * Delete the "id" acudiente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
