package com.manitasclub.app.web.rest;

import static com.manitasclub.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.manitasclub.app.IntegrationTest;
import com.manitasclub.app.domain.Abono;
import com.manitasclub.app.repository.AbonoRepository;
import com.manitasclub.app.service.dto.AbonoDTO;
import com.manitasclub.app.service.mapper.AbonoMapper;
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
 * Integration tests for the {@link AbonoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AbonoResourceIT {

    private static final Long DEFAULT_ID_MEMBRESIA = 1L;
    private static final Long UPDATED_ID_MEMBRESIA = 2L;

    private static final Instant DEFAULT_FECHA_ABONO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_ABONO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_VALOR_ABONO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_ABONO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/abonos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AbonoRepository abonoRepository;

    @Autowired
    private AbonoMapper abonoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAbonoMockMvc;

    private Abono abono;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abono createEntity(EntityManager em) {
        Abono abono = new Abono().idMembresia(DEFAULT_ID_MEMBRESIA).fechaAbono(DEFAULT_FECHA_ABONO).valorAbono(DEFAULT_VALOR_ABONO);
        return abono;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abono createUpdatedEntity(EntityManager em) {
        Abono abono = new Abono().idMembresia(UPDATED_ID_MEMBRESIA).fechaAbono(UPDATED_FECHA_ABONO).valorAbono(UPDATED_VALOR_ABONO);
        return abono;
    }

    @BeforeEach
    public void initTest() {
        abono = createEntity(em);
    }

    @Test
    @Transactional
    void createAbono() throws Exception {
        int databaseSizeBeforeCreate = abonoRepository.findAll().size();
        // Create the Abono
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);
        restAbonoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonoDTO)))
            .andExpect(status().isCreated());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeCreate + 1);
        Abono testAbono = abonoList.get(abonoList.size() - 1);
        assertThat(testAbono.getIdMembresia()).isEqualTo(DEFAULT_ID_MEMBRESIA);
        assertThat(testAbono.getFechaAbono()).isEqualTo(DEFAULT_FECHA_ABONO);
        assertThat(testAbono.getValorAbono()).isEqualByComparingTo(DEFAULT_VALOR_ABONO);
    }

    @Test
    @Transactional
    void createAbonoWithExistingId() throws Exception {
        // Create the Abono with an existing ID
        abono.setId(1L);
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);

        int databaseSizeBeforeCreate = abonoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbonoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAbonos() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get all the abonoList
        restAbonoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abono.getId().intValue())))
            .andExpect(jsonPath("$.[*].idMembresia").value(hasItem(DEFAULT_ID_MEMBRESIA.intValue())))
            .andExpect(jsonPath("$.[*].fechaAbono").value(hasItem(DEFAULT_FECHA_ABONO.toString())))
            .andExpect(jsonPath("$.[*].valorAbono").value(hasItem(sameNumber(DEFAULT_VALOR_ABONO))));
    }

    @Test
    @Transactional
    void getAbono() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        // Get the abono
        restAbonoMockMvc
            .perform(get(ENTITY_API_URL_ID, abono.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(abono.getId().intValue()))
            .andExpect(jsonPath("$.idMembresia").value(DEFAULT_ID_MEMBRESIA.intValue()))
            .andExpect(jsonPath("$.fechaAbono").value(DEFAULT_FECHA_ABONO.toString()))
            .andExpect(jsonPath("$.valorAbono").value(sameNumber(DEFAULT_VALOR_ABONO)));
    }

    @Test
    @Transactional
    void getNonExistingAbono() throws Exception {
        // Get the abono
        restAbonoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAbono() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        int databaseSizeBeforeUpdate = abonoRepository.findAll().size();

        // Update the abono
        Abono updatedAbono = abonoRepository.findById(abono.getId()).get();
        // Disconnect from session so that the updates on updatedAbono are not directly saved in db
        em.detach(updatedAbono);
        updatedAbono.idMembresia(UPDATED_ID_MEMBRESIA).fechaAbono(UPDATED_FECHA_ABONO).valorAbono(UPDATED_VALOR_ABONO);
        AbonoDTO abonoDTO = abonoMapper.toDto(updatedAbono);

        restAbonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, abonoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeUpdate);
        Abono testAbono = abonoList.get(abonoList.size() - 1);
        assertThat(testAbono.getIdMembresia()).isEqualTo(UPDATED_ID_MEMBRESIA);
        assertThat(testAbono.getFechaAbono()).isEqualTo(UPDATED_FECHA_ABONO);
        assertThat(testAbono.getValorAbono()).isEqualByComparingTo(UPDATED_VALOR_ABONO);
    }

    @Test
    @Transactional
    void putNonExistingAbono() throws Exception {
        int databaseSizeBeforeUpdate = abonoRepository.findAll().size();
        abono.setId(count.incrementAndGet());

        // Create the Abono
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, abonoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAbono() throws Exception {
        int databaseSizeBeforeUpdate = abonoRepository.findAll().size();
        abono.setId(count.incrementAndGet());

        // Create the Abono
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAbono() throws Exception {
        int databaseSizeBeforeUpdate = abonoRepository.findAll().size();
        abono.setId(count.incrementAndGet());

        // Create the Abono
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAbonoWithPatch() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        int databaseSizeBeforeUpdate = abonoRepository.findAll().size();

        // Update the abono using partial update
        Abono partialUpdatedAbono = new Abono();
        partialUpdatedAbono.setId(abono.getId());

        partialUpdatedAbono.idMembresia(UPDATED_ID_MEMBRESIA);

        restAbonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbono.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbono))
            )
            .andExpect(status().isOk());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeUpdate);
        Abono testAbono = abonoList.get(abonoList.size() - 1);
        assertThat(testAbono.getIdMembresia()).isEqualTo(UPDATED_ID_MEMBRESIA);
        assertThat(testAbono.getFechaAbono()).isEqualTo(DEFAULT_FECHA_ABONO);
        assertThat(testAbono.getValorAbono()).isEqualByComparingTo(DEFAULT_VALOR_ABONO);
    }

    @Test
    @Transactional
    void fullUpdateAbonoWithPatch() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        int databaseSizeBeforeUpdate = abonoRepository.findAll().size();

        // Update the abono using partial update
        Abono partialUpdatedAbono = new Abono();
        partialUpdatedAbono.setId(abono.getId());

        partialUpdatedAbono.idMembresia(UPDATED_ID_MEMBRESIA).fechaAbono(UPDATED_FECHA_ABONO).valorAbono(UPDATED_VALOR_ABONO);

        restAbonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbono.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbono))
            )
            .andExpect(status().isOk());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeUpdate);
        Abono testAbono = abonoList.get(abonoList.size() - 1);
        assertThat(testAbono.getIdMembresia()).isEqualTo(UPDATED_ID_MEMBRESIA);
        assertThat(testAbono.getFechaAbono()).isEqualTo(UPDATED_FECHA_ABONO);
        assertThat(testAbono.getValorAbono()).isEqualByComparingTo(UPDATED_VALOR_ABONO);
    }

    @Test
    @Transactional
    void patchNonExistingAbono() throws Exception {
        int databaseSizeBeforeUpdate = abonoRepository.findAll().size();
        abono.setId(count.incrementAndGet());

        // Create the Abono
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, abonoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(abonoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAbono() throws Exception {
        int databaseSizeBeforeUpdate = abonoRepository.findAll().size();
        abono.setId(count.incrementAndGet());

        // Create the Abono
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(abonoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAbono() throws Exception {
        int databaseSizeBeforeUpdate = abonoRepository.findAll().size();
        abono.setId(count.incrementAndGet());

        // Create the Abono
        AbonoDTO abonoDTO = abonoMapper.toDto(abono);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(abonoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Abono in the database
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAbono() throws Exception {
        // Initialize the database
        abonoRepository.saveAndFlush(abono);

        int databaseSizeBeforeDelete = abonoRepository.findAll().size();

        // Delete the abono
        restAbonoMockMvc
            .perform(delete(ENTITY_API_URL_ID, abono.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Abono> abonoList = abonoRepository.findAll();
        assertThat(abonoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
