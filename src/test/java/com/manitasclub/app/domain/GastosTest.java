package com.manitasclub.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GastosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gastos.class);
        Gastos gastos1 = new Gastos();
        gastos1.setId(1L);
        Gastos gastos2 = new Gastos();
        gastos2.setId(gastos1.getId());
        assertThat(gastos1).isEqualTo(gastos2);
        gastos2.setId(2L);
        assertThat(gastos1).isNotEqualTo(gastos2);
        gastos1.setId(null);
        assertThat(gastos1).isNotEqualTo(gastos2);
    }
}
