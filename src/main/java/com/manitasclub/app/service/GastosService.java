package com.manitasclub.app.service;

import com.manitasclub.app.service.dto.GastosDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.manitasclub.app.domain.Gastos}.
 */
public interface GastosService {
    /**
     * Save a gastos.
     *
     * @param gastosDTO the entity to save.
     * @return the persisted entity.
     */
    GastosDTO save(GastosDTO gastosDTO);

    /**
     * Updates a gastos.
     *
     * @param gastosDTO the entity to update.
     * @return the persisted entity.
     */
    GastosDTO update(GastosDTO gastosDTO);

    /**
     * Partially updates a gastos.
     *
     * @param gastosDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GastosDTO> partialUpdate(GastosDTO gastosDTO);

    /**
     * Get all the gastos.
     *
     * @return the list of entities.
     */
    List<GastosDTO> findAll();

    /**
     * Get the "id" gastos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GastosDTO> findOne(Long id);

    /**
     * Delete the "id" gastos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    BigDecimal valorPorMeses(String fechaInicio, String fechaFin);
}
