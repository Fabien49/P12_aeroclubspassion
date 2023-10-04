package com.fabienit.aeroclubspassion.repository;

import com.fabienit.aeroclubspassion.domain.Atelier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Atelier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AtelierRepository extends JpaRepository<Atelier, Long> {}
