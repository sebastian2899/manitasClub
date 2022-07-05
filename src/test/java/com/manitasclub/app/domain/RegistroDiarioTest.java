package com.manitasclub.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegistroDiarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistroDiario.class);
        RegistroDiario registroDiario1 = new RegistroDiario();
        registroDiario1.setId(1L);
        RegistroDiario registroDiario2 = new RegistroDiario();
        registroDiario2.setId(registroDiario1.getId());
        assertThat(registroDiario1).isEqualTo(registroDiario2);
        registroDiario2.setId(2L);
        assertThat(registroDiario1).isNotEqualTo(registroDiario2);
        registroDiario1.setId(null);
        assertThat(registroDiario1).isNotEqualTo(registroDiario2);
    }
}
