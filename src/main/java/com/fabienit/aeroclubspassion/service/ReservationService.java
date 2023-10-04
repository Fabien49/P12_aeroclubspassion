package com.fabienit.aeroclubspassion.service;

import com.fabienit.aeroclubspassion.domain.Reservation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Reservation}.
 */
public interface ReservationService {
    /**
     * Save a reservation.
     *
     * @param reservation the entity to save.
     * @return the persisted entity.
     */
    Reservation save(Reservation reservation);

    /**
     * Partially updates a reservation.
     *
     * @param reservation the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Reservation> partialUpdate(Reservation reservation);

    /**
     * Get all the reservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Reservation> findAll(Pageable pageable);

    /**
     * Get the "id" reservation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Reservation> findOne(Long id);

    /**
     * Delete the "id" reservation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
