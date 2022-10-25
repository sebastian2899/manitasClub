package com.manitasclub.app.service.impl;

import com.manitasclub.app.domain.Gastos;
import com.manitasclub.app.repository.GastosRepository;
import com.manitasclub.app.service.GastosService;
import com.manitasclub.app.service.dto.GastosDTO;
import com.manitasclub.app.service.mapper.GastosMapper;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Gastos}.
 */
@Service
@Transactional
public class GastosServiceImpl implements GastosService {

    private final Logger log = LoggerFactory.getLogger(GastosServiceImpl.class);

    private final GastosRepository gastosRepository;

    private final GastosMapper gastosMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public GastosServiceImpl(GastosRepository gastosRepository, GastosMapper gastosMapper) {
        this.gastosRepository = gastosRepository;
        this.gastosMapper = gastosMapper;
    }

    @Override
    public GastosDTO save(GastosDTO gastosDTO) {
        log.debug("Request to save Gastos : {}", gastosDTO);
        Gastos gastos = gastosMapper.toEntity(gastosDTO);
        gastos = gastosRepository.save(gastos);
        return gastosMapper.toDto(gastos);
    }

    @Override
    public GastosDTO update(GastosDTO gastosDTO) {
        log.debug("Request to save Gastos : {}", gastosDTO);
        Gastos gastos = gastosMapper.toEntity(gastosDTO);
        gastos = gastosRepository.save(gastos);
        return gastosMapper.toDto(gastos);
    }

    @Override
    public Optional<GastosDTO> partialUpdate(GastosDTO gastosDTO) {
        log.debug("Request to partially update Gastos : {}", gastosDTO);

        return gastosRepository
            .findById(gastosDTO.getId())
            .map(existingGastos -> {
                gastosMapper.partialUpdate(existingGastos, gastosDTO);

                return existingGastos;
            })
            .map(gastosRepository::save)
            .map(gastosMapper::toDto);
    }

    @Override
    @Transactional
    public BigDecimal valorPorMeses(String fechaInicio, String fechaFin) {
        log.debug("find value per months");

        String fechaInicioFormat = fechaInicio.substring(0, 10);
        String fechaFinFormat = fechaFin.substring(0, 10);

        Query q = entityManager
            .createQuery(
                "SELECT SUM(e.valor) FROM Gastos e WHERE TO_CHAR(e.fechaCreacion, 'yyyy-MM-dd') " + "BETWEEN :fechaInicio AND :fechaFin"
            )
            .setParameter("fechaInicio", fechaInicioFormat)
            .setParameter("fechaFin", fechaFinFormat);

        BigDecimal value = (BigDecimal) q.getSingleResult();

        if (null == value) {
            value = BigDecimal.ZERO;
        }

        return value;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GastosDTO> findAll() {
        log.debug("Request to get all Gastos");
        return gastosRepository.findAll().stream().map(gastosMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GastosDTO> findOne(Long id) {
        log.debug("Request to get Gastos : {}", id);
        return gastosRepository.findById(id).map(gastosMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gastos : {}", id);
        gastosRepository.deleteById(id);
    }
}
