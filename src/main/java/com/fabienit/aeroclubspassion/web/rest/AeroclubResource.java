package com.fabienit.aeroclubspassion.web.rest;

import com.fabienit.aeroclubspassion.domain.Aeroclub;
import com.fabienit.aeroclubspassion.repository.AeroclubRepository;
import com.fabienit.aeroclubspassion.service.AeroclubService;
import com.fabienit.aeroclubspassion.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.fabienit.aeroclubspassion.domain.Aeroclub}.
 */
@RestController
@RequestMapping("/api")
public class AeroclubResource {

    private final Logger log = LoggerFactory.getLogger(AeroclubResource.class);

    private static final String ENTITY_NAME = "aeroclub";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AeroclubService aeroclubService;

    private final AeroclubRepository aeroclubRepository;

    public AeroclubResource(AeroclubService aeroclubService, AeroclubRepository aeroclubRepository) {
        this.aeroclubService = aeroclubService;
        this.aeroclubRepository = aeroclubRepository;
    }

    /**
     * {@code POST  /aeroclubs} : Create a new aeroclub.
     *
     * @param aeroclub the aeroclub to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aeroclub, or with status {@code 400 (Bad Request)} if the aeroclub has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aeroclubs")
    public ResponseEntity<Aeroclub> createAeroclub(@Valid @RequestBody Aeroclub aeroclub) throws URISyntaxException {
        log.debug("REST request to save Aeroclub : {}", aeroclub);
        if (aeroclub.getId() != null) {
            throw new BadRequestAlertException("A new aeroclub cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Aeroclub result = aeroclubService.save(aeroclub);
        return ResponseEntity
            .created(new URI("/api/aeroclubs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aeroclubs/:id} : Updates an existing aeroclub.
     *
     * @param id the id of the aeroclub to save.
     * @param aeroclub the aeroclub to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aeroclub,
     * or with status {@code 400 (Bad Request)} if the aeroclub is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aeroclub couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aeroclubs/{id}")
    public ResponseEntity<Aeroclub> updateAeroclub(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Aeroclub aeroclub
    ) throws URISyntaxException {
        log.debug("REST request to update Aeroclub : {}, {}", id, aeroclub);
        if (aeroclub.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aeroclub.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aeroclubRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Aeroclub result = aeroclubService.save(aeroclub);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aeroclub.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aeroclubs/:id} : Partial updates given fields of an existing aeroclub, field will ignore if it is null
     *
     * @param id the id of the aeroclub to save.
     * @param aeroclub the aeroclub to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aeroclub,
     * or with status {@code 400 (Bad Request)} if the aeroclub is not valid,
     * or with status {@code 404 (Not Found)} if the aeroclub is not found,
     * or with status {@code 500 (Internal Server Error)} if the aeroclub couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aeroclubs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Aeroclub> partialUpdateAeroclub(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Aeroclub aeroclub
    ) throws URISyntaxException {
        log.debug("REST request to partial update Aeroclub partially : {}, {}", id, aeroclub);
        if (aeroclub.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aeroclub.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aeroclubRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Aeroclub> result = aeroclubService.partialUpdate(aeroclub);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aeroclub.getId().toString())
        );
    }

    /**
     * {@code GET  /aeroclubs} : get all the aeroclubs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aeroclubs in body.
     */
    @GetMapping("/aeroclubs")
    public ResponseEntity<List<Aeroclub>> getAllAeroclubs(Pageable pageable) {
        log.debug("REST request to get a page of Aeroclubs");
        Page<Aeroclub> page = aeroclubService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aeroclubs/:id} : get the "id" aeroclub.
     *
     * @param id the id of the aeroclub to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aeroclub, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aeroclubs/{id}")
    public ResponseEntity<Aeroclub> getAeroclub(@PathVariable Long id) {
        log.debug("REST request to get Aeroclub : {}", id);
        Optional<Aeroclub> aeroclub = aeroclubService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aeroclub);
    }

    /**
     * {@code DELETE  /aeroclubs/:id} : delete the "id" aeroclub.
     *
     * @param id the id of the aeroclub to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aeroclubs/{id}")
    public ResponseEntity<Void> deleteAeroclub(@PathVariable Long id) {
        log.debug("REST request to delete Aeroclub : {}", id);
        aeroclubService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
