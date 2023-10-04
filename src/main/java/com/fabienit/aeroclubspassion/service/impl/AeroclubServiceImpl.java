package com.fabienit.aeroclubspassion.service.impl;

import com.fabienit.aeroclubspassion.domain.Aeroclub;
import com.fabienit.aeroclubspassion.repository.AeroclubRepository;
import com.fabienit.aeroclubspassion.service.AeroclubService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Aeroclub}.
 */
@Service
@Transactional
public class AeroclubServiceImpl implements AeroclubService {

    private final Logger log = LoggerFactory.getLogger(AeroclubServiceImpl.class);

    private final AeroclubRepository aeroclubRepository;

    public AeroclubServiceImpl(AeroclubRepository aeroclubRepository) {
        this.aeroclubRepository = aeroclubRepository;
    }

    @Override
    public Aeroclub save(Aeroclub aeroclub) {
        log.debug("Request to save Aeroclub : {}", aeroclub);
        return aeroclubRepository.save(aeroclub);
    }

    @Override
    public Optional<Aeroclub> partialUpdate(Aeroclub aeroclub) {
        log.debug("Request to partially update Aeroclub : {}", aeroclub);

        return aeroclubRepository
            .findById(aeroclub.getId())
            .map(existingAeroclub -> {
                if (aeroclub.getOaci() != null) {
                    existingAeroclub.setOaci(aeroclub.getOaci());
                }
                if (aeroclub.getName() != null) {
                    existingAeroclub.setName(aeroclub.getName());
                }
                if (aeroclub.getType() != null) {
                    existingAeroclub.setType(aeroclub.getType());
                }
                if (aeroclub.getPhoneNumber() != null) {
                    existingAeroclub.setPhoneNumber(aeroclub.getPhoneNumber());
                }
                if (aeroclub.getMail() != null) {
                    existingAeroclub.setMail(aeroclub.getMail());
                }
                if (aeroclub.getAdresse() != null) {
                    existingAeroclub.setAdresse(aeroclub.getAdresse());
                }
                if (aeroclub.getCodePostal() != null) {
                    existingAeroclub.setCodePostal(aeroclub.getCodePostal());
                }
                if (aeroclub.getCommune() != null) {
                    existingAeroclub.setCommune(aeroclub.getCommune());
                }

                return existingAeroclub;
            })
            .map(aeroclubRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Aeroclub> findAll(Pageable pageable) {
        log.debug("Request to get all Aeroclubs");
        return aeroclubRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aeroclub> findOne(Long id) {
        log.debug("Request to get Aeroclub : {}", id);
        return aeroclubRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Aeroclub : {}", id);
        aeroclubRepository.deleteById(id);
    }
}
