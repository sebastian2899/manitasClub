package com.manitasclub.app.service.impl;

import com.manitasclub.app.domain.RegistroDiario;
import com.manitasclub.app.repository.RegistroDiarioRepository;
import com.manitasclub.app.service.RegistroDiarioService;
import com.manitasclub.app.service.dto.RegistroDiarioDTO;
import com.manitasclub.app.service.mapper.RegistroDiarioMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RegistroDiario}.
 */
@Service
@Transactional
public class RegistroDiarioServiceImpl implements RegistroDiarioService {

    private final Logger log = LoggerFactory.getLogger(RegistroDiarioServiceImpl.class);

    private final RegistroDiarioRepository registroDiarioRepository;

    private final RegistroDiarioMapper registroDiarioMapper;

    public RegistroDiarioServiceImpl(RegistroDiarioRepository registroDiarioRepository, RegistroDiarioMapper registroDiarioMapper) {
        this.registroDiarioRepository = registroDiarioRepository;
        this.registroDiarioMapper = registroDiarioMapper;
    }

    @Override
    public RegistroDiarioDTO save(RegistroDiarioDTO registroDiarioDTO) {
        log.debug("Request to save RegistroDiario : {}", registroDiarioDTO);
        RegistroDiario registroDiario = registroDiarioMapper.toEntity(registroDiarioDTO);
        registroDiario = registroDiarioRepository.save(registroDiario);
        return registroDiarioMapper.toDto(registroDiario);
    }

    @Override
    public RegistroDiarioDTO update(RegistroDiarioDTO registroDiarioDTO) {
        log.debug("Request to save RegistroDiario : {}", registroDiarioDTO);
        RegistroDiario registroDiario = registroDiarioMapper.toEntity(registroDiarioDTO);
        registroDiario = registroDiarioRepository.save(registroDiario);
        return registroDiarioMapper.toDto(registroDiario);
    }

    @Override
    public Optional<RegistroDiarioDTO> partialUpdate(RegistroDiarioDTO registroDiarioDTO) {
        log.debug("Request to partially update RegistroDiario : {}", registroDiarioDTO);

        return registroDiarioRepository
            .findById(registroDiarioDTO.getId())
            .map(existingRegistroDiario -> {
                registroDiarioMapper.partialUpdate(existingRegistroDiario, registroDiarioDTO);

                return existingRegistroDiario;
            })
            .map(registroDiarioRepository::save)
            .map(registroDiarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistroDiarioDTO> findAll() {
        log.debug("Request to get all RegistroDiarios");
        return registroDiarioRepository
            .findAll()
            .stream()
            .map(registroDiarioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegistroDiarioDTO> findOne(Long id) {
        log.debug("Request to get RegistroDiario : {}", id);
        return registroDiarioRepository.findById(id).map(registroDiarioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RegistroDiario : {}", id);
        registroDiarioRepository.deleteById(id);
    }
}
