package com.manitasclub.app.service.mapper;

import com.manitasclub.app.domain.RegistroDiario;
import com.manitasclub.app.service.dto.RegistroDiarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RegistroDiario} and its DTO {@link RegistroDiarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface RegistroDiarioMapper extends EntityMapper<RegistroDiarioDTO, RegistroDiario> {}
