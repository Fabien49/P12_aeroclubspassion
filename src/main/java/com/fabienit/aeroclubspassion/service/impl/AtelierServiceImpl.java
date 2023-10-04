package com.fabienit.aeroclubspassion.service.impl;

import com.fabienit.aeroclubspassion.domain.Atelier;
import com.fabienit.aeroclubspassion.repository.AtelierRepository;
import com.fabienit.aeroclubspassion.service.AtelierService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Atelier}.
 */
@Service
@Transactional
public class AtelierServiceImpl implements AtelierService {

    private final Logger log = LoggerFactory.getLogger(AtelierServiceImpl.class);

    private final AtelierRepository atelierRepository;

    public AtelierServiceImpl(AtelierRepository atelierRepository) {
        this.atelierRepository = atelierRepository;
    }

    @Override
    public Atelier save(Atelier atelier) {
        log.debug("Request to save Atelier : {}", atelier);
        return atelierRepository.save(atelier);
    }

    @Override
    public Optional<Atelier> partialUpdate(Atelier atelier) {
        log.debug("Request to partially update Atelier : {}", atelier);

        return atelierRepository
            .findById(atelier.getId())
            .map(existingAtelier -> {
                if (atelier.getCompteurChgtMoteur() != null) {
                    existingAtelier.setCompteurChgtMoteur(atelier.getCompteurChgtMoteur());
                }
                if (atelier.getCompteurCarrosserie() != null) {
                    existingAtelier.setCompteurCarrosserie(atelier.getCompteurCarrosserie());
                }
                if (atelier.getCompteurHelisse() != null) {
                    existingAtelier.setCompteurHelisse(atelier.getCompteurHelisse());
                }
                if (atelier.getDate() != null) {
                    existingAtelier.setDate(atelier.getDate());
                }

                return existingAtelier;
            })
            .map(atelierRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Atelier> findAll() {
        log.debug("Request to get all Ateliers");
        return atelierRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Atelier> findOne(Long id) {
        log.debug("Request to get Atelier : {}", id);
        return atelierRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Atelier : {}", id);
        atelierRepository.deleteById(id);
    }
}
