package com.manitasclub.app.service.mapper;

import com.manitasclub.app.domain.TipoMembresia;
import com.manitasclub.app.service.dto.TipoMembresiaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoMembresia} and its DTO {@link TipoMembresiaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoMembresiaMapper extends EntityMapper<TipoMembresiaDTO, TipoMembresia> {}
