package com.manitasclub.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.manitasclub.app.IntegrationTest;
import com.manitasclub.app.domain.Acudiente;
import com.manitasclub.app.domain.enumeration.TipoIdentificacion;
import com.manitasclub.app.repository.AcudienteRepository;
import com.manitasclub.app.service.dto.AcudienteDTO;
import com.manitasclub.app.service.mapper.AcudienteMapper;
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
 * Integration tests for the {@link AcudienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AcudienteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final TipoIdentificacion DEFAULT_TIPO_IDENTIFIACACION = TipoIdentificacion.CC;
    private static final TipoIdentificacion UPDATED_TIPO_IDENTIFIACACION = TipoIdentificacion.CE;

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PARENTESCO = "AAAAAAAAAA";
    private static final String UPDATED_PARENTESCO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/acudientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcudienteRepository acudienteRepository;

    @Autowired
    private AcudienteMapper acudienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcudienteMockMvc;

    private Acudiente acudiente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acudiente createEntity(EntityManager em) {
        Acudiente acudiente = new Acudiente()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .tipoIdentifiacacion(DEFAULT_TIPO_IDENTIFIACACION)
            .direccion(DEFAULT_DIRECCION)
            .telefono(DEFAULT_TELEFONO)
            .email(DEFAULT_EMAIL)
            .parentesco(DEFAULT_PARENTESCO);
        return acudiente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acudiente createUpdatedEntity(EntityManager em) {
        Acudiente acudiente = new Acudiente()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .tipoIdentifiacacion(UPDATED_TIPO_IDENTIFIACACION)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .parentesco(UPDATED_PARENTESCO);
        return acudiente;
    }

    @BeforeEach
    public void initTest() {
        acudiente = createEntity(em);
    }

    @Test
    @Transactional
    void createAcudiente() throws Exception {
        int databaseSizeBeforeCreate = acudienteRepository.findAll().size();
        // Create the Acudiente
        AcudienteDTO acudienteDTO = acudienteMapper.toDto(acudiente);
        restAcudienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acudienteDTO)))
            .andExpect(status().isCreated());

        // Validate the Acudiente in the database
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeCreate + 1);
        Acudiente testAcudiente = acudienteList.get(acudienteList.size() - 1);
        assertThat(testAcudiente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAcudiente.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testAcudiente.getTipoIdentifiacacion()).isEqualTo(DEFAULT_TIPO_IDENTIFIACACION);
        assertThat(testAcudiente.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testAcudiente.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testAcudiente.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAcudiente.getParentesco()).isEqualTo(DEFAULT_PARENTESCO);
    }

    @Test
    @Transactional
    void createAcudienteWithExistingId() throws Exception {
        // Create the Acudiente with an existing ID
        acudiente.setId(1L);
        AcudienteDTO acudienteDTO = acudienteMapper.toDto(acudiente);

        int databaseSizeBeforeCreate = acudienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcudienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acudienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Acudiente in the database
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAcudientes() throws Exception {
        // Initialize the database
        acudienteRepository.saveAndFlush(acudiente);

        // Get all the acudienteList
        restAcudienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acudiente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].tipoIdentifiacacion").value(hasItem(DEFAULT_TIPO_IDENTIFIACACION.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].parentesco").value(hasItem(DEFAULT_PARENTESCO)));
    }

    @Test
    @Transactional
    void getAcudiente() throws Exception {
        // Initialize the database
        acudienteRepository.saveAndFlush(acudiente);

        // Get the acudiente
        restAcudienteMockMvc
            .perform(get(ENTITY_API_URL_ID, acudiente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(acudiente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.tipoIdentifiacacion").value(DEFAULT_TIPO_IDENTIFIACACION.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.parentesco").value(DEFAULT_PARENTESCO));
    }

    @Test
    @Transactional
    void getNonExistingAcudiente() throws Exception {
        // Get the acudiente
        restAcudienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAcudiente() throws Exception {
        // Initialize the database
        acudienteRepository.saveAndFlush(acudiente);

        int databaseSizeBeforeUpdate = acudienteRepository.findAll().size();

        // Update the acudiente
        Acudiente updatedAcudiente = acudienteRepository.findById(acudiente.getId()).get();
        // Disconnect from session so that the updates on updatedAcudiente are not directly saved in db
        em.detach(updatedAcudiente);
        updatedAcudiente
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .tipoIdentifiacacion(UPDATED_TIPO_IDENTIFIACACION)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .parentesco(UPDATED_PARENTESCO);
        AcudienteDTO acudienteDTO = acudienteMapper.toDto(updatedAcudiente);

        restAcudienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acudienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acudienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Acudiente in the database
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeUpdate);
        Acudiente testAcudiente = acudienteList.get(acudienteList.size() - 1);
        assertThat(testAcudiente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAcudiente.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testAcudiente.getTipoIdentifiacacion()).isEqualTo(UPDATED_TIPO_IDENTIFIACACION);
        assertThat(testAcudiente.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testAcudiente.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testAcudiente.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAcudiente.getParentesco()).isEqualTo(UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void putNonExistingAcudiente() throws Exception {
        int databaseSizeBeforeUpdate = acudienteRepository.findAll().size();
        acudiente.setId(count.incrementAndGet());

        // Create the Acudiente
        AcudienteDTO acudienteDTO = acudienteMapper.toDto(acudiente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcudienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acudienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acudienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acudiente in the database
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcudiente() throws Exception {
        int databaseSizeBeforeUpdate = acudienteRepository.findAll().size();
        acudiente.setId(count.incrementAndGet());

        // Create the Acudiente
        AcudienteDTO acudienteDTO = acudienteMapper.toDto(acudiente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcudienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acudienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acudiente in the database
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcudiente() throws Exception {
        int databaseSizeBeforeUpdate = acudienteRepository.findAll().size();
        acudiente.setId(count.incrementAndGet());

        // Create the Acudiente
        AcudienteDTO acudienteDTO = acudienteMapper.toDto(acudiente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcudienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acudienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acudiente in the database
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcudienteWithPatch() throws Exception {
        // Initialize the database
        acudienteRepository.saveAndFlush(acudiente);

        int databaseSizeBeforeUpdate = acudienteRepository.findAll().size();

        // Update the acudiente using partial update
        Acudiente partialUpdatedAcudiente = new Acudiente();
        partialUpdatedAcudiente.setId(acudiente.getId());

        partialUpdatedAcudiente.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).parentesco(UPDATED_PARENTESCO);

        restAcudienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcudiente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcudiente))
            )
            .andExpect(status().isOk());

        // Validate the Acudiente in the database
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeUpdate);
        Acudiente testAcudiente = acudienteList.get(acudienteList.size() - 1);
        assertThat(testAcudiente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAcudiente.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testAcudiente.getTipoIdentifiacacion()).isEqualTo(DEFAULT_TIPO_IDENTIFIACACION);
        assertThat(testAcudiente.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testAcudiente.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testAcudiente.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAcudiente.getParentesco()).isEqualTo(UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void fullUpdateAcudienteWithPatch() throws Exception {
        // Initialize the database
        acudienteRepository.saveAndFlush(acudiente);

        int databaseSizeBeforeUpdate = acudienteRepository.findAll().size();

        // Update the acudiente using partial update
        Acudiente partialUpdatedAcudiente = new Acudiente();
        partialUpdatedAcudiente.setId(acudiente.getId());

        partialUpdatedAcudiente
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .tipoIdentifiacacion(UPDATED_TIPO_IDENTIFIACACION)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .parentesco(UPDATED_PARENTESCO);

        restAcudienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcudiente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcudiente))
            )
            .andExpect(status().isOk());

        // Validate the Acudiente in the database
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeUpdate);
        Acudiente testAcudiente = acudienteList.get(acudienteList.size() - 1);
        assertThat(testAcudiente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAcudiente.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testAcudiente.getTipoIdentifiacacion()).isEqualTo(UPDATED_TIPO_IDENTIFIACACION);
        assertThat(testAcudiente.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testAcudiente.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testAcudiente.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAcudiente.getParentesco()).isEqualTo(UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void patchNonExistingAcudiente() throws Exception {
        int databaseSizeBeforeUpdate = acudienteRepository.findAll().size();
        acudiente.setId(count.incrementAndGet());

        // Create the Acudiente
        AcudienteDTO acudienteDTO = acudienteMapper.toDto(acudiente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcudienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, acudienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acudienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acudiente in the database
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcudiente() throws Exception {
        int databaseSizeBeforeUpdate = acudienteRepository.findAll().size();
        acudiente.setId(count.incrementAndGet());

        // Create the Acudiente
        AcudienteDTO acudienteDTO = acudienteMapper.toDto(acudiente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcudienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acudienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acudiente in the database
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcudiente() throws Exception {
        int databaseSizeBeforeUpdate = acudienteRepository.findAll().size();
        acudiente.setId(count.incrementAndGet());

        // Create the Acudiente
        AcudienteDTO acudienteDTO = acudienteMapper.toDto(acudiente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcudienteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(acudienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acudiente in the database
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcudiente() throws Exception {
        // Initialize the database
        acudienteRepository.saveAndFlush(acudiente);

        int databaseSizeBeforeDelete = acudienteRepository.findAll().size();

        // Delete the acudiente
        restAcudienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, acudiente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Acudiente> acudienteList = acudienteRepository.findAll();
        assertThat(acudienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
