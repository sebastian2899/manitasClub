package com.manitasclub.app.web.rest;

import static com.manitasclub.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.manitasclub.app.IntegrationTest;
import com.manitasclub.app.domain.RegistroDiario;
import com.manitasclub.app.repository.RegistroDiarioRepository;
import com.manitasclub.app.service.dto.RegistroDiarioDTO;
import com.manitasclub.app.service.mapper.RegistroDiarioMapper;
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
 * Integration tests for the {@link RegistroDiarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegistroDiarioResourceIT {

    private static final String DEFAULT_NOMBRE_NINIO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_NINIO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_ACUDIENTE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_ACUDIENTE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_ACUDIENTE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_ACUDIENTE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);

    private static final Instant DEFAULT_FECHA_INGRESO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INGRESO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_HORA_ENTRADA = "AAAAAAAAAA";
    private static final String UPDATED_HORA_ENTRADA = "BBBBBBBBBB";

    private static final String DEFAULT_HORA_SALIDA = "AAAAAAAAAA";
    private static final String UPDATED_HORA_SALIDA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/registro-diarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private RegistroDiarioMapper registroDiarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegistroDiarioMockMvc;

    private RegistroDiario registroDiario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistroDiario createEntity(EntityManager em) {
        RegistroDiario registroDiario = new RegistroDiario()
            .nombreNinio(DEFAULT_NOMBRE_NINIO)
            .nombreAcudiente(DEFAULT_NOMBRE_ACUDIENTE)
            .telefonoAcudiente(DEFAULT_TELEFONO_ACUDIENTE)
            .valor(DEFAULT_VALOR)
            .fechaIngreso(DEFAULT_FECHA_INGRESO)
            .horaEntrada(DEFAULT_HORA_ENTRADA)
            .horaSalida(DEFAULT_HORA_SALIDA);
        return registroDiario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistroDiario createUpdatedEntity(EntityManager em) {
        RegistroDiario registroDiario = new RegistroDiario()
            .nombreNinio(UPDATED_NOMBRE_NINIO)
            .nombreAcudiente(UPDATED_NOMBRE_ACUDIENTE)
            .telefonoAcudiente(UPDATED_TELEFONO_ACUDIENTE)
            .valor(UPDATED_VALOR)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .horaEntrada(UPDATED_HORA_ENTRADA)
            .horaSalida(UPDATED_HORA_SALIDA);
        return registroDiario;
    }

    @BeforeEach
    public void initTest() {
        registroDiario = createEntity(em);
    }

    @Test
    @Transactional
    void createRegistroDiario() throws Exception {
        int databaseSizeBeforeCreate = registroDiarioRepository.findAll().size();
        // Create the RegistroDiario
        RegistroDiarioDTO registroDiarioDTO = registroDiarioMapper.toDto(registroDiario);
        restRegistroDiarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroDiarioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RegistroDiario in the database
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeCreate + 1);
        RegistroDiario testRegistroDiario = registroDiarioList.get(registroDiarioList.size() - 1);
        assertThat(testRegistroDiario.getNombreNinio()).isEqualTo(DEFAULT_NOMBRE_NINIO);
        assertThat(testRegistroDiario.getNombreAcudiente()).isEqualTo(DEFAULT_NOMBRE_ACUDIENTE);
        assertThat(testRegistroDiario.getTelefonoAcudiente()).isEqualTo(DEFAULT_TELEFONO_ACUDIENTE);
        assertThat(testRegistroDiario.getValor()).isEqualByComparingTo(DEFAULT_VALOR);
        assertThat(testRegistroDiario.getFechaIngreso()).isEqualTo(DEFAULT_FECHA_INGRESO);
        assertThat(testRegistroDiario.getHoraEntrada()).isEqualTo(DEFAULT_HORA_ENTRADA);
        assertThat(testRegistroDiario.getHoraSalida()).isEqualTo(DEFAULT_HORA_SALIDA);
    }

    @Test
    @Transactional
    void createRegistroDiarioWithExistingId() throws Exception {
        // Create the RegistroDiario with an existing ID
        registroDiario.setId(1L);
        RegistroDiarioDTO registroDiarioDTO = registroDiarioMapper.toDto(registroDiario);

        int databaseSizeBeforeCreate = registroDiarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistroDiarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroDiarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroDiario in the database
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRegistroDiarios() throws Exception {
        // Initialize the database
        registroDiarioRepository.saveAndFlush(registroDiario);

        // Get all the registroDiarioList
        restRegistroDiarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registroDiario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreNinio").value(hasItem(DEFAULT_NOMBRE_NINIO)))
            .andExpect(jsonPath("$.[*].nombreAcudiente").value(hasItem(DEFAULT_NOMBRE_ACUDIENTE)))
            .andExpect(jsonPath("$.[*].telefonoAcudiente").value(hasItem(DEFAULT_TELEFONO_ACUDIENTE)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].horaEntrada").value(hasItem(DEFAULT_HORA_ENTRADA)))
            .andExpect(jsonPath("$.[*].horaSalida").value(hasItem(DEFAULT_HORA_SALIDA)));
    }

    @Test
    @Transactional
    void getRegistroDiario() throws Exception {
        // Initialize the database
        registroDiarioRepository.saveAndFlush(registroDiario);

        // Get the registroDiario
        restRegistroDiarioMockMvc
            .perform(get(ENTITY_API_URL_ID, registroDiario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(registroDiario.getId().intValue()))
            .andExpect(jsonPath("$.nombreNinio").value(DEFAULT_NOMBRE_NINIO))
            .andExpect(jsonPath("$.nombreAcudiente").value(DEFAULT_NOMBRE_ACUDIENTE))
            .andExpect(jsonPath("$.telefonoAcudiente").value(DEFAULT_TELEFONO_ACUDIENTE))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.fechaIngreso").value(DEFAULT_FECHA_INGRESO.toString()))
            .andExpect(jsonPath("$.horaEntrada").value(DEFAULT_HORA_ENTRADA))
            .andExpect(jsonPath("$.horaSalida").value(DEFAULT_HORA_SALIDA));
    }

    @Test
    @Transactional
    void getNonExistingRegistroDiario() throws Exception {
        // Get the registroDiario
        restRegistroDiarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRegistroDiario() throws Exception {
        // Initialize the database
        registroDiarioRepository.saveAndFlush(registroDiario);

        int databaseSizeBeforeUpdate = registroDiarioRepository.findAll().size();

        // Update the registroDiario
        RegistroDiario updatedRegistroDiario = registroDiarioRepository.findById(registroDiario.getId()).get();
        // Disconnect from session so that the updates on updatedRegistroDiario are not directly saved in db
        em.detach(updatedRegistroDiario);
        updatedRegistroDiario
            .nombreNinio(UPDATED_NOMBRE_NINIO)
            .nombreAcudiente(UPDATED_NOMBRE_ACUDIENTE)
            .telefonoAcudiente(UPDATED_TELEFONO_ACUDIENTE)
            .valor(UPDATED_VALOR)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .horaEntrada(UPDATED_HORA_ENTRADA)
            .horaSalida(UPDATED_HORA_SALIDA);
        RegistroDiarioDTO registroDiarioDTO = registroDiarioMapper.toDto(updatedRegistroDiario);

        restRegistroDiarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registroDiarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroDiarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the RegistroDiario in the database
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeUpdate);
        RegistroDiario testRegistroDiario = registroDiarioList.get(registroDiarioList.size() - 1);
        assertThat(testRegistroDiario.getNombreNinio()).isEqualTo(UPDATED_NOMBRE_NINIO);
        assertThat(testRegistroDiario.getNombreAcudiente()).isEqualTo(UPDATED_NOMBRE_ACUDIENTE);
        assertThat(testRegistroDiario.getTelefonoAcudiente()).isEqualTo(UPDATED_TELEFONO_ACUDIENTE);
        assertThat(testRegistroDiario.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testRegistroDiario.getFechaIngreso()).isEqualTo(UPDATED_FECHA_INGRESO);
        assertThat(testRegistroDiario.getHoraEntrada()).isEqualTo(UPDATED_HORA_ENTRADA);
        assertThat(testRegistroDiario.getHoraSalida()).isEqualTo(UPDATED_HORA_SALIDA);
    }

    @Test
    @Transactional
    void putNonExistingRegistroDiario() throws Exception {
        int databaseSizeBeforeUpdate = registroDiarioRepository.findAll().size();
        registroDiario.setId(count.incrementAndGet());

        // Create the RegistroDiario
        RegistroDiarioDTO registroDiarioDTO = registroDiarioMapper.toDto(registroDiario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistroDiarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registroDiarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroDiarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroDiario in the database
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegistroDiario() throws Exception {
        int databaseSizeBeforeUpdate = registroDiarioRepository.findAll().size();
        registroDiario.setId(count.incrementAndGet());

        // Create the RegistroDiario
        RegistroDiarioDTO registroDiarioDTO = registroDiarioMapper.toDto(registroDiario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroDiarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroDiarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroDiario in the database
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegistroDiario() throws Exception {
        int databaseSizeBeforeUpdate = registroDiarioRepository.findAll().size();
        registroDiario.setId(count.incrementAndGet());

        // Create the RegistroDiario
        RegistroDiarioDTO registroDiarioDTO = registroDiarioMapper.toDto(registroDiario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroDiarioMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroDiarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegistroDiario in the database
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegistroDiarioWithPatch() throws Exception {
        // Initialize the database
        registroDiarioRepository.saveAndFlush(registroDiario);

        int databaseSizeBeforeUpdate = registroDiarioRepository.findAll().size();

        // Update the registroDiario using partial update
        RegistroDiario partialUpdatedRegistroDiario = new RegistroDiario();
        partialUpdatedRegistroDiario.setId(registroDiario.getId());

        partialUpdatedRegistroDiario
            .nombreNinio(UPDATED_NOMBRE_NINIO)
            .valor(UPDATED_VALOR)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .horaEntrada(UPDATED_HORA_ENTRADA)
            .horaSalida(UPDATED_HORA_SALIDA);

        restRegistroDiarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistroDiario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistroDiario))
            )
            .andExpect(status().isOk());

        // Validate the RegistroDiario in the database
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeUpdate);
        RegistroDiario testRegistroDiario = registroDiarioList.get(registroDiarioList.size() - 1);
        assertThat(testRegistroDiario.getNombreNinio()).isEqualTo(UPDATED_NOMBRE_NINIO);
        assertThat(testRegistroDiario.getNombreAcudiente()).isEqualTo(DEFAULT_NOMBRE_ACUDIENTE);
        assertThat(testRegistroDiario.getTelefonoAcudiente()).isEqualTo(DEFAULT_TELEFONO_ACUDIENTE);
        assertThat(testRegistroDiario.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testRegistroDiario.getFechaIngreso()).isEqualTo(UPDATED_FECHA_INGRESO);
        assertThat(testRegistroDiario.getHoraEntrada()).isEqualTo(UPDATED_HORA_ENTRADA);
        assertThat(testRegistroDiario.getHoraSalida()).isEqualTo(UPDATED_HORA_SALIDA);
    }

    @Test
    @Transactional
    void fullUpdateRegistroDiarioWithPatch() throws Exception {
        // Initialize the database
        registroDiarioRepository.saveAndFlush(registroDiario);

        int databaseSizeBeforeUpdate = registroDiarioRepository.findAll().size();

        // Update the registroDiario using partial update
        RegistroDiario partialUpdatedRegistroDiario = new RegistroDiario();
        partialUpdatedRegistroDiario.setId(registroDiario.getId());

        partialUpdatedRegistroDiario
            .nombreNinio(UPDATED_NOMBRE_NINIO)
            .nombreAcudiente(UPDATED_NOMBRE_ACUDIENTE)
            .telefonoAcudiente(UPDATED_TELEFONO_ACUDIENTE)
            .valor(UPDATED_VALOR)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .horaEntrada(UPDATED_HORA_ENTRADA)
            .horaSalida(UPDATED_HORA_SALIDA);

        restRegistroDiarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistroDiario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistroDiario))
            )
            .andExpect(status().isOk());

        // Validate the RegistroDiario in the database
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeUpdate);
        RegistroDiario testRegistroDiario = registroDiarioList.get(registroDiarioList.size() - 1);
        assertThat(testRegistroDiario.getNombreNinio()).isEqualTo(UPDATED_NOMBRE_NINIO);
        assertThat(testRegistroDiario.getNombreAcudiente()).isEqualTo(UPDATED_NOMBRE_ACUDIENTE);
        assertThat(testRegistroDiario.getTelefonoAcudiente()).isEqualTo(UPDATED_TELEFONO_ACUDIENTE);
        assertThat(testRegistroDiario.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testRegistroDiario.getFechaIngreso()).isEqualTo(UPDATED_FECHA_INGRESO);
        assertThat(testRegistroDiario.getHoraEntrada()).isEqualTo(UPDATED_HORA_ENTRADA);
        assertThat(testRegistroDiario.getHoraSalida()).isEqualTo(UPDATED_HORA_SALIDA);
    }

    @Test
    @Transactional
    void patchNonExistingRegistroDiario() throws Exception {
        int databaseSizeBeforeUpdate = registroDiarioRepository.findAll().size();
        registroDiario.setId(count.incrementAndGet());

        // Create the RegistroDiario
        RegistroDiarioDTO registroDiarioDTO = registroDiarioMapper.toDto(registroDiario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistroDiarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, registroDiarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registroDiarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroDiario in the database
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegistroDiario() throws Exception {
        int databaseSizeBeforeUpdate = registroDiarioRepository.findAll().size();
        registroDiario.setId(count.incrementAndGet());

        // Create the RegistroDiario
        RegistroDiarioDTO registroDiarioDTO = registroDiarioMapper.toDto(registroDiario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroDiarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registroDiarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroDiario in the database
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegistroDiario() throws Exception {
        int databaseSizeBeforeUpdate = registroDiarioRepository.findAll().size();
        registroDiario.setId(count.incrementAndGet());

        // Create the RegistroDiario
        RegistroDiarioDTO registroDiarioDTO = registroDiarioMapper.toDto(registroDiario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroDiarioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registroDiarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegistroDiario in the database
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegistroDiario() throws Exception {
        // Initialize the database
        registroDiarioRepository.saveAndFlush(registroDiario);

        int databaseSizeBeforeDelete = registroDiarioRepository.findAll().size();

        // Delete the registroDiario
        restRegistroDiarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, registroDiario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RegistroDiario> registroDiarioList = registroDiarioRepository.findAll();
        assertThat(registroDiarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
