package com.manitasclub.app.service.impl;

import com.manitasclub.app.domain.TipoMembresia;
import com.manitasclub.app.repository.TipoMembresiaRepository;
import com.manitasclub.app.service.TipoMembresiaService;
import com.manitasclub.app.service.dto.TipoMembresiaDTO;
import com.manitasclub.app.service.mapper.TipoMembresiaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoMembresia}.
 */
@Service
@Transactional
public class TipoMembresiaServiceImpl implements TipoMembresiaService {

    private final Logger log = LoggerFactory.getLogger(TipoMembresiaServiceImpl.class);

    private final TipoMembresiaRepository tipoMembresiaRepository;

    private final TipoMembresiaMapper tipoMembresiaMapper;

    public TipoMembresiaServiceImpl(TipoMembresiaRepository tipoMembresiaRepository, TipoMembresiaMapper tipoMembresiaMapper) {
        this.tipoMembresiaRepository = tipoMembresiaRepository;
        this.tipoMembresiaMapper = tipoMembresiaMapper;
    }

    @Override
    public TipoMembresiaDTO save(TipoMembresiaDTO tipoMembresiaDTO) {
        log.debug("Request to save TipoMembresia : {}", tipoMembresiaDTO);
        TipoMembresia tipoMembresia = tipoMembresiaMapper.toEntity(tipoMembresiaDTO);
        tipoMembresia = tipoMembresiaRepository.save(tipoMembresia);
        return tipoMembresiaMapper.toDto(tipoMembresia);
    }

    @Override
    public TipoMembresiaDTO update(TipoMembresiaDTO tipoMembresiaDTO) {
        log.debug("Request to save TipoMembresia : {}", tipoMembresiaDTO);
        TipoMembresia tipoMembresia = tipoMembresiaMapper.toEntity(tipoMembresiaDTO);
        tipoMembresia = tipoMembresiaRepository.save(tipoMembresia);
        return tipoMembresiaMapper.toDto(tipoMembresia);
    }

    @Override
    public Optional<TipoMembresiaDTO> partialUpdate(TipoMembresiaDTO tipoMembresiaDTO) {
        log.debug("Request to partially update TipoMembresia : {}", tipoMembresiaDTO);

        return tipoMembresiaRepository
            .findById(tipoMembresiaDTO.getId())
            .map(existingTipoMembresia -> {
                tipoMembresiaMapper.partialUpdate(existingTipoMembresia, tipoMembresiaDTO);

                return existingTipoMembresia;
            })
            .map(tipoMembresiaRepository::save)
            .map(tipoMembresiaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoMembresiaDTO> findAll() {
        log.debug("Request to get all TipoMembresias");
        return tipoMembresiaRepository.findAll().stream().map(tipoMembresiaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoMembresiaDTO> findOne(Long id) {
        log.debug("Request to get TipoMembresia : {}", id);
        return tipoMembresiaRepository.findById(id).map(tipoMembresiaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoMembresia : {}", id);
        tipoMembresiaRepository.deleteById(id);
    }
}
