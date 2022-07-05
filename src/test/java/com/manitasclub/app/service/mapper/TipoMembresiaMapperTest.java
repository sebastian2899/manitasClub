package com.manitasclub.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoMembresiaMapperTest {

    private TipoMembresiaMapper tipoMembresiaMapper;

    @BeforeEach
    public void setUp() {
        tipoMembresiaMapper = new TipoMembresiaMapperImpl();
    }
}
