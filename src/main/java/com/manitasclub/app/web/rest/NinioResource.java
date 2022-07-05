package com.manitasclub.app.web.rest;

import com.manitasclub.app.repository.NinioRepository;
import com.manitasclub.app.service.NinioService;
import com.manitasclub.app.service.dto.NinioDTO;
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
 * REST controller for managing {@link com.manitasclub.app.domain.Ninio}.
 */
@RestController
@RequestMapping("/api")
public class NinioResource {

    private final Logger log = LoggerFactory.getLogger(NinioResource.class);

    private static final String ENTITY_NAME = "ninio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NinioService ninioService;

    private final NinioRepository ninioRepository;

    public NinioResource(NinioService ninioService, NinioRepository ninioRepository) {
        this.ninioService = ninioService;
        this.ninioRepository = ninioRepository;
    }

    /**
     * {@code POST  /ninios} : Create a new ninio.
     *
     * @param ninioDTO the ninioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ninioDTO, or with status {@code 400 (Bad Request)} if the ninio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ninios")
    public ResponseEntity<NinioDTO> createNinio(@RequestBody NinioDTO ninioDTO) throws URISyntaxException {
        log.debug("REST request to save Ninio : {}", ninioDTO);
        if (ninioDTO.getId() != null) {
            throw new BadRequestAlertException("A new ninio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NinioDTO result = ninioService.save(ninioDTO);
        return ResponseEntity
            .created(new URI("/api/ninios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ninios/:id} : Updates an existing ninio.
     *
     * @param id the id of the ninioDTO to save.
     * @param ninioDTO the ninioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ninioDTO,
     * or with status {@code 400 (Bad Request)} if the ninioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ninioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ninios/{id}")
    public ResponseEntity<NinioDTO> updateNinio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NinioDTO ninioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ninio : {}, {}", id, ninioDTO);
        if (ninioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ninioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ninioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NinioDTO result = ninioService.update(ninioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ninioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ninios/:id} : Partial updates given fields of an existing ninio, field will ignore if it is null
     *
     * @param id the id of the ninioDTO to save.
     * @param ninioDTO the ninioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ninioDTO,
     * or with status {@code 400 (Bad Request)} if the ninioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ninioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ninioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ninios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NinioDTO> partialUpdateNinio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NinioDTO ninioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ninio partially : {}, {}", id, ninioDTO);
        if (ninioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ninioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ninioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NinioDTO> result = ninioService.partialUpdate(ninioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ninioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ninios} : get all the ninios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ninios in body.
     */
    @GetMapping("/ninios")
    public List<NinioDTO> getAllNinios() {
        log.debug("REST request to get all Ninios");
        return ninioService.findAll();
    }

    /**
     * {@code GET  /ninios/:id} : get the "id" ninio.
     *
     * @param id the id of the ninioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ninioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ninios/{id}")
    public ResponseEntity<NinioDTO> getNinio(@PathVariable Long id) {
        log.debug("REST request to get Ninio : {}", id);
        Optional<NinioDTO> ninioDTO = ninioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ninioDTO);
    }

    /**
     * {@code DELETE  /ninios/:id} : delete the "id" ninio.
     *
     * @param id the id of the ninioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ninios/{id}")
    public ResponseEntity<Void> deleteNinio(@PathVariable Long id) {
        log.debug("REST request to delete Ninio : {}", id);
        ninioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
