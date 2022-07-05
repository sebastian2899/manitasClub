package com.manitasclub.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.manitasclub.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NinioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NinioDTO.class);
        NinioDTO ninioDTO1 = new NinioDTO();
        ninioDTO1.setId(1L);
        NinioDTO ninioDTO2 = new NinioDTO();
        assertThat(ninioDTO1).isNotEqualTo(ninioDTO2);
        ninioDTO2.setId(ninioDTO1.getId());
        assertThat(ninioDTO1).isEqualTo(ninioDTO2);
        ninioDTO2.setId(2L);
        assertThat(ninioDTO1).isNotEqualTo(ninioDTO2);
        ninioDTO1.setId(null);
        assertThat(ninioDTO1).isNotEqualTo(ninioDTO2);
    }
}
