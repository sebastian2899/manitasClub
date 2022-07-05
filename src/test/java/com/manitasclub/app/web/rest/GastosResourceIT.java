package com.manitasclub.app.web.rest;

import static com.manitasclub.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.manitasclub.app.IntegrationTest;
import com.manitasclub.app.domain.Gastos;
import com.manitasclub.app.repository.GastosRepository;
import com.manitasclub.app.service.dto.GastosDTO;
import com.manitasclub.app.service.mapper.GastosMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link GastosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GastosResourceIT {

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gastos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GastosRepository gastosRepository;

    @Autowired
    private GastosMapper gastosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGastosMockMvc;

    private Gastos gastos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gastos createEntity(EntityManager em) {
        Gastos gastos = new Gastos().fechaCreacion(DEFAULT_FECHA_CREACION).valor(DEFAULT_VALOR).descripcion(DEFAULT_DESCRIPCION);
        return gastos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gastos createUpdatedEntity(EntityManager em) {
        Gastos gastos = new Gastos().fechaCreacion(UPDATED_FECHA_CREACION).valor(UPDATED_VALOR).descripcion(UPDATED_DESCRIPCION);
        return gastos;
    }

    @BeforeEach
    public void initTest() {
        gastos = createEntity(em);
    }

    @Test
    @Transactional
    void createGastos() throws Exception {
        int databaseSizeBeforeCreate = gastosRepository.findAll().size();
        // Create the Gastos
        GastosDTO gastosDTO = gastosMapper.toDto(gastos);
        restGastosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gastosDTO)))
            .andExpect(status().isCreated());

        // Validate the Gastos in the database
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeCreate + 1);
        Gastos testGastos = gastosList.get(gastosList.size() - 1);
        assertThat(testGastos.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testGastos.getValor()).isEqualByComparingTo(DEFAULT_VALOR);
        assertThat(testGastos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createGastosWithExistingId() throws Exception {
        // Create the Gastos with an existing ID
        gastos.setId(1L);
        GastosDTO gastosDTO = gastosMapper.toDto(gastos);

        int databaseSizeBeforeCreate = gastosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGastosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gastosDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gastos in the database
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGastos() throws Exception {
        // Initialize the database
        gastosRepository.saveAndFlush(gastos);

        // Get all the gastosList
        restGastosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gastos.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getGastos() throws Exception {
        // Initialize the database
        gastosRepository.saveAndFlush(gastos);

        // Get the gastos
        restGastosMockMvc
            .perform(get(ENTITY_API_URL_ID, gastos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gastos.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingGastos() throws Exception {
        // Get the gastos
        restGastosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGastos() throws Exception {
        // Initialize the database
        gastosRepository.saveAndFlush(gastos);

        int databaseSizeBeforeUpdate = gastosRepository.findAll().size();

        // Update the gastos
        Gastos updatedGastos = gastosRepository.findById(gastos.getId()).get();
        // Disconnect from session so that the updates on updatedGastos are not directly saved in db
        em.detach(updatedGastos);
        updatedGastos.fechaCreacion(UPDATED_FECHA_CREACION).valor(UPDATED_VALOR).descripcion(UPDATED_DESCRIPCION);
        GastosDTO gastosDTO = gastosMapper.toDto(updatedGastos);

        restGastosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gastosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gastosDTO))
            )
            .andExpect(status().isOk());

        // Validate the Gastos in the database
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeUpdate);
        Gastos testGastos = gastosList.get(gastosList.size() - 1);
        assertThat(testGastos.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testGastos.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testGastos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingGastos() throws Exception {
        int databaseSizeBeforeUpdate = gastosRepository.findAll().size();
        gastos.setId(count.incrementAndGet());

        // Create the Gastos
        GastosDTO gastosDTO = gastosMapper.toDto(gastos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGastosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gastosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gastos in the database
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGastos() throws Exception {
        int databaseSizeBeforeUpdate = gastosRepository.findAll().size();
        gastos.setId(count.incrementAndGet());

        // Create the Gastos
        GastosDTO gastosDTO = gastosMapper.toDto(gastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGastosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gastos in the database
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGastos() throws Exception {
        int databaseSizeBeforeUpdate = gastosRepository.findAll().size();
        gastos.setId(count.incrementAndGet());

        // Create the Gastos
        GastosDTO gastosDTO = gastosMapper.toDto(gastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGastosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gastosDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gastos in the database
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGastosWithPatch() throws Exception {
        // Initialize the database
        gastosRepository.saveAndFlush(gastos);

        int databaseSizeBeforeUpdate = gastosRepository.findAll().size();

        // Update the gastos using partial update
        Gastos partialUpdatedGastos = new Gastos();
        partialUpdatedGastos.setId(gastos.getId());

        partialUpdatedGastos.valor(UPDATED_VALOR);

        restGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGastos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGastos))
            )
            .andExpect(status().isOk());

        // Validate the Gastos in the database
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeUpdate);
        Gastos testGastos = gastosList.get(gastosList.size() - 1);
        assertThat(testGastos.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testGastos.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testGastos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateGastosWithPatch() throws Exception {
        // Initialize the database
        gastosRepository.saveAndFlush(gastos);

        int databaseSizeBeforeUpdate = gastosRepository.findAll().size();

        // Update the gastos using partial update
        Gastos partialUpdatedGastos = new Gastos();
        partialUpdatedGastos.setId(gastos.getId());

        partialUpdatedGastos.fechaCreacion(UPDATED_FECHA_CREACION).valor(UPDATED_VALOR).descripcion(UPDATED_DESCRIPCION);

        restGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGastos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGastos))
            )
            .andExpect(status().isOk());

        // Validate the Gastos in the database
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeUpdate);
        Gastos testGastos = gastosList.get(gastosList.size() - 1);
        assertThat(testGastos.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testGastos.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testGastos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingGastos() throws Exception {
        int databaseSizeBeforeUpdate = gastosRepository.findAll().size();
        gastos.setId(count.incrementAndGet());

        // Create the Gastos
        GastosDTO gastosDTO = gastosMapper.toDto(gastos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gastosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gastos in the database
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGastos() throws Exception {
        int databaseSizeBeforeUpdate = gastosRepository.findAll().size();
        gastos.setId(count.incrementAndGet());

        // Create the Gastos
        GastosDTO gastosDTO = gastosMapper.toDto(gastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gastos in the database
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGastos() throws Exception {
        int databaseSizeBeforeUpdate = gastosRepository.findAll().size();
        gastos.setId(count.incrementAndGet());

        // Create the Gastos
        GastosDTO gastosDTO = gastosMapper.toDto(gastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGastosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gastosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gastos in the database
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGastos() throws Exception {
        // Initialize the database
        gastosRepository.saveAndFlush(gastos);

        int databaseSizeBeforeDelete = gastosRepository.findAll().size();

        // Delete the gastos
        restGastosMockMvc
            .perform(delete(ENTITY_API_URL_ID, gastos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gastos> gastosList = gastosRepository.findAll();
        assertThat(gastosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
