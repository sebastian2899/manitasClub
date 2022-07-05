package com.manitasclub.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.manitasclub.app.IntegrationTest;
import com.manitasclub.app.domain.Ninio;
import com.manitasclub.app.repository.NinioRepository;
import com.manitasclub.app.service.dto.NinioDTO;
import com.manitasclub.app.service.mapper.NinioMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link NinioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NinioResourceIT {

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_DOUCUMENTO_IDENTIDAD = "AAAAAAAAAA";
    private static final String UPDATED_DOUCUMENTO_IDENTIDAD = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_NACIMIENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_NACIMIENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_EDAD = 1L;
    private static final Long UPDATED_EDAD = 2L;

    private static final Boolean DEFAULT_OBSERVACION = false;
    private static final Boolean UPDATED_OBSERVACION = true;

    private static final String DEFAULT_DESCRIPCION_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION_OBSERVACION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/ninios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NinioRepository ninioRepository;

    @Autowired
    private NinioMapper ninioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNinioMockMvc;

    private Ninio ninio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ninio createEntity(EntityManager em) {
        Ninio ninio = new Ninio()
            .nombres(DEFAULT_NOMBRES)
            .apellidos(DEFAULT_APELLIDOS)
            .doucumentoIdentidad(DEFAULT_DOUCUMENTO_IDENTIDAD)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .edad(DEFAULT_EDAD)
            .observacion(DEFAULT_OBSERVACION)
            .descripcionObservacion(DEFAULT_DESCRIPCION_OBSERVACION)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE);
        return ninio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ninio createUpdatedEntity(EntityManager em) {
        Ninio ninio = new Ninio()
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .doucumentoIdentidad(UPDATED_DOUCUMENTO_IDENTIDAD)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .edad(UPDATED_EDAD)
            .observacion(UPDATED_OBSERVACION)
            .descripcionObservacion(UPDATED_DESCRIPCION_OBSERVACION)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        return ninio;
    }

    @BeforeEach
    public void initTest() {
        ninio = createEntity(em);
    }

    @Test
    @Transactional
    void createNinio() throws Exception {
        int databaseSizeBeforeCreate = ninioRepository.findAll().size();
        // Create the Ninio
        NinioDTO ninioDTO = ninioMapper.toDto(ninio);
        restNinioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ninioDTO)))
            .andExpect(status().isCreated());

        // Validate the Ninio in the database
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeCreate + 1);
        Ninio testNinio = ninioList.get(ninioList.size() - 1);
        assertThat(testNinio.getNombres()).isEqualTo(DEFAULT_NOMBRES);
        assertThat(testNinio.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testNinio.getDoucumentoIdentidad()).isEqualTo(DEFAULT_DOUCUMENTO_IDENTIDAD);
        assertThat(testNinio.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testNinio.getEdad()).isEqualTo(DEFAULT_EDAD);
        assertThat(testNinio.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
        assertThat(testNinio.getDescripcionObservacion()).isEqualTo(DEFAULT_DESCRIPCION_OBSERVACION);
        assertThat(testNinio.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testNinio.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createNinioWithExistingId() throws Exception {
        // Create the Ninio with an existing ID
        ninio.setId(1L);
        NinioDTO ninioDTO = ninioMapper.toDto(ninio);

        int databaseSizeBeforeCreate = ninioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNinioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ninioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ninio in the database
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNinios() throws Exception {
        // Initialize the database
        ninioRepository.saveAndFlush(ninio);

        // Get all the ninioList
        restNinioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ninio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].doucumentoIdentidad").value(hasItem(DEFAULT_DOUCUMENTO_IDENTIDAD)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD.intValue())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION.booleanValue())))
            .andExpect(jsonPath("$.[*].descripcionObservacion").value(hasItem(DEFAULT_DESCRIPCION_OBSERVACION)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))));
    }

    @Test
    @Transactional
    void getNinio() throws Exception {
        // Initialize the database
        ninioRepository.saveAndFlush(ninio);

        // Get the ninio
        restNinioMockMvc
            .perform(get(ENTITY_API_URL_ID, ninio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ninio.getId().intValue()))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.doucumentoIdentidad").value(DEFAULT_DOUCUMENTO_IDENTIDAD))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.edad").value(DEFAULT_EDAD.intValue()))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION.booleanValue()))
            .andExpect(jsonPath("$.descripcionObservacion").value(DEFAULT_DESCRIPCION_OBSERVACION))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)));
    }

    @Test
    @Transactional
    void getNonExistingNinio() throws Exception {
        // Get the ninio
        restNinioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNinio() throws Exception {
        // Initialize the database
        ninioRepository.saveAndFlush(ninio);

        int databaseSizeBeforeUpdate = ninioRepository.findAll().size();

        // Update the ninio
        Ninio updatedNinio = ninioRepository.findById(ninio.getId()).get();
        // Disconnect from session so that the updates on updatedNinio are not directly saved in db
        em.detach(updatedNinio);
        updatedNinio
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .doucumentoIdentidad(UPDATED_DOUCUMENTO_IDENTIDAD)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .edad(UPDATED_EDAD)
            .observacion(UPDATED_OBSERVACION)
            .descripcionObservacion(UPDATED_DESCRIPCION_OBSERVACION)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        NinioDTO ninioDTO = ninioMapper.toDto(updatedNinio);

        restNinioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ninioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ninioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ninio in the database
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeUpdate);
        Ninio testNinio = ninioList.get(ninioList.size() - 1);
        assertThat(testNinio.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testNinio.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testNinio.getDoucumentoIdentidad()).isEqualTo(UPDATED_DOUCUMENTO_IDENTIDAD);
        assertThat(testNinio.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testNinio.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testNinio.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
        assertThat(testNinio.getDescripcionObservacion()).isEqualTo(UPDATED_DESCRIPCION_OBSERVACION);
        assertThat(testNinio.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testNinio.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingNinio() throws Exception {
        int databaseSizeBeforeUpdate = ninioRepository.findAll().size();
        ninio.setId(count.incrementAndGet());

        // Create the Ninio
        NinioDTO ninioDTO = ninioMapper.toDto(ninio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNinioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ninioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ninioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ninio in the database
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNinio() throws Exception {
        int databaseSizeBeforeUpdate = ninioRepository.findAll().size();
        ninio.setId(count.incrementAndGet());

        // Create the Ninio
        NinioDTO ninioDTO = ninioMapper.toDto(ninio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNinioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ninioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ninio in the database
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNinio() throws Exception {
        int databaseSizeBeforeUpdate = ninioRepository.findAll().size();
        ninio.setId(count.incrementAndGet());

        // Create the Ninio
        NinioDTO ninioDTO = ninioMapper.toDto(ninio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNinioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ninioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ninio in the database
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNinioWithPatch() throws Exception {
        // Initialize the database
        ninioRepository.saveAndFlush(ninio);

        int databaseSizeBeforeUpdate = ninioRepository.findAll().size();

        // Update the ninio using partial update
        Ninio partialUpdatedNinio = new Ninio();
        partialUpdatedNinio.setId(ninio.getId());

        partialUpdatedNinio.nombres(UPDATED_NOMBRES).edad(UPDATED_EDAD).descripcionObservacion(UPDATED_DESCRIPCION_OBSERVACION);

        restNinioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNinio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNinio))
            )
            .andExpect(status().isOk());

        // Validate the Ninio in the database
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeUpdate);
        Ninio testNinio = ninioList.get(ninioList.size() - 1);
        assertThat(testNinio.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testNinio.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testNinio.getDoucumentoIdentidad()).isEqualTo(DEFAULT_DOUCUMENTO_IDENTIDAD);
        assertThat(testNinio.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testNinio.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testNinio.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
        assertThat(testNinio.getDescripcionObservacion()).isEqualTo(UPDATED_DESCRIPCION_OBSERVACION);
        assertThat(testNinio.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testNinio.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateNinioWithPatch() throws Exception {
        // Initialize the database
        ninioRepository.saveAndFlush(ninio);

        int databaseSizeBeforeUpdate = ninioRepository.findAll().size();

        // Update the ninio using partial update
        Ninio partialUpdatedNinio = new Ninio();
        partialUpdatedNinio.setId(ninio.getId());

        partialUpdatedNinio
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .doucumentoIdentidad(UPDATED_DOUCUMENTO_IDENTIDAD)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .edad(UPDATED_EDAD)
            .observacion(UPDATED_OBSERVACION)
            .descripcionObservacion(UPDATED_DESCRIPCION_OBSERVACION)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restNinioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNinio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNinio))
            )
            .andExpect(status().isOk());

        // Validate the Ninio in the database
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeUpdate);
        Ninio testNinio = ninioList.get(ninioList.size() - 1);
        assertThat(testNinio.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testNinio.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testNinio.getDoucumentoIdentidad()).isEqualTo(UPDATED_DOUCUMENTO_IDENTIDAD);
        assertThat(testNinio.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testNinio.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testNinio.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
        assertThat(testNinio.getDescripcionObservacion()).isEqualTo(UPDATED_DESCRIPCION_OBSERVACION);
        assertThat(testNinio.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testNinio.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingNinio() throws Exception {
        int databaseSizeBeforeUpdate = ninioRepository.findAll().size();
        ninio.setId(count.incrementAndGet());

        // Create the Ninio
        NinioDTO ninioDTO = ninioMapper.toDto(ninio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNinioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ninioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ninioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ninio in the database
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNinio() throws Exception {
        int databaseSizeBeforeUpdate = ninioRepository.findAll().size();
        ninio.setId(count.incrementAndGet());

        // Create the Ninio
        NinioDTO ninioDTO = ninioMapper.toDto(ninio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNinioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ninioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ninio in the database
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNinio() throws Exception {
        int databaseSizeBeforeUpdate = ninioRepository.findAll().size();
        ninio.setId(count.incrementAndGet());

        // Create the Ninio
        NinioDTO ninioDTO = ninioMapper.toDto(ninio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNinioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ninioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ninio in the database
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNinio() throws Exception {
        // Initialize the database
        ninioRepository.saveAndFlush(ninio);

        int databaseSizeBeforeDelete = ninioRepository.findAll().size();

        // Delete the ninio
        restNinioMockMvc
            .perform(delete(ENTITY_API_URL_ID, ninio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ninio> ninioList = ninioRepository.findAll();
        assertThat(ninioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
