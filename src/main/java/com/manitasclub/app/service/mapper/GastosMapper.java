package com.manitasclub.app.service.mapper;

import com.manitasclub.app.domain.Gastos;
import com.manitasclub.app.service.dto.GastosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Gastos} and its DTO {@link GastosDTO}.
 */
@Mapper(componentModel = "spring")
public interface GastosMapper extends EntityMapper<GastosDTO, Gastos> {}
