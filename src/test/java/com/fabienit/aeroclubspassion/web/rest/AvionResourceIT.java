package com.fabienit.aeroclubspassion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fabienit.aeroclubspassion.IntegrationTest;
import com.fabienit.aeroclubspassion.domain.Avion;
import com.fabienit.aeroclubspassion.repository.AvionRepository;
import java.time.Duration;
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
 * Integration tests for the {@link AvionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AvionResourceIT {

    private static final String DEFAULT_MARQUE = "AAAAAAAAAA";
    private static final String UPDATED_MARQUE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MOTEUR = "AAAAAAAAAA";
    private static final String UPDATED_MOTEUR = "BBBBBBBBBB";

    private static final Integer DEFAULT_PUISSANCE = 1;
    private static final Integer UPDATED_PUISSANCE = 2;

    private static final Integer DEFAULT_PLACE = 1;
    private static final Integer UPDATED_PLACE = 2;

    private static final Duration DEFAULT_AUTONOMIE = Duration.ofHours(6);
    private static final Duration UPDATED_AUTONOMIE = Duration.ofHours(12);

    private static final String DEFAULT_USAGE = "AAAAAAAAAA";
    private static final String UPDATED_USAGE = "BBBBBBBBBB";

    private static final Duration DEFAULT_HEURES = Duration.ofHours(6);
    private static final Duration UPDATED_HEURES = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/avions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvionMockMvc;

    private Avion avion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Avion createEntity(EntityManager em) {
        Avion avion = new Avion()
            .marque(DEFAULT_MARQUE)
            .type(DEFAULT_TYPE)
            .moteur(DEFAULT_MOTEUR)
            .puissance(DEFAULT_PUISSANCE)
            .place(DEFAULT_PLACE)
            .autonomie(DEFAULT_AUTONOMIE)
            .usage(DEFAULT_USAGE)
            .heures(DEFAULT_HEURES);
        return avion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Avion createUpdatedEntity(EntityManager em) {
        Avion avion = new Avion()
            .marque(UPDATED_MARQUE)
            .type(UPDATED_TYPE)
            .moteur(UPDATED_MOTEUR)
            .puissance(UPDATED_PUISSANCE)
            .place(UPDATED_PLACE)
            .autonomie(UPDATED_AUTONOMIE)
            .usage(UPDATED_USAGE)
            .heures(UPDATED_HEURES);
        return avion;
    }

    @BeforeEach
    public void initTest() {
        avion = createEntity(em);
    }

    @Test
    @Transactional
    void createAvion() throws Exception {
        int databaseSizeBeforeCreate = avionRepository.findAll().size();
        // Create the Avion
        restAvionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avion)))
            .andExpect(status().isCreated());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeCreate + 1);
        Avion testAvion = avionList.get(avionList.size() - 1);
        assertThat(testAvion.getMarque()).isEqualTo(DEFAULT_MARQUE);
        assertThat(testAvion.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAvion.getMoteur()).isEqualTo(DEFAULT_MOTEUR);
        assertThat(testAvion.getPuissance()).isEqualTo(DEFAULT_PUISSANCE);
        assertThat(testAvion.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testAvion.getAutonomie()).isEqualTo(DEFAULT_AUTONOMIE);
        assertThat(testAvion.getUsage()).isEqualTo(DEFAULT_USAGE);
        assertThat(testAvion.getHeures()).isEqualTo(DEFAULT_HEURES);
    }

    @Test
    @Transactional
    void createAvionWithExistingId() throws Exception {
        // Create the Avion with an existing ID
        avion.setId(1L);

        int databaseSizeBeforeCreate = avionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avion)))
            .andExpect(status().isBadRequest());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMarqueIsRequired() throws Exception {
        int databaseSizeBeforeTest = avionRepository.findAll().size();
        // set the field null
        avion.setMarque(null);

        // Create the Avion, which fails.

        restAvionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avion)))
            .andExpect(status().isBadRequest());

        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAvions() throws Exception {
        // Initialize the database
        avionRepository.saveAndFlush(avion);

        // Get all the avionList
        restAvionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avion.getId().intValue())))
            .andExpect(jsonPath("$.[*].marque").value(hasItem(DEFAULT_MARQUE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].moteur").value(hasItem(DEFAULT_MOTEUR)))
            .andExpect(jsonPath("$.[*].puissance").value(hasItem(DEFAULT_PUISSANCE)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].autonomie").value(hasItem(DEFAULT_AUTONOMIE.toString())))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE)))
            .andExpect(jsonPath("$.[*].heures").value(hasItem(DEFAULT_HEURES.toString())));
    }

    @Test
    @Transactional
    void getAvion() throws Exception {
        // Initialize the database
        avionRepository.saveAndFlush(avion);

        // Get the avion
        restAvionMockMvc
            .perform(get(ENTITY_API_URL_ID, avion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(avion.getId().intValue()))
            .andExpect(jsonPath("$.marque").value(DEFAULT_MARQUE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.moteur").value(DEFAULT_MOTEUR))
            .andExpect(jsonPath("$.puissance").value(DEFAULT_PUISSANCE))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE))
            .andExpect(jsonPath("$.autonomie").value(DEFAULT_AUTONOMIE.toString()))
            .andExpect(jsonPath("$.usage").value(DEFAULT_USAGE))
            .andExpect(jsonPath("$.heures").value(DEFAULT_HEURES.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAvion() throws Exception {
        // Get the avion
        restAvionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAvion() throws Exception {
        // Initialize the database
        avionRepository.saveAndFlush(avion);

        int databaseSizeBeforeUpdate = avionRepository.findAll().size();

        // Update the avion
        Avion updatedAvion = avionRepository.findById(avion.getId()).get();
        // Disconnect from session so that the updates on updatedAvion are not directly saved in db
        em.detach(updatedAvion);
        updatedAvion
            .marque(UPDATED_MARQUE)
            .type(UPDATED_TYPE)
            .moteur(UPDATED_MOTEUR)
            .puissance(UPDATED_PUISSANCE)
            .place(UPDATED_PLACE)
            .autonomie(UPDATED_AUTONOMIE)
            .usage(UPDATED_USAGE)
            .heures(UPDATED_HEURES);

        restAvionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAvion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAvion))
            )
            .andExpect(status().isOk());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeUpdate);
        Avion testAvion = avionList.get(avionList.size() - 1);
        assertThat(testAvion.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testAvion.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAvion.getMoteur()).isEqualTo(UPDATED_MOTEUR);
        assertThat(testAvion.getPuissance()).isEqualTo(UPDATED_PUISSANCE);
        assertThat(testAvion.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testAvion.getAutonomie()).isEqualTo(UPDATED_AUTONOMIE);
        assertThat(testAvion.getUsage()).isEqualTo(UPDATED_USAGE);
        assertThat(testAvion.getHeures()).isEqualTo(UPDATED_HEURES);
    }

    @Test
    @Transactional
    void putNonExistingAvion() throws Exception {
        int databaseSizeBeforeUpdate = avionRepository.findAll().size();
        avion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, avion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvion() throws Exception {
        int databaseSizeBeforeUpdate = avionRepository.findAll().size();
        avion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvion() throws Exception {
        int databaseSizeBeforeUpdate = avionRepository.findAll().size();
        avion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvionWithPatch() throws Exception {
        // Initialize the database
        avionRepository.saveAndFlush(avion);

        int databaseSizeBeforeUpdate = avionRepository.findAll().size();

        // Update the avion using partial update
        Avion partialUpdatedAvion = new Avion();
        partialUpdatedAvion.setId(avion.getId());

        partialUpdatedAvion.marque(UPDATED_MARQUE).puissance(UPDATED_PUISSANCE).autonomie(UPDATED_AUTONOMIE).usage(UPDATED_USAGE);

        restAvionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvion))
            )
            .andExpect(status().isOk());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeUpdate);
        Avion testAvion = avionList.get(avionList.size() - 1);
        assertThat(testAvion.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testAvion.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAvion.getMoteur()).isEqualTo(DEFAULT_MOTEUR);
        assertThat(testAvion.getPuissance()).isEqualTo(UPDATED_PUISSANCE);
        assertThat(testAvion.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testAvion.getAutonomie()).isEqualTo(UPDATED_AUTONOMIE);
        assertThat(testAvion.getUsage()).isEqualTo(UPDATED_USAGE);
        assertThat(testAvion.getHeures()).isEqualTo(DEFAULT_HEURES);
    }

    @Test
    @Transactional
    void fullUpdateAvionWithPatch() throws Exception {
        // Initialize the database
        avionRepository.saveAndFlush(avion);

        int databaseSizeBeforeUpdate = avionRepository.findAll().size();

        // Update the avion using partial update
        Avion partialUpdatedAvion = new Avion();
        partialUpdatedAvion.setId(avion.getId());

        partialUpdatedAvion
            .marque(UPDATED_MARQUE)
            .type(UPDATED_TYPE)
            .moteur(UPDATED_MOTEUR)
            .puissance(UPDATED_PUISSANCE)
            .place(UPDATED_PLACE)
            .autonomie(UPDATED_AUTONOMIE)
            .usage(UPDATED_USAGE)
            .heures(UPDATED_HEURES);

        restAvionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvion))
            )
            .andExpect(status().isOk());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeUpdate);
        Avion testAvion = avionList.get(avionList.size() - 1);
        assertThat(testAvion.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testAvion.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAvion.getMoteur()).isEqualTo(UPDATED_MOTEUR);
        assertThat(testAvion.getPuissance()).isEqualTo(UPDATED_PUISSANCE);
        assertThat(testAvion.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testAvion.getAutonomie()).isEqualTo(UPDATED_AUTONOMIE);
        assertThat(testAvion.getUsage()).isEqualTo(UPDATED_USAGE);
        assertThat(testAvion.getHeures()).isEqualTo(UPDATED_HEURES);
    }

    @Test
    @Transactional
    void patchNonExistingAvion() throws Exception {
        int databaseSizeBeforeUpdate = avionRepository.findAll().size();
        avion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, avion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvion() throws Exception {
        int databaseSizeBeforeUpdate = avionRepository.findAll().size();
        avion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvion() throws Exception {
        int databaseSizeBeforeUpdate = avionRepository.findAll().size();
        avion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(avion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvion() throws Exception {
        // Initialize the database
        avionRepository.saveAndFlush(avion);

        int databaseSizeBeforeDelete = avionRepository.findAll().size();

        // Delete the avion
        restAvionMockMvc
            .perform(delete(ENTITY_API_URL_ID, avion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
