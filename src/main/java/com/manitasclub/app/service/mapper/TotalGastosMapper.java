package com.manitasclub.app.service.mapper;

import com.manitasclub.app.domain.TotalGastos;
import com.manitasclub.app.service.dto.TotalGastosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TotalGastos} and its DTO {@link TotalGastosDTO}.
 */
@Mapper(componentModel = "spring")
public interface TotalGastosMapper extends EntityMapper<TotalGastosDTO, TotalGastos> {}
