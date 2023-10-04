package com.fabienit.aeroclubspassion.service.impl;

import com.fabienit.aeroclubspassion.domain.Tarif;
import com.fabienit.aeroclubspassion.repository.TarifRepository;
import com.fabienit.aeroclubspassion.service.TarifService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tarif}.
 */
@Service
@Transactional
public class TarifServiceImpl implements TarifService {

    private final Logger log = LoggerFactory.getLogger(TarifServiceImpl.class);

    private final TarifRepository tarifRepository;

    public TarifServiceImpl(TarifRepository tarifRepository) {
        this.tarifRepository = tarifRepository;
    }

    @Override
    public Tarif save(Tarif tarif) {
        log.debug("Request to save Tarif : {}", tarif);
        return tarifRepository.save(tarif);
    }

    @Override
    public Optional<Tarif> partialUpdate(Tarif tarif) {
        log.debug("Request to partially update Tarif : {}", tarif);

        return tarifRepository
            .findById(tarif.getId())
            .map(existingTarif -> {
                if (tarif.getTaxeAtterrissage() != null) {
                    existingTarif.setTaxeAtterrissage(tarif.getTaxeAtterrissage());
                }
                if (tarif.getTaxeParking() != null) {
                    existingTarif.setTaxeParking(tarif.getTaxeParking());
                }
                if (tarif.getCarburant() != null) {
                    existingTarif.setCarburant(tarif.getCarburant());
                }

                return existingTarif;
            })
            .map(tarifRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tarif> findAll() {
        log.debug("Request to get all Tarifs");
        return tarifRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tarif> findOne(Long id) {
        log.debug("Request to get Tarif : {}", id);
        return tarifRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tarif : {}", id);
        tarifRepository.deleteById(id);
    }
}
