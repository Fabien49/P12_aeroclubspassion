package com.fabienit.aeroclubspassion.service;

import com.fabienit.aeroclubspassion.domain.Tarif;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Tarif}.
 */
public interface TarifService {
    /**
     * Save a tarif.
     *
     * @param tarif the entity to save.
     * @return the persisted entity.
     */
    Tarif save(Tarif tarif);

    /**
     * Partially updates a tarif.
     *
     * @param tarif the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Tarif> partialUpdate(Tarif tarif);

    /**
     * Get all the tarifs.
     *
     * @return the list of entities.
     */
    List<Tarif> findAll();

    /**
     * Get the "id" tarif.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tarif> findOne(Long id);

    /**
     * Delete the "id" tarif.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
