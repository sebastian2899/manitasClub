package com.manitasclub.app.service.impl;

import com.manitasclub.app.domain.TotalGastos;
import com.manitasclub.app.repository.TotalGastosRepository;
import com.manitasclub.app.service.TotalGastosService;
import com.manitasclub.app.service.dto.TotalGastosDTO;
import com.manitasclub.app.service.mapper.TotalGastosMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TotalGastos}.
 */
@Service
@Transactional
public class TotalGastosServiceImpl implements TotalGastosService {

    private final Logger log = LoggerFactory.getLogger(TotalGastosServiceImpl.class);

    private final TotalGastosRepository totalGastosRepository;

    private final TotalGastosMapper totalGastosMapper;

    public TotalGastosServiceImpl(TotalGastosRepository totalGastosRepository, TotalGastosMapper totalGastosMapper) {
        this.totalGastosRepository = totalGastosRepository;
        this.totalGastosMapper = totalGastosMapper;
    }

    @Override
    public TotalGastosDTO save(TotalGastosDTO totalGastosDTO) {
        log.debug("Request to save TotalGastos : {}", totalGastosDTO);
        TotalGastos totalGastos = totalGastosMapper.toEntity(totalGastosDTO);
        totalGastos = totalGastosRepository.save(totalGastos);
        return totalGastosMapper.toDto(totalGastos);
    }

    @Override
    public TotalGastosDTO update(TotalGastosDTO totalGastosDTO) {
        log.debug("Request to save TotalGastos : {}", totalGastosDTO);
        TotalGastos totalGastos = totalGastosMapper.toEntity(totalGastosDTO);
        totalGastos = totalGastosRepository.save(totalGastos);
        return totalGastosMapper.toDto(totalGastos);
    }

    @Override
    public Optional<TotalGastosDTO> partialUpdate(TotalGastosDTO totalGastosDTO) {
        log.debug("Request to partially update TotalGastos : {}", totalGastosDTO);

        return totalGastosRepository
            .findById(totalGastosDTO.getId())
            .map(existingTotalGastos -> {
                totalGastosMapper.partialUpdate(existingTotalGastos, totalGastosDTO);

                return existingTotalGastos;
            })
            .map(totalGastosRepository::save)
            .map(totalGastosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TotalGastosDTO> findAll() {
        log.debug("Request to get all TotalGastos");
        return totalGastosRepository.findAll().stream().map(totalGastosMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TotalGastosDTO> findOne(Long id) {
        log.debug("Request to get TotalGastos : {}", id);
        return totalGastosRepository.findById(id).map(totalGastosMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TotalGastos : {}", id);
        totalGastosRepository.deleteById(id);
    }
}
