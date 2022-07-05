package com.manitasclub.app.service;

import com.manitasclub.app.service.dto.TotalGastosDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.manitasclub.app.domain.TotalGastos}.
 */
public interface TotalGastosService {
    /**
     * Save a totalGastos.
     *
     * @param totalGastosDTO the entity to save.
     * @return the persisted entity.
     */
    TotalGastosDTO save(TotalGastosDTO totalGastosDTO);

    /**
     * Updates a totalGastos.
     *
     * @param totalGastosDTO the entity to update.
     * @return the persisted entity.
     */
    TotalGastosDTO update(TotalGastosDTO totalGastosDTO);

    /**
     * Partially updates a totalGastos.
     *
     * @param totalGastosDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TotalGastosDTO> partialUpdate(TotalGastosDTO totalGastosDTO);

    /**
     * Get all the totalGastos.
     *
     * @return the list of entities.
     */
    List<TotalGastosDTO> findAll();

    /**
     * Get the "id" totalGastos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TotalGastosDTO> findOne(Long id);

    /**
     * Delete the "id" totalGastos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
