package com.manitasclub.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AcudienteMapperTest {

    private AcudienteMapper acudienteMapper;

    @BeforeEach
    public void setUp() {
        acudienteMapper = new AcudienteMapperImpl();
    }
}
