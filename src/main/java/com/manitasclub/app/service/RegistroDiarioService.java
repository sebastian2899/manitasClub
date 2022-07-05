package com.manitasclub.app.service;

import com.manitasclub.app.service.dto.RegistroDiarioDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.manitasclub.app.domain.RegistroDiario}.
 */
public interface RegistroDiarioService {
    /**
     * Save a registroDiario.
     *
     * @param registroDiarioDTO the entity to save.
     * @return the persisted entity.
     */
    RegistroDiarioDTO save(RegistroDiarioDTO registroDiarioDTO);

    /**
     * Updates a registroDiario.
     *
     * @param registroDiarioDTO the entity to update.
     * @return the persisted entity.
     */
    RegistroDiarioDTO update(RegistroDiarioDTO registroDiarioDTO);

    /**
     * Partially updates a registroDiario.
     *
     * @param registroDiarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RegistroDiarioDTO> partialUpdate(RegistroDiarioDTO registroDiarioDTO);

    /**
     * Get all the registroDiarios.
     *
     * @return the list of entities.
     */
    List<RegistroDiarioDTO> findAll();

    /**
     * Get the "id" registroDiario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RegistroDiarioDTO> findOne(Long id);

    /**
     * Delete the "id" registroDiario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
