package com.manitasclub.app.web.rest;

import com.manitasclub.app.repository.MembresiaRepository;
import com.manitasclub.app.service.MembresiaService;
import com.manitasclub.app.service.dto.MembresiaDTO;
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
 * REST controller for managing {@link com.manitasclub.app.domain.Membresia}.
 */
@RestController
@RequestMapping("/api")
public class MembresiaResource {

    private final Logger log = LoggerFactory.getLogger(MembresiaResource.class);

    private static final String ENTITY_NAME = "membresia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembresiaService membresiaService;

    private final MembresiaRepository membresiaRepository;

    public MembresiaResource(MembresiaService membresiaService, MembresiaRepository membresiaRepository) {
        this.membresiaService = membresiaService;
        this.membresiaRepository = membresiaRepository;
    }

    /**
     * {@code POST  /membresias} : Create a new membresia.
     *
     * @param membresiaDTO the membresiaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membresiaDTO, or with status {@code 400 (Bad Request)} if the membresia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/membresias")
    public ResponseEntity<MembresiaDTO> createMembresia(@RequestBody MembresiaDTO membresiaDTO) throws URISyntaxException {
        log.debug("REST request to save Membresia : {}", membresiaDTO);
        if (membresiaDTO.getId() != null) {
            throw new BadRequestAlertException("A new membresia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MembresiaDTO result = membresiaService.save(membresiaDTO);
        return ResponseEntity
            .created(new URI("/api/membresias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /membresias/:id} : Updates an existing membresia.
     *
     * @param id the id of the membresiaDTO to save.
     * @param membresiaDTO the membresiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membresiaDTO,
     * or with status {@code 400 (Bad Request)} if the membresiaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membresiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/membresias/{id}")
    public ResponseEntity<MembresiaDTO> updateMembresia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MembresiaDTO membresiaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Membresia : {}, {}", id, membresiaDTO);
        if (membresiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, membresiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!membresiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MembresiaDTO result = membresiaService.update(membresiaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membresiaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /membresias/:id} : Partial updates given fields of an existing membresia, field will ignore if it is null
     *
     * @param id the id of the membresiaDTO to save.
     * @param membresiaDTO the membresiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membresiaDTO,
     * or with status {@code 400 (Bad Request)} if the membresiaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the membresiaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the membresiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/membresias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MembresiaDTO> partialUpdateMembresia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MembresiaDTO membresiaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Membresia partially : {}, {}", id, membresiaDTO);
        if (membresiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, membresiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!membresiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MembresiaDTO> result = membresiaService.partialUpdate(membresiaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membresiaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /membresias} : get all the membresias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membresias in body.
     */
    @GetMapping("/membresias")
    public List<MembresiaDTO> getAllMembresias() {
        log.debug("REST request to get all Membresias");
        return membresiaService.findAll();
    }

    /**
     * {@code GET  /membresias/:id} : get the "id" membresia.
     *
     * @param id the id of the membresiaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membresiaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/membresias/{id}")
    public ResponseEntity<MembresiaDTO> getMembresia(@PathVariable Long id) {
        log.debug("REST request to get Membresia : {}", id);
        Optional<MembresiaDTO> membresiaDTO = membresiaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membresiaDTO);
    }

    /**
     * {@code DELETE  /membresias/:id} : delete the "id" membresia.
     *
     * @param id the id of the membresiaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/membresias/{id}")
    public ResponseEntity<Void> deleteMembresia(@PathVariable Long id) {
        log.debug("REST request to delete Membresia : {}", id);
        membresiaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
