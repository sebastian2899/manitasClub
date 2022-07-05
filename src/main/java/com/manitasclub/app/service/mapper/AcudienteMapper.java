package com.manitasclub.app.service.mapper;

import com.manitasclub.app.domain.Acudiente;
import com.manitasclub.app.service.dto.AcudienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Acudiente} and its DTO {@link AcudienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface AcudienteMapper extends EntityMapper<AcudienteDTO, Acudiente> {}
