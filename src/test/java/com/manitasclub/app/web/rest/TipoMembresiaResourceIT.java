package com.manitasclub.app.web.rest;

import static com.manitasclub.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.manitasclub.app.IntegrationTest;
import com.manitasclub.app.domain.TipoMembresia;
import com.manitasclub.app.repository.TipoMembresiaRepository;
import com.manitasclub.app.service.dto.TipoMembresiaDTO;
import com.manitasclub.app.service.mapper.TipoMembresiaMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TipoMembresiaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoMembresiaResourceIT {

    private static final String DEFAULT_NOMBRE_MEMBRESIA = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_MEMBRESIA = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR_MEMBRESIA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_MEMBRESIA = new BigDecimal(2);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-membresias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoMembresiaRepository tipoMembresiaRepository;

    @Autowired
    private TipoMembresiaMapper tipoMembresiaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoMembresiaMockMvc;

    private TipoMembresia tipoMembresia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoMembresia createEntity(EntityManager em) {
        TipoMembresia tipoMembresia = new TipoMembresia()
            .nombreMembresia(DEFAULT_NOMBRE_MEMBRESIA)
            .valorMembresia(DEFAULT_VALOR_MEMBRESIA)
            .descripcion(DEFAULT_DESCRIPCION);
        return tipoMembresia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoMembresia createUpdatedEntity(EntityManager em) {
        TipoMembresia tipoMembresia = new TipoMembresia()
            .nombreMembresia(UPDATED_NOMBRE_MEMBRESIA)
            .valorMembresia(UPDATED_VALOR_MEMBRESIA)
            .descripcion(UPDATED_DESCRIPCION);
        return tipoMembresia;
    }

    @BeforeEach
    public void initTest() {
        tipoMembresia = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoMembresia() throws Exception {
        int databaseSizeBeforeCreate = tipoMembresiaRepository.findAll().size();
        // Create the TipoMembresia
        TipoMembresiaDTO tipoMembresiaDTO = tipoMembresiaMapper.toDto(tipoMembresia);
        restTipoMembresiaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoMembresiaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TipoMembresia in the database
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeCreate + 1);
        TipoMembresia testTipoMembresia = tipoMembresiaList.get(tipoMembresiaList.size() - 1);
        assertThat(testTipoMembresia.getNombreMembresia()).isEqualTo(DEFAULT_NOMBRE_MEMBRESIA);
        assertThat(testTipoMembresia.getValorMembresia()).isEqualByComparingTo(DEFAULT_VALOR_MEMBRESIA);
        assertThat(testTipoMembresia.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createTipoMembresiaWithExistingId() throws Exception {
        // Create the TipoMembresia with an existing ID
        tipoMembresia.setId(1L);
        TipoMembresiaDTO tipoMembresiaDTO = tipoMembresiaMapper.toDto(tipoMembresia);

        int databaseSizeBeforeCreate = tipoMembresiaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoMembresiaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoMembresiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoMembresia in the database
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTipoMembresias() throws Exception {
        // Initialize the database
        tipoMembresiaRepository.saveAndFlush(tipoMembresia);

        // Get all the tipoMembresiaList
        restTipoMembresiaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoMembresia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreMembresia").value(hasItem(DEFAULT_NOMBRE_MEMBRESIA)))
            .andExpect(jsonPath("$.[*].valorMembresia").value(hasItem(sameNumber(DEFAULT_VALOR_MEMBRESIA))))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getTipoMembresia() throws Exception {
        // Initialize the database
        tipoMembresiaRepository.saveAndFlush(tipoMembresia);

        // Get the tipoMembresia
        restTipoMembresiaMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoMembresia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoMembresia.getId().intValue()))
            .andExpect(jsonPath("$.nombreMembresia").value(DEFAULT_NOMBRE_MEMBRESIA))
            .andExpect(jsonPath("$.valorMembresia").value(sameNumber(DEFAULT_VALOR_MEMBRESIA)))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingTipoMembresia() throws Exception {
        // Get the tipoMembresia
        restTipoMembresiaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoMembresia() throws Exception {
        // Initialize the database
        tipoMembresiaRepository.saveAndFlush(tipoMembresia);

        int databaseSizeBeforeUpdate = tipoMembresiaRepository.findAll().size();

        // Update the tipoMembresia
        TipoMembresia updatedTipoMembresia = tipoMembresiaRepository.findById(tipoMembresia.getId()).get();
        // Disconnect from session so that the updates on updatedTipoMembresia are not directly saved in db
        em.detach(updatedTipoMembresia);
        updatedTipoMembresia
            .nombreMembresia(UPDATED_NOMBRE_MEMBRESIA)
            .valorMembresia(UPDATED_VALOR_MEMBRESIA)
            .descripcion(UPDATED_DESCRIPCION);
        TipoMembresiaDTO tipoMembresiaDTO = tipoMembresiaMapper.toDto(updatedTipoMembresia);

        restTipoMembresiaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoMembresiaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoMembresiaDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoMembresia in the database
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeUpdate);
        TipoMembresia testTipoMembresia = tipoMembresiaList.get(tipoMembresiaList.size() - 1);
        assertThat(testTipoMembresia.getNombreMembresia()).isEqualTo(UPDATED_NOMBRE_MEMBRESIA);
        assertThat(testTipoMembresia.getValorMembresia()).isEqualByComparingTo(UPDATED_VALOR_MEMBRESIA);
        assertThat(testTipoMembresia.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingTipoMembresia() throws Exception {
        int databaseSizeBeforeUpdate = tipoMembresiaRepository.findAll().size();
        tipoMembresia.setId(count.incrementAndGet());

        // Create the TipoMembresia
        TipoMembresiaDTO tipoMembresiaDTO = tipoMembresiaMapper.toDto(tipoMembresia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoMembresiaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoMembresiaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoMembresiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoMembresia in the database
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoMembresia() throws Exception {
        int databaseSizeBeforeUpdate = tipoMembresiaRepository.findAll().size();
        tipoMembresia.setId(count.incrementAndGet());

        // Create the TipoMembresia
        TipoMembresiaDTO tipoMembresiaDTO = tipoMembresiaMapper.toDto(tipoMembresia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMembresiaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoMembresiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoMembresia in the database
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoMembresia() throws Exception {
        int databaseSizeBeforeUpdate = tipoMembresiaRepository.findAll().size();
        tipoMembresia.setId(count.incrementAndGet());

        // Create the TipoMembresia
        TipoMembresiaDTO tipoMembresiaDTO = tipoMembresiaMapper.toDto(tipoMembresia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMembresiaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoMembresiaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoMembresia in the database
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoMembresiaWithPatch() throws Exception {
        // Initialize the database
        tipoMembresiaRepository.saveAndFlush(tipoMembresia);

        int databaseSizeBeforeUpdate = tipoMembresiaRepository.findAll().size();

        // Update the tipoMembresia using partial update
        TipoMembresia partialUpdatedTipoMembresia = new TipoMembresia();
        partialUpdatedTipoMembresia.setId(tipoMembresia.getId());

        partialUpdatedTipoMembresia.valorMembresia(UPDATED_VALOR_MEMBRESIA);

        restTipoMembresiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoMembresia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoMembresia))
            )
            .andExpect(status().isOk());

        // Validate the TipoMembresia in the database
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeUpdate);
        TipoMembresia testTipoMembresia = tipoMembresiaList.get(tipoMembresiaList.size() - 1);
        assertThat(testTipoMembresia.getNombreMembresia()).isEqualTo(DEFAULT_NOMBRE_MEMBRESIA);
        assertThat(testTipoMembresia.getValorMembresia()).isEqualByComparingTo(UPDATED_VALOR_MEMBRESIA);
        assertThat(testTipoMembresia.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateTipoMembresiaWithPatch() throws Exception {
        // Initialize the database
        tipoMembresiaRepository.saveAndFlush(tipoMembresia);

        int databaseSizeBeforeUpdate = tipoMembresiaRepository.findAll().size();

        // Update the tipoMembresia using partial update
        TipoMembresia partialUpdatedTipoMembresia = new TipoMembresia();
        partialUpdatedTipoMembresia.setId(tipoMembresia.getId());

        partialUpdatedTipoMembresia
            .nombreMembresia(UPDATED_NOMBRE_MEMBRESIA)
            .valorMembresia(UPDATED_VALOR_MEMBRESIA)
            .descripcion(UPDATED_DESCRIPCION);

        restTipoMembresiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoMembresia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoMembresia))
            )
            .andExpect(status().isOk());

        // Validate the TipoMembresia in the database
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeUpdate);
        TipoMembresia testTipoMembresia = tipoMembresiaList.get(tipoMembresiaList.size() - 1);
        assertThat(testTipoMembresia.getNombreMembresia()).isEqualTo(UPDATED_NOMBRE_MEMBRESIA);
        assertThat(testTipoMembresia.getValorMembresia()).isEqualByComparingTo(UPDATED_VALOR_MEMBRESIA);
        assertThat(testTipoMembresia.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingTipoMembresia() throws Exception {
        int databaseSizeBeforeUpdate = tipoMembresiaRepository.findAll().size();
        tipoMembresia.setId(count.incrementAndGet());

        // Create the TipoMembresia
        TipoMembresiaDTO tipoMembresiaDTO = tipoMembresiaMapper.toDto(tipoMembresia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoMembresiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoMembresiaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoMembresiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoMembresia in the database
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoMembresia() throws Exception {
        int databaseSizeBeforeUpdate = tipoMembresiaRepository.findAll().size();
        tipoMembresia.setId(count.incrementAndGet());

        // Create the TipoMembresia
        TipoMembresiaDTO tipoMembresiaDTO = tipoMembresiaMapper.toDto(tipoMembresia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMembresiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoMembresiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoMembresia in the database
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoMembresia() throws Exception {
        int databaseSizeBeforeUpdate = tipoMembresiaRepository.findAll().size();
        tipoMembresia.setId(count.incrementAndGet());

        // Create the TipoMembresia
        TipoMembresiaDTO tipoMembresiaDTO = tipoMembresiaMapper.toDto(tipoMembresia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMembresiaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoMembresiaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoMembresia in the database
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoMembresia() throws Exception {
        // Initialize the database
        tipoMembresiaRepository.saveAndFlush(tipoMembresia);

        int databaseSizeBeforeDelete = tipoMembresiaRepository.findAll().size();

        // Delete the tipoMembresia
        restTipoMembresiaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoMembresia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoMembresia> tipoMembresiaList = tipoMembresiaRepository.findAll();
        assertThat(tipoMembresiaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
