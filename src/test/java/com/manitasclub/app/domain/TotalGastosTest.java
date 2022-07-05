package com.manitasclub.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TotalGastosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TotalGastos.class);
        TotalGastos totalGastos1 = new TotalGastos();
        totalGastos1.setId(1L);
        TotalGastos totalGastos2 = new TotalGastos();
        totalGastos2.setId(totalGastos1.getId());
        assertThat(totalGastos1).isEqualTo(totalGastos2);
        totalGastos2.setId(2L);
        assertThat(totalGastos1).isNotEqualTo(totalGastos2);
        totalGastos1.setId(null);
        assertThat(totalGastos1).isNotEqualTo(totalGastos2);
    }
}
