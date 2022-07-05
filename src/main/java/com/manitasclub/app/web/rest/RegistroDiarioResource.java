package com.manitasclub.app.web.rest;

import com.manitasclub.app.repository.RegistroDiarioRepository;
import com.manitasclub.app.service.RegistroDiarioService;
import com.manitasclub.app.service.dto.RegistroDiarioDTO;
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
 * REST controller for managing {@link com.manitasclub.app.domain.RegistroDiario}.
 */
@RestController
@RequestMapping("/api")
public class RegistroDiarioResource {

    private final Logger log = LoggerFactory.getLogger(RegistroDiarioResource.class);

    private static final String ENTITY_NAME = "registroDiario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegistroDiarioService registroDiarioService;

    private final RegistroDiarioRepository registroDiarioRepository;

    public RegistroDiarioResource(RegistroDiarioService registroDiarioService, RegistroDiarioRepository registroDiarioRepository) {
        this.registroDiarioService = registroDiarioService;
        this.registroDiarioRepository = registroDiarioRepository;
    }

    /**
     * {@code POST  /registro-diarios} : Create a new registroDiario.
     *
     * @param registroDiarioDTO the registroDiarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registroDiarioDTO, or with status {@code 400 (Bad Request)} if the registroDiario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/registro-diarios")
    public ResponseEntity<RegistroDiarioDTO> createRegistroDiario(@RequestBody RegistroDiarioDTO registroDiarioDTO)
        throws URISyntaxException {
        log.debug("REST request to save RegistroDiario : {}", registroDiarioDTO);
        if (registroDiarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new registroDiario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegistroDiarioDTO result = registroDiarioService.save(registroDiarioDTO);
        return ResponseEntity
            .created(new URI("/api/registro-diarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /registro-diarios/:id} : Updates an existing registroDiario.
     *
     * @param id the id of the registroDiarioDTO to save.
     * @param registroDiarioDTO the registroDiarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroDiarioDTO,
     * or with status {@code 400 (Bad Request)} if the registroDiarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registroDiarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/registro-diarios/{id}")
    public ResponseEntity<RegistroDiarioDTO> updateRegistroDiario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RegistroDiarioDTO registroDiarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RegistroDiario : {}, {}", id, registroDiarioDTO);
        if (registroDiarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroDiarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registroDiarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RegistroDiarioDTO result = registroDiarioService.update(registroDiarioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registroDiarioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /registro-diarios/:id} : Partial updates given fields of an existing registroDiario, field will ignore if it is null
     *
     * @param id the id of the registroDiarioDTO to save.
     * @param registroDiarioDTO the registroDiarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroDiarioDTO,
     * or with status {@code 400 (Bad Request)} if the registroDiarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the registroDiarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the registroDiarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/registro-diarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RegistroDiarioDTO> partialUpdateRegistroDiario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RegistroDiarioDTO registroDiarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RegistroDiario partially : {}, {}", id, registroDiarioDTO);
        if (registroDiarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroDiarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registroDiarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RegistroDiarioDTO> result = registroDiarioService.partialUpdate(registroDiarioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registroDiarioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /registro-diarios} : get all the registroDiarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registroDiarios in body.
     */
    @GetMapping("/registro-diarios")
    public List<RegistroDiarioDTO> getAllRegistroDiarios() {
        log.debug("REST request to get all RegistroDiarios");
        return registroDiarioService.findAll();
    }

    /**
     * {@code GET  /registro-diarios/:id} : get the "id" registroDiario.
     *
     * @param id the id of the registroDiarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registroDiarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/registro-diarios/{id}")
    public ResponseEntity<RegistroDiarioDTO> getRegistroDiario(@PathVariable Long id) {
        log.debug("REST request to get RegistroDiario : {}", id);
        Optional<RegistroDiarioDTO> registroDiarioDTO = registroDiarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(registroDiarioDTO);
    }

    /**
     * {@code DELETE  /registro-diarios/:id} : delete the "id" registroDiario.
     *
     * @param id the id of the registroDiarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/registro-diarios/{id}")
    public ResponseEntity<Void> deleteRegistroDiario(@PathVariable Long id) {
        log.debug("REST request to delete RegistroDiario : {}", id);
        registroDiarioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
