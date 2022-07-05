package com.manitasclub.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoMembresiaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoMembresiaDTO.class);
        TipoMembresiaDTO tipoMembresiaDTO1 = new TipoMembresiaDTO();
        tipoMembresiaDTO1.setId(1L);
        TipoMembresiaDTO tipoMembresiaDTO2 = new TipoMembresiaDTO();
        assertThat(tipoMembresiaDTO1).isNotEqualTo(tipoMembresiaDTO2);
        tipoMembresiaDTO2.setId(tipoMembresiaDTO1.getId());
        assertThat(tipoMembresiaDTO1).isEqualTo(tipoMembresiaDTO2);
        tipoMembresiaDTO2.setId(2L);
        assertThat(tipoMembresiaDTO1).isNotEqualTo(tipoMembresiaDTO2);
        tipoMembresiaDTO1.setId(null);
        assertThat(tipoMembresiaDTO1).isNotEqualTo(tipoMembresiaDTO2);
    }
}
