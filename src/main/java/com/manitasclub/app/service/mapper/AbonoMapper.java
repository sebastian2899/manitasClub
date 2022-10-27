package com.manitasclub.app.service.mapper;

import com.manitasclub.app.domain.Abono;
import com.manitasclub.app.service.dto.AbonoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Abono} and its DTO {@link AbonoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AbonoMapper extends EntityMapper<AbonoDTO, Abono> {}
