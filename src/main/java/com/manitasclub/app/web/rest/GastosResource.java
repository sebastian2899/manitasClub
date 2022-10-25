package com.manitasclub.app.web.rest;

import com.manitasclub.app.repository.GastosRepository;
import com.manitasclub.app.service.GastosService;
import com.manitasclub.app.service.dto.GastosDTO;
import com.manitasclub.app.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.manitasclub.app.domain.Gastos}.
 */
@RestController
@RequestMapping("/api")
public class GastosResource {

    private final Logger log = LoggerFactory.getLogger(GastosResource.class);

    private static final String ENTITY_NAME = "gastos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GastosService gastosService;

    private final GastosRepository gastosRepository;

    public GastosResource(GastosService gastosService, GastosRepository gastosRepository) {
        this.gastosService = gastosService;
        this.gastosRepository = gastosRepository;
    }

    /**
     * {@code POST  /gastos} : Create a new gastos.
     *
     * @param gastosDTO the gastosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gastosDTO, or with status {@code 400 (Bad Request)} if the gastos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gastos")
    public ResponseEntity<GastosDTO> createGastos(@RequestBody GastosDTO gastosDTO) throws URISyntaxException {
        log.debug("REST request to save Gastos : {}", gastosDTO);
        if (gastosDTO.getId() != null) {
            throw new BadRequestAlertException("A new gastos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GastosDTO result = gastosService.save(gastosDTO);
        return ResponseEntity
            .created(new URI("/api/gastos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gastos/:id} : Updates an existing gastos.
     *
     * @param id the id of the gastosDTO to save.
     * @param gastosDTO the gastosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gastosDTO,
     * or with status {@code 400 (Bad Request)} if the gastosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gastosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gastos/{id}")
    public ResponseEntity<GastosDTO> updateGastos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GastosDTO gastosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Gastos : {}, {}", id, gastosDTO);
        if (gastosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gastosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gastosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GastosDTO result = gastosService.update(gastosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gastosDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("gastos/consultarValorMes/{fechaInicio}/{fechaFin}")
    public ResponseEntity<BigDecimal> valuePerMonths(@PathVariable String fechaInicio, @PathVariable String fechaFin) {
        log.debug("Rest request to get values per months");
        BigDecimal value = gastosService.valorPorMeses(fechaInicio, fechaFin);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }

    /**
     * {@code PATCH  /gastos/:id} : Partial updates given fields of an existing gastos, field will ignore if it is null
     *
     * @param id the id of the gastosDTO to save.
     * @param gastosDTO the gastosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gastosDTO,
     * or with status {@code 400 (Bad Request)} if the gastosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gastosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gastosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gastos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GastosDTO> partialUpdateGastos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GastosDTO gastosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Gastos partially : {}, {}", id, gastosDTO);
        if (gastosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gastosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gastosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GastosDTO> result = gastosService.partialUpdate(gastosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gastosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gastos} : get all the gastos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gastos in body.
     */
    @GetMapping("/gastos")
    public List<GastosDTO> getAllGastos() {
        log.debug("REST request to get all Gastos");
        return gastosService.findAll();
    }

    /**
     * {@code GET  /gastos/:id} : get the "id" gastos.
     *
     * @param id the id of the gastosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gastosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gastos/{id}")
    public ResponseEntity<GastosDTO> getGastos(@PathVariable Long id) {
        log.debug("REST request to get Gastos : {}", id);
        Optional<GastosDTO> gastosDTO = gastosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gastosDTO);
    }

    /**
     * {@code DELETE  /gastos/:id} : delete the "id" gastos.
     *
     * @param id the id of the gastosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gastos/{id}")
    public ResponseEntity<Void> deleteGastos(@PathVariable Long id) {
        log.debug("REST request to delete Gastos : {}", id);
        gastosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
