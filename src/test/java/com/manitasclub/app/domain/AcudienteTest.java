package com.manitasclub.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcudienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Acudiente.class);
        Acudiente acudiente1 = new Acudiente();
        acudiente1.setId(1L);
        Acudiente acudiente2 = new Acudiente();
        acudiente2.setId(acudiente1.getId());
        assertThat(acudiente1).isEqualTo(acudiente2);
        acudiente2.setId(2L);
        assertThat(acudiente1).isNotEqualTo(acudiente2);
        acudiente1.setId(null);
        assertThat(acudiente1).isNotEqualTo(acudiente2);
    }
}
