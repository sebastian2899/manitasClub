package com.manitasclub.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.manitasclub.app.IntegrationTest;
import com.manitasclub.app.domain.Membresia;
import com.manitasclub.app.domain.enumeration.EstadoMembresia;
import com.manitasclub.app.repository.MembresiaRepository;
import com.manitasclub.app.service.dto.MembresiaDTO;
import com.manitasclub.app.service.mapper.MembresiaMapper;
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
 * Integration tests for the {@link MembresiaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MembresiaResourceIT {

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_CANTIDAD = 1L;
    private static final Long UPDATED_CANTIDAD = 2L;

    private static final EstadoMembresia DEFAULT_ESTADO = EstadoMembresia.VENCIDA;
    private static final EstadoMembresia UPDATED_ESTADO = EstadoMembresia.ACTIVA;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/membresias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MembresiaRepository membresiaRepository;

    @Autowired
    private MembresiaMapper membresiaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembresiaMockMvc;

    private Membresia membresia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Membresia createEntity(EntityManager em) {
        Membresia membresia = new Membresia()
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .cantidad(DEFAULT_CANTIDAD)
            .estado(DEFAULT_ESTADO)
            .descripcion(DEFAULT_DESCRIPCION);
        return membresia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Membresia createUpdatedEntity(EntityManager em) {
        Membresia membresia = new Membresia()
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .cantidad(UPDATED_CANTIDAD)
            .estado(UPDATED_ESTADO)
            .descripcion(UPDATED_DESCRIPCION);
        return membresia;
    }

    @BeforeEach
    public void initTest() {
        membresia = createEntity(em);
    }

    @Test
    @Transactional
    void createMembresia() throws Exception {
        int databaseSizeBeforeCreate = membresiaRepository.findAll().size();
        // Create the Membresia
        MembresiaDTO membresiaDTO = membresiaMapper.toDto(membresia);
        restMembresiaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membresiaDTO)))
            .andExpect(status().isCreated());

        // Validate the Membresia in the database
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeCreate + 1);
        Membresia testMembresia = membresiaList.get(membresiaList.size() - 1);
        assertThat(testMembresia.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testMembresia.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testMembresia.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testMembresia.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testMembresia.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testMembresia.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createMembresiaWithExistingId() throws Exception {
        // Create the Membresia with an existing ID
        membresia.setId(1L);
        MembresiaDTO membresiaDTO = membresiaMapper.toDto(membresia);

        int databaseSizeBeforeCreate = membresiaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembresiaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membresiaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Membresia in the database
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMembresias() throws Exception {
        // Initialize the database
        membresiaRepository.saveAndFlush(membresia);

        // Get all the membresiaList
        restMembresiaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membresia.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getMembresia() throws Exception {
        // Initialize the database
        membresiaRepository.saveAndFlush(membresia);

        // Get the membresia
        restMembresiaMockMvc
            .perform(get(ENTITY_API_URL_ID, membresia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membresia.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingMembresia() throws Exception {
        // Get the membresia
        restMembresiaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMembresia() throws Exception {
        // Initialize the database
        membresiaRepository.saveAndFlush(membresia);

        int databaseSizeBeforeUpdate = membresiaRepository.findAll().size();

        // Update the membresia
        Membresia updatedMembresia = membresiaRepository.findById(membresia.getId()).get();
        // Disconnect from session so that the updates on updatedMembresia are not directly saved in db
        em.detach(updatedMembresia);
        updatedMembresia
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .cantidad(UPDATED_CANTIDAD)
            .estado(UPDATED_ESTADO)
            .descripcion(UPDATED_DESCRIPCION);
        MembresiaDTO membresiaDTO = membresiaMapper.toDto(updatedMembresia);

        restMembresiaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membresiaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membresiaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Membresia in the database
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeUpdate);
        Membresia testMembresia = membresiaList.get(membresiaList.size() - 1);
        assertThat(testMembresia.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testMembresia.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testMembresia.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testMembresia.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testMembresia.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testMembresia.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingMembresia() throws Exception {
        int databaseSizeBeforeUpdate = membresiaRepository.findAll().size();
        membresia.setId(count.incrementAndGet());

        // Create the Membresia
        MembresiaDTO membresiaDTO = membresiaMapper.toDto(membresia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembresiaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membresiaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membresiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membresia in the database
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMembresia() throws Exception {
        int databaseSizeBeforeUpdate = membresiaRepository.findAll().size();
        membresia.setId(count.incrementAndGet());

        // Create the Membresia
        MembresiaDTO membresiaDTO = membresiaMapper.toDto(membresia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembresiaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membresiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membresia in the database
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMembresia() throws Exception {
        int databaseSizeBeforeUpdate = membresiaRepository.findAll().size();
        membresia.setId(count.incrementAndGet());

        // Create the Membresia
        MembresiaDTO membresiaDTO = membresiaMapper.toDto(membresia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembresiaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membresiaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Membresia in the database
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMembresiaWithPatch() throws Exception {
        // Initialize the database
        membresiaRepository.saveAndFlush(membresia);

        int databaseSizeBeforeUpdate = membresiaRepository.findAll().size();

        // Update the membresia using partial update
        Membresia partialUpdatedMembresia = new Membresia();
        partialUpdatedMembresia.setId(membresia.getId());

        partialUpdatedMembresia.fechaCreacion(UPDATED_FECHA_CREACION).fechaFin(UPDATED_FECHA_FIN).descripcion(UPDATED_DESCRIPCION);

        restMembresiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembresia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembresia))
            )
            .andExpect(status().isOk());

        // Validate the Membresia in the database
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeUpdate);
        Membresia testMembresia = membresiaList.get(membresiaList.size() - 1);
        assertThat(testMembresia.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testMembresia.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testMembresia.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testMembresia.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testMembresia.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testMembresia.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateMembresiaWithPatch() throws Exception {
        // Initialize the database
        membresiaRepository.saveAndFlush(membresia);

        int databaseSizeBeforeUpdate = membresiaRepository.findAll().size();

        // Update the membresia using partial update
        Membresia partialUpdatedMembresia = new Membresia();
        partialUpdatedMembresia.setId(membresia.getId());

        partialUpdatedMembresia
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .cantidad(UPDATED_CANTIDAD)
            .estado(UPDATED_ESTADO)
            .descripcion(UPDATED_DESCRIPCION);

        restMembresiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembresia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembresia))
            )
            .andExpect(status().isOk());

        // Validate the Membresia in the database
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeUpdate);
        Membresia testMembresia = membresiaList.get(membresiaList.size() - 1);
        assertThat(testMembresia.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testMembresia.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testMembresia.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testMembresia.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testMembresia.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testMembresia.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingMembresia() throws Exception {
        int databaseSizeBeforeUpdate = membresiaRepository.findAll().size();
        membresia.setId(count.incrementAndGet());

        // Create the Membresia
        MembresiaDTO membresiaDTO = membresiaMapper.toDto(membresia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembresiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, membresiaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(membresiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membresia in the database
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMembresia() throws Exception {
        int databaseSizeBeforeUpdate = membresiaRepository.findAll().size();
        membresia.setId(count.incrementAndGet());

        // Create the Membresia
        MembresiaDTO membresiaDTO = membresiaMapper.toDto(membresia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembresiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(membresiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membresia in the database
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMembresia() throws Exception {
        int databaseSizeBeforeUpdate = membresiaRepository.findAll().size();
        membresia.setId(count.incrementAndGet());

        // Create the Membresia
        MembresiaDTO membresiaDTO = membresiaMapper.toDto(membresia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembresiaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(membresiaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Membresia in the database
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMembresia() throws Exception {
        // Initialize the database
        membresiaRepository.saveAndFlush(membresia);

        int databaseSizeBeforeDelete = membresiaRepository.findAll().size();

        // Delete the membresia
        restMembresiaMockMvc
            .perform(delete(ENTITY_API_URL_ID, membresia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Membresia> membresiaList = membresiaRepository.findAll();
        assertThat(membresiaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
