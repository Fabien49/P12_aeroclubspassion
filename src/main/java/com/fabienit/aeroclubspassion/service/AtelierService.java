package com.fabienit.aeroclubspassion.service;

import com.fabienit.aeroclubspassion.domain.Atelier;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Atelier}.
 */
public interface AtelierService {
    /**
     * Save a atelier.
     *
     * @param atelier the entity to save.
     * @return the persisted entity.
     */
    Atelier save(Atelier atelier);

    /**
     * Partially updates a atelier.
     *
     * @param atelier the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Atelier> partialUpdate(Atelier atelier);

    /**
     * Get all the ateliers.
     *
     * @return the list of entities.
     */
    List<Atelier> findAll();

    /**
     * Get the "id" atelier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Atelier> findOne(Long id);

    /**
     * Delete the "id" atelier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
