package com.manitasclub.app.service.impl;

import static org.springframework.data.jpa.domain.Specification.where;

import com.manitasclub.app.domain.Ninio;
import com.manitasclub.app.repository.NinioRepository;
import com.manitasclub.app.repository.NinioSpecification;
import com.manitasclub.app.service.NinioService;
import com.manitasclub.app.service.dto.NinioDTO;
import com.manitasclub.app.service.mapper.NinioMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ninio}.
 */
@Service
@Transactional
public class NinioServiceImpl implements NinioService {

    private final Logger log = LoggerFactory.getLogger(NinioServiceImpl.class);

    private final NinioRepository ninioRepository;

    private final NinioMapper ninioMapper;

    public NinioServiceImpl(NinioRepository ninioRepository, NinioMapper ninioMapper) {
        this.ninioRepository = ninioRepository;
        this.ninioMapper = ninioMapper;
    }

    @Override
    public NinioDTO save(NinioDTO ninioDTO) {
        log.debug("Request to save Ninio : {}", ninioDTO);
        Ninio ninio = ninioMapper.toEntity(ninioDTO);
        ninio = ninioRepository.save(ninio);
        return ninioMapper.toDto(ninio);
    }

    @Override
    public NinioDTO update(NinioDTO ninioDTO) {
        log.debug("Request to save Ninio : {}", ninioDTO);
        Ninio ninio = ninioMapper.toEntity(ninioDTO);
        if (!ninio.getObservacion() && null != ninio.getDescripcionObservacion()) {
            ninio.setDescripcionObservacion(null);
        }
        ninio = ninioRepository.save(ninio);
        return ninioMapper.toDto(ninio);
    }

    @Override
    public Optional<NinioDTO> partialUpdate(NinioDTO ninioDTO) {
        log.debug("Request to partially update Ninio : {}", ninioDTO);

        return ninioRepository
            .findById(ninioDTO.getId())
            .map(existingNinio -> {
                ninioMapper.partialUpdate(existingNinio, ninioDTO);

                return existingNinio;
            })
            .map(ninioRepository::save)
            .map(ninioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ninio> findAll() {
        log.debug("Request to get all Ninios");
        return ninioRepository.findAll().stream().collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NinioDTO> findOne(Long id) {
        log.debug("Request to get Ninio : {}", id);
        return ninioRepository.findById(id).map(ninioMapper::toDto);
    }

    @Override
    public List<Ninio> ninioByFilter(NinioDTO ninioDTO) {
        log.debug("Request to get Ninio : {}", ninioDTO);
        return ninioRepository.findAll(
            where(NinioSpecification.nombreContains(ninioDTO.getNombres() != null ? ninioDTO.getNombres() : ""))
                .and(NinioSpecification.apellidosContains(ninioDTO.getApellidos() != null ? ninioDTO.getApellidos() : ""))
                .and(NinioSpecification.observacion(ninioDTO.getObservacion()))
        );
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ninio : {}", id);
        ninioRepository.deleteById(id);
    }
}
