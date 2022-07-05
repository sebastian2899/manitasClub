package com.manitasclub.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NinioMapperTest {

    private NinioMapper ninioMapper;

    @BeforeEach
    public void setUp() {
        ninioMapper = new NinioMapperImpl();
    }
}
