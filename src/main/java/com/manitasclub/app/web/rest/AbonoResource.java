package com.manitasclub.app.web.rest;

import com.manitasclub.app.repository.AbonoRepository;
import com.manitasclub.app.service.AbonoService;
import com.manitasclub.app.service.dto.AbonoDTO;
import com.manitasclub.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.manitasclub.app.domain.Abono}.
 */
@RestController
@RequestMapping("/api")
public class AbonoResource {

    private final Logger log = LoggerFactory.getLogger(AbonoResource.class);

    private static final String ENTITY_NAME = "abono";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AbonoService abonoService;

    private final AbonoRepository abonoRepository;

    public AbonoResource(AbonoService abonoService, AbonoRepository abonoRepository) {
        this.abonoService = abonoService;
        this.abonoRepository = abonoRepository;
    }

    /**
     * {@code POST  /abonos} : Create a new abono.
     *
     * @param abonoDTO the abonoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new abonoDTO, or with status {@code 400 (Bad Request)} if the abono has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/abonos")
    public ResponseEntity<AbonoDTO> createAbono(@RequestBody AbonoDTO abonoDTO) throws URISyntaxException {
        log.debug("REST request to save Abono : {}", abonoDTO);
        if (abonoDTO.getId() != null) {
            throw new BadRequestAlertException("A new abono cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AbonoDTO result = abonoService.save(abonoDTO);
        return ResponseEntity
            .created(new URI("/api/abonos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /abonos/:id} : Updates an existing abono.
     *
     * @param id the id of the abonoDTO to save.
     * @param abonoDTO the abonoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated abonoDTO,
     * or with status {@code 400 (Bad Request)} if the abonoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the abonoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/abonos/{id}")
    public ResponseEntity<AbonoDTO> updateAbono(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AbonoDTO abonoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Abono : {}, {}", id, abonoDTO);
        if (abonoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, abonoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!abonoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AbonoDTO result = abonoService.update(abonoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, abonoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /abonos/:id} : Partial updates given fields of an existing abono, field will ignore if it is null
     *
     * @param id the id of the abonoDTO to save.
     * @param abonoDTO the abonoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated abonoDTO,
     * or with status {@code 400 (Bad Request)} if the abonoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the abonoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the abonoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/abonos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AbonoDTO> partialUpdateAbono(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AbonoDTO abonoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Abono partially : {}, {}", id, abonoDTO);
        if (abonoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, abonoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!abonoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AbonoDTO> result = abonoService.partialUpdate(abonoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, abonoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /abonos} : get all the abonos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of abonos in body.
     */
    @GetMapping("/abonos")
    public ResponseEntity<List<AbonoDTO>> getAllAbonos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Abonos");
        Page<AbonoDTO> page = abonoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /abonos/:id} : get the "id" abono.
     *
     * @param id the id of the abonoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the abonoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/abonos/{id}")
    public ResponseEntity<AbonoDTO> getAbono(@PathVariable Long id) {
        log.debug("REST request to get Abono : {}", id);
        Optional<AbonoDTO> abonoDTO = abonoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(abonoDTO);
    }

    /**
     * {@code DELETE  /abonos/:id} : delete the "id" abono.
     *
     * @param id the id of the abonoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/abonos/{id}")
    public ResponseEntity<Void> deleteAbono(@PathVariable Long id) {
        log.debug("REST request to delete Abono : {}", id);
        abonoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
