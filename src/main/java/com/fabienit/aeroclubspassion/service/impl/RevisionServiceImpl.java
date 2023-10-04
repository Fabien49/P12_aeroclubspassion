package com.fabienit.aeroclubspassion.service.impl;

import com.fabienit.aeroclubspassion.domain.Revision;
import com.fabienit.aeroclubspassion.repository.RevisionRepository;
import com.fabienit.aeroclubspassion.service.RevisionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Revision}.
 */
@Service
@Transactional
public class RevisionServiceImpl implements RevisionService {

    private final Logger log = LoggerFactory.getLogger(RevisionServiceImpl.class);

    private final RevisionRepository revisionRepository;

    public RevisionServiceImpl(RevisionRepository revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    @Override
    public Revision save(Revision revision) {
        log.debug("Request to save Revision : {}", revision);
        return revisionRepository.save(revision);
    }

    @Override
    public Optional<Revision> partialUpdate(Revision revision) {
        log.debug("Request to partially update Revision : {}", revision);

        return revisionRepository
            .findById(revision.getId())
            .map(existingRevision -> {
                if (revision.getNiveaux() != null) {
                    existingRevision.setNiveaux(revision.getNiveaux());
                }
                if (revision.getPression() != null) {
                    existingRevision.setPression(revision.getPression());
                }
                if (revision.getCarroserie() != null) {
                    existingRevision.setCarroserie(revision.getCarroserie());
                }
                if (revision.getDate() != null) {
                    existingRevision.setDate(revision.getDate());
                }

                return existingRevision;
            })
            .map(revisionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Revision> findAll() {
        log.debug("Request to get all Revisions");
        return revisionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Revision> findOne(Long id) {
        log.debug("Request to get Revision : {}", id);
        return revisionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Revision : {}", id);
        revisionRepository.deleteById(id);
    }
}
