package com.manitasclub.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MembresiaMapperTest {

    private MembresiaMapper membresiaMapper;

    @BeforeEach
    public void setUp() {
        membresiaMapper = new MembresiaMapperImpl();
    }
}
