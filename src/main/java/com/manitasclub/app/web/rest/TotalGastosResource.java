package com.manitasclub.app.web.rest;

import com.manitasclub.app.repository.TotalGastosRepository;
import com.manitasclub.app.service.TotalGastosService;
import com.manitasclub.app.service.dto.TotalGastosDTO;
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
 * REST controller for managing {@link com.manitasclub.app.domain.TotalGastos}.
 */
@RestController
@RequestMapping("/api")
public class TotalGastosResource {

    private final Logger log = LoggerFactory.getLogger(TotalGastosResource.class);

    private static final String ENTITY_NAME = "totalGastos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TotalGastosService totalGastosService;

    private final TotalGastosRepository totalGastosRepository;

    public TotalGastosResource(TotalGastosService totalGastosService, TotalGastosRepository totalGastosRepository) {
        this.totalGastosService = totalGastosService;
        this.totalGastosRepository = totalGastosRepository;
    }

    /**
     * {@code POST  /total-gastos} : Create a new totalGastos.
     *
     * @param totalGastosDTO the totalGastosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new totalGastosDTO, or with status {@code 400 (Bad Request)} if the totalGastos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/total-gastos")
    public ResponseEntity<TotalGastosDTO> createTotalGastos(@RequestBody TotalGastosDTO totalGastosDTO) throws URISyntaxException {
        log.debug("REST request to save TotalGastos : {}", totalGastosDTO);
        if (totalGastosDTO.getId() != null) {
            throw new BadRequestAlertException("A new totalGastos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TotalGastosDTO result = totalGastosService.save(totalGastosDTO);
        return ResponseEntity
            .created(new URI("/api/total-gastos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /total-gastos/:id} : Updates an existing totalGastos.
     *
     * @param id the id of the totalGastosDTO to save.
     * @param totalGastosDTO the totalGastosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated totalGastosDTO,
     * or with status {@code 400 (Bad Request)} if the totalGastosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the totalGastosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/total-gastos/{id}")
    public ResponseEntity<TotalGastosDTO> updateTotalGastos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TotalGastosDTO totalGastosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TotalGastos : {}, {}", id, totalGastosDTO);
        if (totalGastosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, totalGastosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!totalGastosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TotalGastosDTO result = totalGastosService.update(totalGastosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, totalGastosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /total-gastos/:id} : Partial updates given fields of an existing totalGastos, field will ignore if it is null
     *
     * @param id the id of the totalGastosDTO to save.
     * @param totalGastosDTO the totalGastosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated totalGastosDTO,
     * or with status {@code 400 (Bad Request)} if the totalGastosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the totalGastosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the totalGastosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/total-gastos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TotalGastosDTO> partialUpdateTotalGastos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TotalGastosDTO totalGastosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TotalGastos partially : {}, {}", id, totalGastosDTO);
        if (totalGastosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, totalGastosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!totalGastosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TotalGastosDTO> result = totalGastosService.partialUpdate(totalGastosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, totalGastosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /total-gastos} : get all the totalGastos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of totalGastos in body.
     */
    @GetMapping("/total-gastos")
    public List<TotalGastosDTO> getAllTotalGastos() {
        log.debug("REST request to get all TotalGastos");
        return totalGastosService.findAll();
    }

    /**
     * {@code GET  /total-gastos/:id} : get the "id" totalGastos.
     *
     * @param id the id of the totalGastosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the totalGastosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/total-gastos/{id}")
    public ResponseEntity<TotalGastosDTO> getTotalGastos(@PathVariable Long id) {
        log.debug("REST request to get TotalGastos : {}", id);
        Optional<TotalGastosDTO> totalGastosDTO = totalGastosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(totalGastosDTO);
    }

    /**
     * {@code DELETE  /total-gastos/:id} : delete the "id" totalGastos.
     *
     * @param id the id of the totalGastosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/total-gastos/{id}")
    public ResponseEntity<Void> deleteTotalGastos(@PathVariable Long id) {
        log.debug("REST request to delete TotalGastos : {}", id);
        totalGastosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
