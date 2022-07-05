package com.manitasclub.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegistroDiarioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistroDiarioDTO.class);
        RegistroDiarioDTO registroDiarioDTO1 = new RegistroDiarioDTO();
        registroDiarioDTO1.setId(1L);
        RegistroDiarioDTO registroDiarioDTO2 = new RegistroDiarioDTO();
        assertThat(registroDiarioDTO1).isNotEqualTo(registroDiarioDTO2);
        registroDiarioDTO2.setId(registroDiarioDTO1.getId());
        assertThat(registroDiarioDTO1).isEqualTo(registroDiarioDTO2);
        registroDiarioDTO2.setId(2L);
        assertThat(registroDiarioDTO1).isNotEqualTo(registroDiarioDTO2);
        registroDiarioDTO1.setId(null);
        assertThat(registroDiarioDTO1).isNotEqualTo(registroDiarioDTO2);
    }
}
