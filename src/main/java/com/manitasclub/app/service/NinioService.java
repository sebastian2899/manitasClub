package com.manitasclub.app.service;

import com.manitasclub.app.domain.Ninio;
import com.manitasclub.app.service.dto.NinioDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.manitasclub.app.domain.Ninio}.
 */
public interface NinioService {
    /**
     * Save a ninio.
     *
     * @param ninioDTO the entity to save.
     * @return the persisted entity.
     */
    NinioDTO save(NinioDTO ninioDTO);

    /**
     * Updates a ninio.
     *
     * @param ninioDTO the entity to update.
     * @return the persisted entity.
     */
    NinioDTO update(NinioDTO ninioDTO);

    /**
     * Partially updates a ninio.
     *
     * @param ninioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NinioDTO> partialUpdate(NinioDTO ninioDTO);

    /**
     * Get all the ninios.
     *
     * @return the list of entities.
     */
    List<Ninio> findAll();

    /**
     * Get the "id" ninio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NinioDTO> findOne(Long id);

    List<Ninio> ninioByFilter(NinioDTO ninioDTO);

    /**
     * Delete the "id" ninio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
