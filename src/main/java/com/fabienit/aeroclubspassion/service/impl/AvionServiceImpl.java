package com.fabienit.aeroclubspassion.service.impl;

import com.fabienit.aeroclubspassion.domain.Avion;
import com.fabienit.aeroclubspassion.repository.AvionRepository;
import com.fabienit.aeroclubspassion.service.AvionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Avion}.
 */
@Service
@Transactional
public class AvionServiceImpl implements AvionService {

    private final Logger log = LoggerFactory.getLogger(AvionServiceImpl.class);

    private final AvionRepository avionRepository;

    public AvionServiceImpl(AvionRepository avionRepository) {
        this.avionRepository = avionRepository;
    }

    @Override
    public Avion save(Avion avion) {
        log.debug("Request to save Avion : {}", avion);
        return avionRepository.save(avion);
    }

    @Override
    public Optional<Avion> partialUpdate(Avion avion) {
        log.debug("Request to partially update Avion : {}", avion);

        return avionRepository
            .findById(avion.getId())
            .map(existingAvion -> {
                if (avion.getMarque() != null) {
                    existingAvion.setMarque(avion.getMarque());
                }
                if (avion.getType() != null) {
                    existingAvion.setType(avion.getType());
                }
                if (avion.getMoteur() != null) {
                    existingAvion.setMoteur(avion.getMoteur());
                }
                if (avion.getPuissance() != null) {
                    existingAvion.setPuissance(avion.getPuissance());
                }
                if (avion.getPlace() != null) {
                    existingAvion.setPlace(avion.getPlace());
                }
                if (avion.getAutonomie() != null) {
                    existingAvion.setAutonomie(avion.getAutonomie());
                }
                if (avion.getUsage() != null) {
                    existingAvion.setUsage(avion.getUsage());
                }
                if (avion.getHeures() != null) {
                    existingAvion.setHeures(avion.getHeures());
                }

                return existingAvion;
            })
            .map(avionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Avion> findAll(Pageable pageable) {
        log.debug("Request to get all Avions");
        return avionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Avion> findOne(Long id) {
        log.debug("Request to get Avion : {}", id);
        return avionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Avion : {}", id);
        avionRepository.deleteById(id);
    }
}
