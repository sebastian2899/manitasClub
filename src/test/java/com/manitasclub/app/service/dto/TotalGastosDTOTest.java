package com.manitasclub.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TotalGastosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TotalGastosDTO.class);
        TotalGastosDTO totalGastosDTO1 = new TotalGastosDTO();
        totalGastosDTO1.setId(1L);
        TotalGastosDTO totalGastosDTO2 = new TotalGastosDTO();
        assertThat(totalGastosDTO1).isNotEqualTo(totalGastosDTO2);
        totalGastosDTO2.setId(totalGastosDTO1.getId());
        assertThat(totalGastosDTO1).isEqualTo(totalGastosDTO2);
        totalGastosDTO2.setId(2L);
        assertThat(totalGastosDTO1).isNotEqualTo(totalGastosDTO2);
        totalGastosDTO1.setId(null);
        assertThat(totalGastosDTO1).isNotEqualTo(totalGastosDTO2);
    }
}
