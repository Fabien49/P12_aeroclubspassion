package com.fabienit.aeroclubspassion.service;

import com.fabienit.aeroclubspassion.domain.Aeroclub;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Aeroclub}.
 */
public interface AeroclubService {
    /**
     * Save a aeroclub.
     *
     * @param aeroclub the entity to save.
     * @return the persisted entity.
     */
    Aeroclub save(Aeroclub aeroclub);

    /**
     * Partially updates a aeroclub.
     *
     * @param aeroclub the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Aeroclub> partialUpdate(Aeroclub aeroclub);

    /**
     * Get all the aeroclubs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Aeroclub> findAll(Pageable pageable);

    /**
     * Get the "id" aeroclub.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Aeroclub> findOne(Long id);

    /**
     * Delete the "id" aeroclub.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
