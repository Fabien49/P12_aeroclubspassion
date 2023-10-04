package com.fabienit.aeroclubspassion.service;

import com.fabienit.aeroclubspassion.domain.Revision;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Revision}.
 */
public interface RevisionService {
    /**
     * Save a revision.
     *
     * @param revision the entity to save.
     * @return the persisted entity.
     */
    Revision save(Revision revision);

    /**
     * Partially updates a revision.
     *
     * @param revision the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Revision> partialUpdate(Revision revision);

    /**
     * Get all the revisions.
     *
     * @return the list of entities.
     */
    List<Revision> findAll();

    /**
     * Get the "id" revision.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Revision> findOne(Long id);

    /**
     * Delete the "id" revision.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
