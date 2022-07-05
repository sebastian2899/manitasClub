package com.manitasclub.app.service.impl;

import com.manitasclub.app.domain.Acudiente;
import com.manitasclub.app.repository.AcudienteRepository;
import com.manitasclub.app.service.AcudienteService;
import com.manitasclub.app.service.dto.AcudienteDTO;
import com.manitasclub.app.service.mapper.AcudienteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Acudiente}.
 */
@Service
@Transactional
public class AcudienteServiceImpl implements AcudienteService {

    private final Logger log = LoggerFactory.getLogger(AcudienteServiceImpl.class);

    private final AcudienteRepository acudienteRepository;

    private final AcudienteMapper acudienteMapper;

    public AcudienteServiceImpl(AcudienteRepository acudienteRepository, AcudienteMapper acudienteMapper) {
        this.acudienteRepository = acudienteRepository;
        this.acudienteMapper = acudienteMapper;
    }

    @Override
    public AcudienteDTO save(AcudienteDTO acudienteDTO) {
        log.debug("Request to save Acudiente : {}", acudienteDTO);
        Acudiente acudiente = acudienteMapper.toEntity(acudienteDTO);
        acudiente = acudienteRepository.save(acudiente);
        return acudienteMapper.toDto(acudiente);
    }

    @Override
    public AcudienteDTO update(AcudienteDTO acudienteDTO) {
        log.debug("Request to save Acudiente : {}", acudienteDTO);
        Acudiente acudiente = acudienteMapper.toEntity(acudienteDTO);
        acudiente = acudienteRepository.save(acudiente);
        return acudienteMapper.toDto(acudiente);
    }

    @Override
    public Optional<AcudienteDTO> partialUpdate(AcudienteDTO acudienteDTO) {
        log.debug("Request to partially update Acudiente : {}", acudienteDTO);

        return acudienteRepository
            .findById(acudienteDTO.getId())
            .map(existingAcudiente -> {
                acudienteMapper.partialUpdate(existingAcudiente, acudienteDTO);

                return existingAcudiente;
            })
            .map(acudienteRepository::save)
            .map(acudienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcudienteDTO> findAll() {
        log.debug("Request to get all Acudientes");
        return acudienteRepository.findAll().stream().map(acudienteMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcudienteDTO> findOne(Long id) {
        log.debug("Request to get Acudiente : {}", id);
        return acudienteRepository.findById(id).map(acudienteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Acudiente : {}", id);
        acudienteRepository.deleteById(id);
    }
}
