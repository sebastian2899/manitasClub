package com.manitasclub.app.service.impl;

import com.manitasclub.app.domain.Abono;
import com.manitasclub.app.domain.Membresia;
import com.manitasclub.app.domain.enumeration.EstadoMembresia;
import com.manitasclub.app.repository.AbonoRepository;
import com.manitasclub.app.repository.MembresiaRepository;
import com.manitasclub.app.service.AbonoService;
import com.manitasclub.app.service.dto.AbonoDTO;
import com.manitasclub.app.service.mapper.AbonoMapper;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Abono}.
 */
@Service
@Transactional
public class AbonoServiceImpl implements AbonoService {

    private final Logger log = LoggerFactory.getLogger(AbonoServiceImpl.class);

    private final AbonoRepository abonoRepository;

    private final AbonoMapper abonoMapper;

    private final MembresiaRepository membresiaRepository;

    public AbonoServiceImpl(AbonoRepository abonoRepository, AbonoMapper abonoMapper, MembresiaRepository membresiaRepository) {
        this.abonoRepository = abonoRepository;
        this.abonoMapper = abonoMapper;
        this.membresiaRepository = membresiaRepository;
    }

    @Override
    public AbonoDTO save(AbonoDTO abonoDTO) {
        log.debug("Request to save Abono : {}", abonoDTO);
        Abono abono = abonoMapper.toEntity(abonoDTO);
        abono = abonoRepository.save(abono);

        Membresia membresia = membresiaRepository.findById(abono.getIdMembresia()).get();

        membresia.setDeuda(membresia.getDeuda().subtract(abono.getValorAbono()));
        membresia.setEstado(membresia.getDeuda().compareTo(BigDecimal.ZERO) == 0 ? EstadoMembresia.ACTIVA : EstadoMembresia.DEUDA);
        membresiaRepository.save(membresia);
        return abonoMapper.toDto(abono);
    }

    @Override
    public AbonoDTO update(AbonoDTO abonoDTO) {
        log.debug("Request to save Abono : {}", abonoDTO);
        Abono abono = abonoMapper.toEntity(abonoDTO);
        abono = abonoRepository.save(abono);
        return abonoMapper.toDto(abono);
    }

    @Override
    public Optional<AbonoDTO> partialUpdate(AbonoDTO abonoDTO) {
        log.debug("Request to partially update Abono : {}", abonoDTO);

        return abonoRepository
            .findById(abonoDTO.getId())
            .map(existingAbono -> {
                abonoMapper.partialUpdate(existingAbono, abonoDTO);

                return existingAbono;
            })
            .map(abonoRepository::save)
            .map(abonoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AbonoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Abonos");
        return abonoRepository.findAll(pageable).map(abonoMapper::toDto);
    }

    @Override
    @Transactional
    public List<AbonoDTO> findAllByMembresia(Long idMembresia) {
        log.debug("Request to get all Abonos");
        return abonoRepository
            .findByMembresia(idMembresia)
            .stream()
            .map(abonoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AbonoDTO> findOne(Long id) {
        log.debug("Request to get Abono : {}", id);
        return abonoRepository.findById(id).map(abonoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Abono : {}", id);
        abonoRepository.deleteById(id);
    }
}
