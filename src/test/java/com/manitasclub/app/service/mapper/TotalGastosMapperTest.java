package com.manitasclub.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TotalGastosMapperTest {

    private TotalGastosMapper totalGastosMapper;

    @BeforeEach
    public void setUp() {
        totalGastosMapper = new TotalGastosMapperImpl();
    }
}
