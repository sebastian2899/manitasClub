package com.manitasclub.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NinioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ninio.class);
        Ninio ninio1 = new Ninio();
        ninio1.setId(1L);
        Ninio ninio2 = new Ninio();
        ninio2.setId(ninio1.getId());
        assertThat(ninio1).isEqualTo(ninio2);
        ninio2.setId(2L);
        assertThat(ninio1).isNotEqualTo(ninio2);
        ninio1.setId(null);
        assertThat(ninio1).isNotEqualTo(ninio2);
    }
}
