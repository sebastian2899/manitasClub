package com.manitasclub.app.web.rest;

import com.manitasclub.app.repository.AcudienteRepository;
import com.manitasclub.app.service.AcudienteService;
import com.manitasclub.app.service.dto.AcudienteDTO;
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
 * REST controller for managing {@link com.manitasclub.app.domain.Acudiente}.
 */
@RestController
@RequestMapping("/api")
public class AcudienteResource {

    private final Logger log = LoggerFactory.getLogger(AcudienteResource.class);

    private static final String ENTITY_NAME = "acudiente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcudienteService acudienteService;

    private final AcudienteRepository acudienteRepository;

    public AcudienteResource(AcudienteService acudienteService, AcudienteRepository acudienteRepository) {
        this.acudienteService = acudienteService;
        this.acudienteRepository = acudienteRepository;
    }

    /**
     * {@code POST  /acudientes} : Create a new acudiente.
     *
     * @param acudienteDTO the acudienteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acudienteDTO, or with status {@code 400 (Bad Request)} if the acudiente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acudientes")
    public ResponseEntity<AcudienteDTO> createAcudiente(@RequestBody AcudienteDTO acudienteDTO) throws URISyntaxException {
        log.debug("REST request to save Acudiente : {}", acudienteDTO);
        if (acudienteDTO.getId() != null) {
            throw new BadRequestAlertException("A new acudiente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AcudienteDTO result = acudienteService.save(acudienteDTO);
        return ResponseEntity
            .created(new URI("/api/acudientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acudientes/:id} : Updates an existing acudiente.
     *
     * @param id the id of the acudienteDTO to save.
     * @param acudienteDTO the acudienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acudienteDTO,
     * or with status {@code 400 (Bad Request)} if the acudienteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acudienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acudientes/{id}")
    public ResponseEntity<AcudienteDTO> updateAcudiente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AcudienteDTO acudienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Acudiente : {}, {}", id, acudienteDTO);
        if (acudienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acudienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acudienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AcudienteDTO result = acudienteService.update(acudienteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acudienteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acudientes/:id} : Partial updates given fields of an existing acudiente, field will ignore if it is null
     *
     * @param id the id of the acudienteDTO to save.
     * @param acudienteDTO the acudienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acudienteDTO,
     * or with status {@code 400 (Bad Request)} if the acudienteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the acudienteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the acudienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acudientes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AcudienteDTO> partialUpdateAcudiente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AcudienteDTO acudienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Acudiente partially : {}, {}", id, acudienteDTO);
        if (acudienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acudienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acudienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AcudienteDTO> result = acudienteService.partialUpdate(acudienteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acudienteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /acudientes} : get all the acudientes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acudientes in body.
     */
    @GetMapping("/acudientes")
    public List<AcudienteDTO> getAllAcudientes() {
        log.debug("REST request to get all Acudientes");
        return acudienteService.findAll();
    }

    /**
     * {@code GET  /acudientes/:id} : get the "id" acudiente.
     *
     * @param id the id of the acudienteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acudienteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acudientes/{id}")
    public ResponseEntity<AcudienteDTO> getAcudiente(@PathVariable Long id) {
        log.debug("REST request to get Acudiente : {}", id);
        Optional<AcudienteDTO> acudienteDTO = acudienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acudienteDTO);
    }

    /**
     * {@code DELETE  /acudientes/:id} : delete the "id" acudiente.
     *
     * @param id the id of the acudienteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acudientes/{id}")
    public ResponseEntity<Void> deleteAcudiente(@PathVariable Long id) {
        log.debug("REST request to delete Acudiente : {}", id);
        acudienteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
