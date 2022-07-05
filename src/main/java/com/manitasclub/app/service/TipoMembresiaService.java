package com.manitasclub.app.service;

import com.manitasclub.app.service.dto.TipoMembresiaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.manitasclub.app.domain.TipoMembresia}.
 */
public interface TipoMembresiaService {
    /**
     * Save a tipoMembresia.
     *
     * @param tipoMembresiaDTO the entity to save.
     * @return the persisted entity.
     */
    TipoMembresiaDTO save(TipoMembresiaDTO tipoMembresiaDTO);

    /**
     * Updates a tipoMembresia.
     *
     * @param tipoMembresiaDTO the entity to update.
     * @return the persisted entity.
     */
    TipoMembresiaDTO update(TipoMembresiaDTO tipoMembresiaDTO);

    /**
     * Partially updates a tipoMembresia.
     *
     * @param tipoMembresiaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoMembresiaDTO> partialUpdate(TipoMembresiaDTO tipoMembresiaDTO);

    /**
     * Get all the tipoMembresias.
     *
     * @return the list of entities.
     */
    List<TipoMembresiaDTO> findAll();

    /**
     * Get the "id" tipoMembresia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoMembresiaDTO> findOne(Long id);

    /**
     * Delete the "id" tipoMembresia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
