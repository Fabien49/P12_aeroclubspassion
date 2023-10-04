package com.fabienit.aeroclubspassion.web.rest;

import com.fabienit.aeroclubspassion.domain.Revision;
import com.fabienit.aeroclubspassion.repository.RevisionRepository;
import com.fabienit.aeroclubspassion.service.RevisionService;
import com.fabienit.aeroclubspassion.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.fabienit.aeroclubspassion.domain.Revision}.
 */
@RestController
@RequestMapping("/api")
public class RevisionResource {

    private final Logger log = LoggerFactory.getLogger(RevisionResource.class);

    private static final String ENTITY_NAME = "revision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RevisionService revisionService;

    private final RevisionRepository revisionRepository;

    public RevisionResource(RevisionService revisionService, RevisionRepository revisionRepository) {
        this.revisionService = revisionService;
        this.revisionRepository = revisionRepository;
    }

    /**
     * {@code POST  /revisions} : Create a new revision.
     *
     * @param revision the revision to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new revision, or with status {@code 400 (Bad Request)} if the revision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/revisions")
    public ResponseEntity<Revision> createRevision(@RequestBody Revision revision) throws URISyntaxException {
        log.debug("REST request to save Revision : {}", revision);
        if (revision.getId() != null) {
            throw new BadRequestAlertException("A new revision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Revision result = revisionService.save(revision);
        return ResponseEntity
            .created(new URI("/api/revisions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /revisions/:id} : Updates an existing revision.
     *
     * @param id the id of the revision to save.
     * @param revision the revision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated revision,
     * or with status {@code 400 (Bad Request)} if the revision is not valid,
     * or with status {@code 500 (Internal Server Error)} if the revision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/revisions/{id}")
    public ResponseEntity<Revision> updateRevision(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Revision revision
    ) throws URISyntaxException {
        log.debug("REST request to update Revision : {}, {}", id, revision);
        if (revision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, revision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!revisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Revision result = revisionService.save(revision);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, revision.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /revisions/:id} : Partial updates given fields of an existing revision, field will ignore if it is null
     *
     * @param id the id of the revision to save.
     * @param revision the revision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated revision,
     * or with status {@code 400 (Bad Request)} if the revision is not valid,
     * or with status {@code 404 (Not Found)} if the revision is not found,
     * or with status {@code 500 (Internal Server Error)} if the revision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/revisions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Revision> partialUpdateRevision(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Revision revision
    ) throws URISyntaxException {
        log.debug("REST request to partial update Revision partially : {}, {}", id, revision);
        if (revision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, revision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!revisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Revision> result = revisionService.partialUpdate(revision);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, revision.getId().toString())
        );
    }

    /**
     * {@code GET  /revisions} : get all the revisions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of revisions in body.
     */
    @GetMapping("/revisions")
    public List<Revision> getAllRevisions() {
        log.debug("REST request to get all Revisions");
        return revisionService.findAll();
    }

    /**
     * {@code GET  /revisions/:id} : get the "id" revision.
     *
     * @param id the id of the revision to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the revision, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/revisions/{id}")
    public ResponseEntity<Revision> getRevision(@PathVariable Long id) {
        log.debug("REST request to get Revision : {}", id);
        Optional<Revision> revision = revisionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(revision);
    }

    /**
     * {@code DELETE  /revisions/:id} : delete the "id" revision.
     *
     * @param id the id of the revision to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/revisions/{id}")
    public ResponseEntity<Void> deleteRevision(@PathVariable Long id) {
        log.debug("REST request to delete Revision : {}", id);
        revisionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
