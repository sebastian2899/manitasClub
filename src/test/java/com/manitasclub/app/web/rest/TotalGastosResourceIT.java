package com.manitasclub.app.web.rest;

import static com.manitasclub.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.manitasclub.app.IntegrationTest;
import com.manitasclub.app.domain.TotalGastos;
import com.manitasclub.app.domain.enumeration.EstadoCaja;
import com.manitasclub.app.repository.TotalGastosRepository;
import com.manitasclub.app.service.dto.TotalGastosDTO;
import com.manitasclub.app.service.mapper.TotalGastosMapper;
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
 * Integration tests for the {@link TotalGastosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TotalGastosResourceIT {

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_VALOR_INICIAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_INICIAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_TOTAL_GASTOS = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_TOTAL_GASTOS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DIFERENCIA = new BigDecimal(1);
    private static final BigDecimal UPDATED_DIFERENCIA = new BigDecimal(2);

    private static final EstadoCaja DEFAULT_ESTADO = EstadoCaja.DEUDA;
    private static final EstadoCaja UPDATED_ESTADO = EstadoCaja.SALDADA;

    private static final String ENTITY_API_URL = "/api/total-gastos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TotalGastosRepository totalGastosRepository;

    @Autowired
    private TotalGastosMapper totalGastosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTotalGastosMockMvc;

    private TotalGastos totalGastos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TotalGastos createEntity(EntityManager em) {
        TotalGastos totalGastos = new TotalGastos()
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .valorInicial(DEFAULT_VALOR_INICIAL)
            .valorTotalGastos(DEFAULT_VALOR_TOTAL_GASTOS)
            .diferencia(DEFAULT_DIFERENCIA)
            .estado(DEFAULT_ESTADO);
        return totalGastos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TotalGastos createUpdatedEntity(EntityManager em) {
        TotalGastos totalGastos = new TotalGastos()
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .valorInicial(UPDATED_VALOR_INICIAL)
            .valorTotalGastos(UPDATED_VALOR_TOTAL_GASTOS)
            .diferencia(UPDATED_DIFERENCIA)
            .estado(UPDATED_ESTADO);
        return totalGastos;
    }

    @BeforeEach
    public void initTest() {
        totalGastos = createEntity(em);
    }

    @Test
    @Transactional
    void createTotalGastos() throws Exception {
        int databaseSizeBeforeCreate = totalGastosRepository.findAll().size();
        // Create the TotalGastos
        TotalGastosDTO totalGastosDTO = totalGastosMapper.toDto(totalGastos);
        restTotalGastosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(totalGastosDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TotalGastos in the database
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeCreate + 1);
        TotalGastos testTotalGastos = totalGastosList.get(totalGastosList.size() - 1);
        assertThat(testTotalGastos.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testTotalGastos.getValorInicial()).isEqualByComparingTo(DEFAULT_VALOR_INICIAL);
        assertThat(testTotalGastos.getValorTotalGastos()).isEqualByComparingTo(DEFAULT_VALOR_TOTAL_GASTOS);
        assertThat(testTotalGastos.getDiferencia()).isEqualByComparingTo(DEFAULT_DIFERENCIA);
        assertThat(testTotalGastos.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createTotalGastosWithExistingId() throws Exception {
        // Create the TotalGastos with an existing ID
        totalGastos.setId(1L);
        TotalGastosDTO totalGastosDTO = totalGastosMapper.toDto(totalGastos);

        int databaseSizeBeforeCreate = totalGastosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTotalGastosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(totalGastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TotalGastos in the database
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTotalGastos() throws Exception {
        // Initialize the database
        totalGastosRepository.saveAndFlush(totalGastos);

        // Get all the totalGastosList
        restTotalGastosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(totalGastos.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].valorInicial").value(hasItem(sameNumber(DEFAULT_VALOR_INICIAL))))
            .andExpect(jsonPath("$.[*].valorTotalGastos").value(hasItem(sameNumber(DEFAULT_VALOR_TOTAL_GASTOS))))
            .andExpect(jsonPath("$.[*].diferencia").value(hasItem(sameNumber(DEFAULT_DIFERENCIA))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    void getTotalGastos() throws Exception {
        // Initialize the database
        totalGastosRepository.saveAndFlush(totalGastos);

        // Get the totalGastos
        restTotalGastosMockMvc
            .perform(get(ENTITY_API_URL_ID, totalGastos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(totalGastos.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.valorInicial").value(sameNumber(DEFAULT_VALOR_INICIAL)))
            .andExpect(jsonPath("$.valorTotalGastos").value(sameNumber(DEFAULT_VALOR_TOTAL_GASTOS)))
            .andExpect(jsonPath("$.diferencia").value(sameNumber(DEFAULT_DIFERENCIA)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTotalGastos() throws Exception {
        // Get the totalGastos
        restTotalGastosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTotalGastos() throws Exception {
        // Initialize the database
        totalGastosRepository.saveAndFlush(totalGastos);

        int databaseSizeBeforeUpdate = totalGastosRepository.findAll().size();

        // Update the totalGastos
        TotalGastos updatedTotalGastos = totalGastosRepository.findById(totalGastos.getId()).get();
        // Disconnect from session so that the updates on updatedTotalGastos are not directly saved in db
        em.detach(updatedTotalGastos);
        updatedTotalGastos
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .valorInicial(UPDATED_VALOR_INICIAL)
            .valorTotalGastos(UPDATED_VALOR_TOTAL_GASTOS)
            .diferencia(UPDATED_DIFERENCIA)
            .estado(UPDATED_ESTADO);
        TotalGastosDTO totalGastosDTO = totalGastosMapper.toDto(updatedTotalGastos);

        restTotalGastosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, totalGastosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(totalGastosDTO))
            )
            .andExpect(status().isOk());

        // Validate the TotalGastos in the database
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeUpdate);
        TotalGastos testTotalGastos = totalGastosList.get(totalGastosList.size() - 1);
        assertThat(testTotalGastos.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testTotalGastos.getValorInicial()).isEqualByComparingTo(UPDATED_VALOR_INICIAL);
        assertThat(testTotalGastos.getValorTotalGastos()).isEqualByComparingTo(UPDATED_VALOR_TOTAL_GASTOS);
        assertThat(testTotalGastos.getDiferencia()).isEqualByComparingTo(UPDATED_DIFERENCIA);
        assertThat(testTotalGastos.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingTotalGastos() throws Exception {
        int databaseSizeBeforeUpdate = totalGastosRepository.findAll().size();
        totalGastos.setId(count.incrementAndGet());

        // Create the TotalGastos
        TotalGastosDTO totalGastosDTO = totalGastosMapper.toDto(totalGastos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTotalGastosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, totalGastosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(totalGastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TotalGastos in the database
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTotalGastos() throws Exception {
        int databaseSizeBeforeUpdate = totalGastosRepository.findAll().size();
        totalGastos.setId(count.incrementAndGet());

        // Create the TotalGastos
        TotalGastosDTO totalGastosDTO = totalGastosMapper.toDto(totalGastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTotalGastosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(totalGastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TotalGastos in the database
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTotalGastos() throws Exception {
        int databaseSizeBeforeUpdate = totalGastosRepository.findAll().size();
        totalGastos.setId(count.incrementAndGet());

        // Create the TotalGastos
        TotalGastosDTO totalGastosDTO = totalGastosMapper.toDto(totalGastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTotalGastosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(totalGastosDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TotalGastos in the database
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTotalGastosWithPatch() throws Exception {
        // Initialize the database
        totalGastosRepository.saveAndFlush(totalGastos);

        int databaseSizeBeforeUpdate = totalGastosRepository.findAll().size();

        // Update the totalGastos using partial update
        TotalGastos partialUpdatedTotalGastos = new TotalGastos();
        partialUpdatedTotalGastos.setId(totalGastos.getId());

        partialUpdatedTotalGastos.fechaCreacion(UPDATED_FECHA_CREACION).diferencia(UPDATED_DIFERENCIA).estado(UPDATED_ESTADO);

        restTotalGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTotalGastos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTotalGastos))
            )
            .andExpect(status().isOk());

        // Validate the TotalGastos in the database
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeUpdate);
        TotalGastos testTotalGastos = totalGastosList.get(totalGastosList.size() - 1);
        assertThat(testTotalGastos.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testTotalGastos.getValorInicial()).isEqualByComparingTo(DEFAULT_VALOR_INICIAL);
        assertThat(testTotalGastos.getValorTotalGastos()).isEqualByComparingTo(DEFAULT_VALOR_TOTAL_GASTOS);
        assertThat(testTotalGastos.getDiferencia()).isEqualByComparingTo(UPDATED_DIFERENCIA);
        assertThat(testTotalGastos.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateTotalGastosWithPatch() throws Exception {
        // Initialize the database
        totalGastosRepository.saveAndFlush(totalGastos);

        int databaseSizeBeforeUpdate = totalGastosRepository.findAll().size();

        // Update the totalGastos using partial update
        TotalGastos partialUpdatedTotalGastos = new TotalGastos();
        partialUpdatedTotalGastos.setId(totalGastos.getId());

        partialUpdatedTotalGastos
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .valorInicial(UPDATED_VALOR_INICIAL)
            .valorTotalGastos(UPDATED_VALOR_TOTAL_GASTOS)
            .diferencia(UPDATED_DIFERENCIA)
            .estado(UPDATED_ESTADO);

        restTotalGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTotalGastos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTotalGastos))
            )
            .andExpect(status().isOk());

        // Validate the TotalGastos in the database
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeUpdate);
        TotalGastos testTotalGastos = totalGastosList.get(totalGastosList.size() - 1);
        assertThat(testTotalGastos.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testTotalGastos.getValorInicial()).isEqualByComparingTo(UPDATED_VALOR_INICIAL);
        assertThat(testTotalGastos.getValorTotalGastos()).isEqualByComparingTo(UPDATED_VALOR_TOTAL_GASTOS);
        assertThat(testTotalGastos.getDiferencia()).isEqualByComparingTo(UPDATED_DIFERENCIA);
        assertThat(testTotalGastos.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingTotalGastos() throws Exception {
        int databaseSizeBeforeUpdate = totalGastosRepository.findAll().size();
        totalGastos.setId(count.incrementAndGet());

        // Create the TotalGastos
        TotalGastosDTO totalGastosDTO = totalGastosMapper.toDto(totalGastos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTotalGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, totalGastosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(totalGastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TotalGastos in the database
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTotalGastos() throws Exception {
        int databaseSizeBeforeUpdate = totalGastosRepository.findAll().size();
        totalGastos.setId(count.incrementAndGet());

        // Create the TotalGastos
        TotalGastosDTO totalGastosDTO = totalGastosMapper.toDto(totalGastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTotalGastosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(totalGastosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TotalGastos in the database
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTotalGastos() throws Exception {
        int databaseSizeBeforeUpdate = totalGastosRepository.findAll().size();
        totalGastos.setId(count.incrementAndGet());

        // Create the TotalGastos
        TotalGastosDTO totalGastosDTO = totalGastosMapper.toDto(totalGastos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTotalGastosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(totalGastosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TotalGastos in the database
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTotalGastos() throws Exception {
        // Initialize the database
        totalGastosRepository.saveAndFlush(totalGastos);

        int databaseSizeBeforeDelete = totalGastosRepository.findAll().size();

        // Delete the totalGastos
        restTotalGastosMockMvc
            .perform(delete(ENTITY_API_URL_ID, totalGastos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TotalGastos> totalGastosList = totalGastosRepository.findAll();
        assertThat(totalGastosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
