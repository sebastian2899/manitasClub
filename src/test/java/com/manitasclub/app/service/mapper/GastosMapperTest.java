package com.manitasclub.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GastosMapperTest {

    private GastosMapper gastosMapper;

    @BeforeEach
    public void setUp() {
        gastosMapper = new GastosMapperImpl();
    }
}
