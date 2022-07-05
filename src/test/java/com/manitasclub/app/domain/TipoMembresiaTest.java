package com.manitasclub.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoMembresiaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoMembresia.class);
        TipoMembresia tipoMembresia1 = new TipoMembresia();
        tipoMembresia1.setId(1L);
        TipoMembresia tipoMembresia2 = new TipoMembresia();
        tipoMembresia2.setId(tipoMembresia1.getId());
        assertThat(tipoMembresia1).isEqualTo(tipoMembresia2);
        tipoMembresia2.setId(2L);
        assertThat(tipoMembresia1).isNotEqualTo(tipoMembresia2);
        tipoMembresia1.setId(null);
        assertThat(tipoMembresia1).isNotEqualTo(tipoMembresia2);
    }
}
