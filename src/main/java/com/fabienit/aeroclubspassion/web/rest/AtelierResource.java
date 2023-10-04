package com.fabienit.aeroclubspassion.web.rest;

import com.fabienit.aeroclubspassion.domain.Atelier;
import com.fabienit.aeroclubspassion.repository.AtelierRepository;
import com.fabienit.aeroclubspassion.service.AtelierService;
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
 * REST controller for managing {@link com.fabienit.aeroclubspassion.domain.Atelier}.
 */
@RestController
@RequestMapping("/api")
public class AtelierResource {

    private final Logger log = LoggerFactory.getLogger(AtelierResource.class);

    private static final String ENTITY_NAME = "atelier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AtelierService atelierService;

    private final AtelierRepository atelierRepository;

    public AtelierResource(AtelierService atelierService, AtelierRepository atelierRepository) {
        this.atelierService = atelierService;
        this.atelierRepository = atelierRepository;
    }

    /**
     * {@code POST  /ateliers} : Create a new atelier.
     *
     * @param atelier the atelier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new atelier, or with status {@code 400 (Bad Request)} if the atelier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ateliers")
    public ResponseEntity<Atelier> createAtelier(@RequestBody Atelier atelier) throws URISyntaxException {
        log.debug("REST request to save Atelier : {}", atelier);
        if (atelier.getId() != null) {
            throw new BadRequestAlertException("A new atelier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Atelier result = atelierService.save(atelier);
        return ResponseEntity
            .created(new URI("/api/ateliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ateliers/:id} : Updates an existing atelier.
     *
     * @param id the id of the atelier to save.
     * @param atelier the atelier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated atelier,
     * or with status {@code 400 (Bad Request)} if the atelier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the atelier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ateliers/{id}")
    public ResponseEntity<Atelier> updateAtelier(@PathVariable(value = "id", required = false) final Long id, @RequestBody Atelier atelier)
        throws URISyntaxException {
        log.debug("REST request to update Atelier : {}, {}", id, atelier);
        if (atelier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, atelier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!atelierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Atelier result = atelierService.save(atelier);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, atelier.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ateliers/:id} : Partial updates given fields of an existing atelier, field will ignore if it is null
     *
     * @param id the id of the atelier to save.
     * @param atelier the atelier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated atelier,
     * or with status {@code 400 (Bad Request)} if the atelier is not valid,
     * or with status {@code 404 (Not Found)} if the atelier is not found,
     * or with status {@code 500 (Internal Server Error)} if the atelier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ateliers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Atelier> partialUpdateAtelier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Atelier atelier
    ) throws URISyntaxException {
        log.debug("REST request to partial update Atelier partially : {}, {}", id, atelier);
        if (atelier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, atelier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!atelierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Atelier> result = atelierService.partialUpdate(atelier);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, atelier.getId().toString())
        );
    }

    /**
     * {@code GET  /ateliers} : get all the ateliers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ateliers in body.
     */
    @GetMapping("/ateliers")
    public List<Atelier> getAllAteliers() {
        log.debug("REST request to get all Ateliers");
        return atelierService.findAll();
    }

    /**
     * {@code GET  /ateliers/:id} : get the "id" atelier.
     *
     * @param id the id of the atelier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the atelier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ateliers/{id}")
    public ResponseEntity<Atelier> getAtelier(@PathVariable Long id) {
        log.debug("REST request to get Atelier : {}", id);
        Optional<Atelier> atelier = atelierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(atelier);
    }

    /**
     * {@code DELETE  /ateliers/:id} : delete the "id" atelier.
     *
     * @param id the id of the atelier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ateliers/{id}")
    public ResponseEntity<Void> deleteAtelier(@PathVariable Long id) {
        log.debug("REST request to delete Atelier : {}", id);
        atelierService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
