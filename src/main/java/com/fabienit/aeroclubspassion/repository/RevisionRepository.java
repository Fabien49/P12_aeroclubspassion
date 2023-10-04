package com.fabienit.aeroclubspassion.repository;

import com.fabienit.aeroclubspassion.domain.Revision;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Revision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {}
