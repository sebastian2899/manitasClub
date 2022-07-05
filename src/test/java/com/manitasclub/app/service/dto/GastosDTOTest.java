package com.manitasclub.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GastosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GastosDTO.class);
        GastosDTO gastosDTO1 = new GastosDTO();
        gastosDTO1.setId(1L);
        GastosDTO gastosDTO2 = new GastosDTO();
        assertThat(gastosDTO1).isNotEqualTo(gastosDTO2);
        gastosDTO2.setId(gastosDTO1.getId());
        assertThat(gastosDTO1).isEqualTo(gastosDTO2);
        gastosDTO2.setId(2L);
        assertThat(gastosDTO1).isNotEqualTo(gastosDTO2);
        gastosDTO1.setId(null);
        assertThat(gastosDTO1).isNotEqualTo(gastosDTO2);
    }
}
