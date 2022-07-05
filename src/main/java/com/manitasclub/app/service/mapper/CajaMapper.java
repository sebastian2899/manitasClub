package com.manitasclub.app.service.mapper;

import com.manitasclub.app.domain.Caja;
import com.manitasclub.app.service.dto.CajaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Caja} and its DTO {@link CajaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CajaMapper extends EntityMapper<CajaDTO, Caja> {}
