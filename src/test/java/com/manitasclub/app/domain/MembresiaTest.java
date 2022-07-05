package com.manitasclub.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembresiaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Membresia.class);
        Membresia membresia1 = new Membresia();
        membresia1.setId(1L);
        Membresia membresia2 = new Membresia();
        membresia2.setId(membresia1.getId());
        assertThat(membresia1).isEqualTo(membresia2);
        membresia2.setId(2L);
        assertThat(membresia1).isNotEqualTo(membresia2);
        membresia1.setId(null);
        assertThat(membresia1).isNotEqualTo(membresia2);
    }
}
