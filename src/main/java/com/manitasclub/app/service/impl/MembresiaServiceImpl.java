package com.manitasclub.app.service.impl;

import com.manitasclub.app.domain.Membresia;
import com.manitasclub.app.domain.enumeration.EstadoMembresia;
import com.manitasclub.app.repository.MembresiaRepository;
import com.manitasclub.app.service.MembresiaService;
import com.manitasclub.app.service.dto.MembresiaDTO;
import com.manitasclub.app.service.mapper.MembresiaMapper;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Membresia}.
 */
@Service
@Transactional
public class MembresiaServiceImpl implements MembresiaService {

    private final Logger log = LoggerFactory.getLogger(MembresiaServiceImpl.class);

    private final MembresiaRepository membresiaRepository;

    private final MembresiaMapper membresiaMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public MembresiaServiceImpl(MembresiaRepository membresiaRepository, MembresiaMapper membresiaMapper) {
        this.membresiaRepository = membresiaRepository;
        this.membresiaMapper = membresiaMapper;
    }

    @Override
    public MembresiaDTO save(MembresiaDTO membresiaDTO) {
        log.debug("Request to save Membresia : {}", membresiaDTO);
        Membresia membresia = membresiaMapper.toEntity(membresiaDTO);
        membresia = membresiaRepository.save(membresia);
        return membresiaMapper.toDto(membresia);
    }

    @Override
    public MembresiaDTO update(MembresiaDTO membresiaDTO) {
        log.debug("Request to save Membresia : {}", membresiaDTO);
        Membresia membresia = membresiaMapper.toEntity(membresiaDTO);
        membresia = membresiaRepository.save(membresia);
        return membresiaMapper.toDto(membresia);
    }

    @Override
    public Optional<MembresiaDTO> partialUpdate(MembresiaDTO membresiaDTO) {
        log.debug("Request to partially update Membresia : {}", membresiaDTO);

        return membresiaRepository
            .findById(membresiaDTO.getId())
            .map(existingMembresia -> {
                membresiaMapper.partialUpdate(existingMembresia, membresiaDTO);

                return existingMembresia;
            })
            .map(membresiaRepository::save)
            .map(membresiaMapper::toDto);
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void validarEstadoMembresia() {
        List<Membresia> membresias = membresiaRepository
            .findAll()
            .stream()
            .filter(element -> element.getFechaFin().isBefore(Instant.now()))
            .collect(Collectors.toCollection(LinkedList::new));

        //membresias vencidas
        membresias
            .stream()
            .forEach(element -> {
                element.setEstado(EstadoMembresia.VENCIDA);
            });

        membresiaRepository.saveAll(membresias);
    }

    @Override
    @Transactional
    public BigDecimal valorPorMeses(String fechaInicio, String fechaFin) {
        log.debug("find value per months");

        String fechaInicioFormat = fechaInicio.substring(0, 10);
        String fechaFinFormat = fechaFin.substring(0, 10);

        Query q = entityManager
            .createQuery(
                "SELECT SUM(e.precioMembresia) FROM Membresia e WHERE TO_CHAR(e.fechaCreacion, 'yyyy-MM-dd') " +
                "BETWEEN :fechaInicio AND :fechaFin"
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
    public List<Membresia> findAll() {
        log.debug("Request to get all Membresias");
        return membresiaRepository.findAll().stream().collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Membresia> findOne(Long id) {
        log.debug("Request to get Membresia : {}", id);
        return membresiaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Membresia : {}", id);
        membresiaRepository.deleteById(id);
    }
}
