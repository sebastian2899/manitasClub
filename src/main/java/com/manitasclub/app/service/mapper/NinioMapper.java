package com.manitasclub.app.service.mapper;

import com.manitasclub.app.domain.Acudiente;
import com.manitasclub.app.domain.Ninio;
import com.manitasclub.app.service.dto.AcudienteDTO;
import com.manitasclub.app.service.dto.NinioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ninio} and its DTO {@link NinioDTO}.
 */
@Mapper(componentModel = "spring")
public interface NinioMapper extends EntityMapper<NinioDTO, Ninio> {
    @Mapping(target = "acudiente", source = "acudiente", qualifiedByName = "acudienteId")
    NinioDTO toDto(Ninio s);

    @Named("acudienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AcudienteDTO toDtoAcudienteId(Acudiente acudiente);
}
