package com.fabienit.aeroclubspassion.web.rest;

import static com.fabienit.aeroclubspassion.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fabienit.aeroclubspassion.IntegrationTest;
import com.fabienit.aeroclubspassion.domain.Revision;
import com.fabienit.aeroclubspassion.repository.RevisionRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RevisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RevisionResourceIT {

    private static final Boolean DEFAULT_NIVEAUX = false;
    private static final Boolean UPDATED_NIVEAUX = true;

    private static final Boolean DEFAULT_PRESSION = false;
    private static final Boolean UPDATED_PRESSION = true;

    private static final Boolean DEFAULT_CARROSERIE = false;
    private static final Boolean UPDATED_CARROSERIE = true;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/revisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RevisionRepository revisionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRevisionMockMvc;

    private Revision revision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Revision createEntity(EntityManager em) {
        Revision revision = new Revision()
            .niveaux(DEFAULT_NIVEAUX)
            .pression(DEFAULT_PRESSION)
            .carroserie(DEFAULT_CARROSERIE)
            .date(DEFAULT_DATE);
        return revision;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Revision createUpdatedEntity(EntityManager em) {
        Revision revision = new Revision()
            .niveaux(UPDATED_NIVEAUX)
            .pression(UPDATED_PRESSION)
            .carroserie(UPDATED_CARROSERIE)
            .date(UPDATED_DATE);
        return revision;
    }

    @BeforeEach
    public void initTest() {
        revision = createEntity(em);
    }

    @Test
    @Transactional
    void createRevision() throws Exception {
        int databaseSizeBeforeCreate = revisionRepository.findAll().size();
        // Create the Revision
        restRevisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revision)))
            .andExpect(status().isCreated());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeCreate + 1);
        Revision testRevision = revisionList.get(revisionList.size() - 1);
        assertThat(testRevision.getNiveaux()).isEqualTo(DEFAULT_NIVEAUX);
        assertThat(testRevision.getPression()).isEqualTo(DEFAULT_PRESSION);
        assertThat(testRevision.getCarroserie()).isEqualTo(DEFAULT_CARROSERIE);
        assertThat(testRevision.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createRevisionWithExistingId() throws Exception {
        // Create the Revision with an existing ID
        revision.setId(1L);

        int databaseSizeBeforeCreate = revisionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revision)))
            .andExpect(status().isBadRequest());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRevisions() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList
        restRevisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revision.getId().intValue())))
            .andExpect(jsonPath("$.[*].niveaux").value(hasItem(DEFAULT_NIVEAUX.booleanValue())))
            .andExpect(jsonPath("$.[*].pression").value(hasItem(DEFAULT_PRESSION.booleanValue())))
            .andExpect(jsonPath("$.[*].carroserie").value(hasItem(DEFAULT_CARROSERIE.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    void getRevision() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get the revision
        restRevisionMockMvc
            .perform(get(ENTITY_API_URL_ID, revision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(revision.getId().intValue()))
            .andExpect(jsonPath("$.niveaux").value(DEFAULT_NIVEAUX.booleanValue()))
            .andExpect(jsonPath("$.pression").value(DEFAULT_PRESSION.booleanValue()))
            .andExpect(jsonPath("$.carroserie").value(DEFAULT_CARROSERIE.booleanValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingRevision() throws Exception {
        // Get the revision
        restRevisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRevision() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();

        // Update the revision
        Revision updatedRevision = revisionRepository.findById(revision.getId()).get();
        // Disconnect from session so that the updates on updatedRevision are not directly saved in db
        em.detach(updatedRevision);
        updatedRevision.niveaux(UPDATED_NIVEAUX).pression(UPDATED_PRESSION).carroserie(UPDATED_CARROSERIE).date(UPDATED_DATE);

        restRevisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRevision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRevision))
            )
            .andExpect(status().isOk());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
        Revision testRevision = revisionList.get(revisionList.size() - 1);
        assertThat(testRevision.getNiveaux()).isEqualTo(UPDATED_NIVEAUX);
        assertThat(testRevision.getPression()).isEqualTo(UPDATED_PRESSION);
        assertThat(testRevision.getCarroserie()).isEqualTo(UPDATED_CARROSERIE);
        assertThat(testRevision.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingRevision() throws Exception {
        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();
        revision.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRevisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, revision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(revision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRevision() throws Exception {
        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();
        revision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(revision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRevision() throws Exception {
        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();
        revision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revision)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRevisionWithPatch() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();

        // Update the revision using partial update
        Revision partialUpdatedRevision = new Revision();
        partialUpdatedRevision.setId(revision.getId());

        partialUpdatedRevision.carroserie(UPDATED_CARROSERIE);

        restRevisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRevision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRevision))
            )
            .andExpect(status().isOk());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
        Revision testRevision = revisionList.get(revisionList.size() - 1);
        assertThat(testRevision.getNiveaux()).isEqualTo(DEFAULT_NIVEAUX);
        assertThat(testRevision.getPression()).isEqualTo(DEFAULT_PRESSION);
        assertThat(testRevision.getCarroserie()).isEqualTo(UPDATED_CARROSERIE);
        assertThat(testRevision.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateRevisionWithPatch() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();

        // Update the revision using partial update
        Revision partialUpdatedRevision = new Revision();
        partialUpdatedRevision.setId(revision.getId());

        partialUpdatedRevision.niveaux(UPDATED_NIVEAUX).pression(UPDATED_PRESSION).carroserie(UPDATED_CARROSERIE).date(UPDATED_DATE);

        restRevisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRevision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRevision))
            )
            .andExpect(status().isOk());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
        Revision testRevision = revisionList.get(revisionList.size() - 1);
        assertThat(testRevision.getNiveaux()).isEqualTo(UPDATED_NIVEAUX);
        assertThat(testRevision.getPression()).isEqualTo(UPDATED_PRESSION);
        assertThat(testRevision.getCarroserie()).isEqualTo(UPDATED_CARROSERIE);
        assertThat(testRevision.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingRevision() throws Exception {
        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();
        revision.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRevisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, revision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(revision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRevision() throws Exception {
        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();
        revision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(revision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRevision() throws Exception {
        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();
        revision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevisionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(revision)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRevision() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        int databaseSizeBeforeDelete = revisionRepository.findAll().size();

        // Delete the revision
        restRevisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, revision.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
