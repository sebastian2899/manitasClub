package com.manitasclub.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistroDiarioMapperTest {

    private RegistroDiarioMapper registroDiarioMapper;

    @BeforeEach
    public void setUp() {
        registroDiarioMapper = new RegistroDiarioMapperImpl();
    }
}
