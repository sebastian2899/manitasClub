package com.manitasclub.app.service.impl;

import com.manitasclub.app.domain.Caja;
import com.manitasclub.app.repository.CajaRepository;
import com.manitasclub.app.service.CajaService;
import com.manitasclub.app.service.dto.CajaDTO;
import com.manitasclub.app.service.mapper.CajaMapper;
import java.math.BigDecimal;
import java.time.Instant;
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
 * Service Implementation for managing {@link Caja}.
 */
@Service
@Transactional
public class CajaServiceImpl implements CajaService {

    private final Logger log = LoggerFactory.getLogger(CajaServiceImpl.class);

    private final CajaRepository cajaRepository;

    private final CajaMapper cajaMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public CajaServiceImpl(CajaRepository cajaRepository, CajaMapper cajaMapper) {
        this.cajaRepository = cajaRepository;
        this.cajaMapper = cajaMapper;
    }

    @Override
    public CajaDTO save(CajaDTO cajaDTO) {
        log.debug("Request to save Caja : {}", cajaDTO);
        Caja caja = cajaMapper.toEntity(cajaDTO);
        caja.fechaCreacion(Instant.now());
        caja = cajaRepository.save(caja);
        return cajaMapper.toDto(caja);
    }

    @Override
    public CajaDTO update(CajaDTO cajaDTO) {
        log.debug("Request to save Caja : {}", cajaDTO);
        Caja caja = cajaMapper.toEntity(cajaDTO);
        caja = cajaRepository.save(caja);
        return cajaMapper.toDto(caja);
    }

    @Override
    public Optional<CajaDTO> partialUpdate(CajaDTO cajaDTO) {
        log.debug("Request to partially update Caja : {}", cajaDTO);

        return cajaRepository
            .findById(cajaDTO.getId())
            .map(existingCaja -> {
                cajaMapper.partialUpdate(existingCaja, cajaDTO);

                return existingCaja;
            })
            .map(cajaRepository::save)
            .map(cajaMapper::toDto);
    }

    @Override
    @Transactional
    public BigDecimal valoresDiarios() {
        String fecha = Instant.now().toString().substring(0, 10);

        Query q = entityManager
            .createNativeQuery("SELECT SUM(m.valor_pagado) FROM membresia AS m WHERE TO_CHAR(m.fecha_creacion, 'yyyy-MM-dd') = :fecha")
            .setParameter("fecha", fecha);
        BigDecimal valueMembresia = (BigDecimal) q.getSingleResult();
        if (valueMembresia == null) {
            valueMembresia = BigDecimal.ZERO;
        }

        Query q2 = entityManager
            .createNativeQuery("SELECT SUM(r.valor) FROM registro_diario AS r WHERE TO_CHAR(r.fecha_ingreso, 'yyyy-MM-dd') = :fecha")
            .setParameter("fecha", fecha);
        BigDecimal valueRegistroDiario = (BigDecimal) q2.getSingleResult();
        if (valueRegistroDiario == null) {
            valueRegistroDiario = BigDecimal.ZERO;
        }

        Query q3 = entityManager
            .createNativeQuery("SELECT SUM(valor_abono) FROM abono WHERE TO_CHAR(fecha_abono, 'yyyy-MM-dd') = :fecha")
            .setParameter("fecha", fecha);
        BigDecimal valueAbono = (BigDecimal) q3.getSingleResult();
        if (valueAbono == null) {
            valueAbono = BigDecimal.ZERO;
        }

        return valueMembresia.add(valueRegistroDiario).add(valueAbono);
    }

    @Override
    @Transactional
    public BigDecimal valorPorMeses(String fechaInicio, String fechaFin) {
        log.debug("find value per months");

        String fechaInicioFormat = fechaInicio.substring(0, 10);
        String fechaFinFormat = fechaFin.substring(0, 10);

        Query q = entityManager
            .createQuery(
                "SELECT SUM(c.valorDia) FROM Caja c WHERE TO_CHAR(c.fechaCreacion, 'yyyy-MM-dd') " + "BETWEEN :fechaInicio AND :fechaFin"
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
    public List<CajaDTO> findAll() {
        log.debug("Request to get all Cajas");
        return cajaRepository.findAll().stream().map(cajaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CajaDTO> findOne(Long id) {
        log.debug("Request to get Caja : {}", id);
        return cajaRepository.findById(id).map(cajaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Caja : {}", id);
        cajaRepository.deleteById(id);
    }
}
