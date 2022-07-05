package com.manitasclub.app.service.mapper;

import com.manitasclub.app.domain.Membresia;
import com.manitasclub.app.domain.Ninio;
import com.manitasclub.app.domain.TipoMembresia;
import com.manitasclub.app.service.dto.MembresiaDTO;
import com.manitasclub.app.service.dto.NinioDTO;
import com.manitasclub.app.service.dto.TipoMembresiaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Membresia} and its DTO {@link MembresiaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MembresiaMapper extends EntityMapper<MembresiaDTO, Membresia> {
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoMembresiaId")
    @Mapping(target = "ninio", source = "ninio", qualifiedByName = "ninioId")
    MembresiaDTO toDto(Membresia s);

    @Named("tipoMembresiaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoMembresiaDTO toDtoTipoMembresiaId(TipoMembresia tipoMembresia);

    @Named("ninioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NinioDTO toDtoNinioId(Ninio ninio);
}
