package com.manitasclub.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembresiaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembresiaDTO.class);
        MembresiaDTO membresiaDTO1 = new MembresiaDTO();
        membresiaDTO1.setId(1L);
        MembresiaDTO membresiaDTO2 = new MembresiaDTO();
        assertThat(membresiaDTO1).isNotEqualTo(membresiaDTO2);
        membresiaDTO2.setId(membresiaDTO1.getId());
        assertThat(membresiaDTO1).isEqualTo(membresiaDTO2);
        membresiaDTO2.setId(2L);
        assertThat(membresiaDTO1).isNotEqualTo(membresiaDTO2);
        membresiaDTO1.setId(null);
        assertThat(membresiaDTO1).isNotEqualTo(membresiaDTO2);
    }
}
