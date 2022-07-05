package com.manitasclub.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcudienteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcudienteDTO.class);
        AcudienteDTO acudienteDTO1 = new AcudienteDTO();
        acudienteDTO1.setId(1L);
        AcudienteDTO acudienteDTO2 = new AcudienteDTO();
        assertThat(acudienteDTO1).isNotEqualTo(acudienteDTO2);
        acudienteDTO2.setId(acudienteDTO1.getId());
        assertThat(acudienteDTO1).isEqualTo(acudienteDTO2);
        acudienteDTO2.setId(2L);
        assertThat(acudienteDTO1).isNotEqualTo(acudienteDTO2);
        acudienteDTO1.setId(null);
        assertThat(acudienteDTO1).isNotEqualTo(acudienteDTO2);
    }
}
