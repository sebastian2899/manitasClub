package com.manitasclub.app.web.rest;

import com.manitasclub.app.repository.TipoMembresiaRepository;
import com.manitasclub.app.service.TipoMembresiaService;
import com.manitasclub.app.service.dto.TipoMembresiaDTO;
import com.manitasclub.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.manitasclub.app.domain.TipoMembresia}.
 */
@RestController
@RequestMapping("/api")
public class TipoMembresiaResource {

    private final Logger log = LoggerFactory.getLogger(TipoMembresiaResource.class);

    private static final String ENTITY_NAME = "tipoMembresia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoMembresiaService tipoMembresiaService;

    private final TipoMembresiaRepository tipoMembresiaRepository;

    public TipoMembresiaResource(TipoMembresiaService tipoMembresiaService, TipoMembresiaRepository tipoMembresiaRepository) {
        this.tipoMembresiaService = tipoMembresiaService;
        this.tipoMembresiaRepository = tipoMembresiaRepository;
    }

    /**
     * {@code POST  /tipo-membresias} : Create a new tipoMembresia.
     *
     * @param tipoMembresiaDTO the tipoMembresiaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoMembresiaDTO, or with status {@code 400 (Bad Request)} if the tipoMembresia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-membresias")
    public ResponseEntity<TipoMembresiaDTO> createTipoMembresia(@RequestBody TipoMembresiaDTO tipoMembresiaDTO) throws URISyntaxException {
        log.debug("REST request to save TipoMembresia : {}", tipoMembresiaDTO);
        if (tipoMembresiaDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoMembresia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoMembresiaDTO result = tipoMembresiaService.save(tipoMembresiaDTO);
        return ResponseEntity
            .created(new URI("/api/tipo-membresias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-membresias/:id} : Updates an existing tipoMembresia.
     *
     * @param id the id of the tipoMembresiaDTO to save.
     * @param tipoMembresiaDTO the tipoMembresiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoMembresiaDTO,
     * or with status {@code 400 (Bad Request)} if the tipoMembresiaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoMembresiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-membresias/{id}")
    public ResponseEntity<TipoMembresiaDTO> updateTipoMembresia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoMembresiaDTO tipoMembresiaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoMembresia : {}, {}", id, tipoMembresiaDTO);
        if (tipoMembresiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoMembresiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoMembresiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoMembresiaDTO result = tipoMembresiaService.update(tipoMembresiaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoMembresiaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-membresias/:id} : Partial updates given fields of an existing tipoMembresia, field will ignore if it is null
     *
     * @param id the id of the tipoMembresiaDTO to save.
     * @param tipoMembresiaDTO the tipoMembresiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoMembresiaDTO,
     * or with status {@code 400 (Bad Request)} if the tipoMembresiaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoMembresiaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoMembresiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-membresias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoMembresiaDTO> partialUpdateTipoMembresia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoMembresiaDTO tipoMembresiaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoMembresia partially : {}, {}", id, tipoMembresiaDTO);
        if (tipoMembresiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoMembresiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoMembresiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoMembresiaDTO> result = tipoMembresiaService.partialUpdate(tipoMembresiaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoMembresiaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-membresias} : get all the tipoMembresias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoMembresias in body.
     */
    @GetMapping("/tipo-membresias")
    public List<TipoMembresiaDTO> getAllTipoMembresias() {
        log.debug("REST request to get all TipoMembresias");
        return tipoMembresiaService.findAll();
    }

    /**
     * {@code GET  /tipo-membresias/:id} : get the "id" tipoMembresia.
     *
     * @param id the id of the tipoMembresiaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoMembresiaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-membresias/{id}")
    public ResponseEntity<TipoMembresiaDTO> getTipoMembresia(@PathVariable Long id) {
        log.debug("REST request to get TipoMembresia : {}", id);
        Optional<TipoMembresiaDTO> tipoMembresiaDTO = tipoMembresiaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoMembresiaDTO);
    }

    /**
     * {@code DELETE  /tipo-membresias/:id} : delete the "id" tipoMembresia.
     *
     * @param id the id of the tipoMembresiaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-membresias/{id}")
    public ResponseEntity<Void> deleteTipoMembresia(@PathVariable Long id) {
        log.debug("REST request to delete TipoMembresia : {}", id);
        tipoMembresiaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
