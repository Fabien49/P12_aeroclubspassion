package com.fabienit.aeroclubspassion.web.rest;

import static com.fabienit.aeroclubspassion.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fabienit.aeroclubspassion.IntegrationTest;
import com.fabienit.aeroclubspassion.domain.Atelier;
import com.fabienit.aeroclubspassion.repository.AtelierRepository;
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
 * Integration tests for the {@link AtelierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AtelierResourceIT {

    private static final Integer DEFAULT_COMPTEUR_CHGT_MOTEUR = 1;
    private static final Integer UPDATED_COMPTEUR_CHGT_MOTEUR = 2;

    private static final Integer DEFAULT_COMPTEUR_CARROSSERIE = 1;
    private static final Integer UPDATED_COMPTEUR_CARROSSERIE = 2;

    private static final Integer DEFAULT_COMPTEUR_HELISSE = 1;
    private static final Integer UPDATED_COMPTEUR_HELISSE = 2;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/ateliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AtelierRepository atelierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAtelierMockMvc;

    private Atelier atelier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Atelier createEntity(EntityManager em) {
        Atelier atelier = new Atelier()
            .compteurChgtMoteur(DEFAULT_COMPTEUR_CHGT_MOTEUR)
            .compteurCarrosserie(DEFAULT_COMPTEUR_CARROSSERIE)
            .compteurHelisse(DEFAULT_COMPTEUR_HELISSE)
            .date(DEFAULT_DATE);
        return atelier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Atelier createUpdatedEntity(EntityManager em) {
        Atelier atelier = new Atelier()
            .compteurChgtMoteur(UPDATED_COMPTEUR_CHGT_MOTEUR)
            .compteurCarrosserie(UPDATED_COMPTEUR_CARROSSERIE)
            .compteurHelisse(UPDATED_COMPTEUR_HELISSE)
            .date(UPDATED_DATE);
        return atelier;
    }

    @BeforeEach
    public void initTest() {
        atelier = createEntity(em);
    }

    @Test
    @Transactional
    void createAtelier() throws Exception {
        int databaseSizeBeforeCreate = atelierRepository.findAll().size();
        // Create the Atelier
        restAtelierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(atelier)))
            .andExpect(status().isCreated());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeCreate + 1);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getCompteurChgtMoteur()).isEqualTo(DEFAULT_COMPTEUR_CHGT_MOTEUR);
        assertThat(testAtelier.getCompteurCarrosserie()).isEqualTo(DEFAULT_COMPTEUR_CARROSSERIE);
        assertThat(testAtelier.getCompteurHelisse()).isEqualTo(DEFAULT_COMPTEUR_HELISSE);
        assertThat(testAtelier.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createAtelierWithExistingId() throws Exception {
        // Create the Atelier with an existing ID
        atelier.setId(1L);

        int databaseSizeBeforeCreate = atelierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAtelierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(atelier)))
            .andExpect(status().isBadRequest());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAteliers() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        // Get all the atelierList
        restAtelierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atelier.getId().intValue())))
            .andExpect(jsonPath("$.[*].compteurChgtMoteur").value(hasItem(DEFAULT_COMPTEUR_CHGT_MOTEUR)))
            .andExpect(jsonPath("$.[*].compteurCarrosserie").value(hasItem(DEFAULT_COMPTEUR_CARROSSERIE)))
            .andExpect(jsonPath("$.[*].compteurHelisse").value(hasItem(DEFAULT_COMPTEUR_HELISSE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    void getAtelier() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        // Get the atelier
        restAtelierMockMvc
            .perform(get(ENTITY_API_URL_ID, atelier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(atelier.getId().intValue()))
            .andExpect(jsonPath("$.compteurChgtMoteur").value(DEFAULT_COMPTEUR_CHGT_MOTEUR))
            .andExpect(jsonPath("$.compteurCarrosserie").value(DEFAULT_COMPTEUR_CARROSSERIE))
            .andExpect(jsonPath("$.compteurHelisse").value(DEFAULT_COMPTEUR_HELISSE))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingAtelier() throws Exception {
        // Get the atelier
        restAtelierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAtelier() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();

        // Update the atelier
        Atelier updatedAtelier = atelierRepository.findById(atelier.getId()).get();
        // Disconnect from session so that the updates on updatedAtelier are not directly saved in db
        em.detach(updatedAtelier);
        updatedAtelier
            .compteurChgtMoteur(UPDATED_COMPTEUR_CHGT_MOTEUR)
            .compteurCarrosserie(UPDATED_COMPTEUR_CARROSSERIE)
            .compteurHelisse(UPDATED_COMPTEUR_HELISSE)
            .date(UPDATED_DATE);

        restAtelierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAtelier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAtelier))
            )
            .andExpect(status().isOk());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getCompteurChgtMoteur()).isEqualTo(UPDATED_COMPTEUR_CHGT_MOTEUR);
        assertThat(testAtelier.getCompteurCarrosserie()).isEqualTo(UPDATED_COMPTEUR_CARROSSERIE);
        assertThat(testAtelier.getCompteurHelisse()).isEqualTo(UPDATED_COMPTEUR_HELISSE);
        assertThat(testAtelier.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, atelier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(atelier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAtelierWithPatch() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();

        // Update the atelier using partial update
        Atelier partialUpdatedAtelier = new Atelier();
        partialUpdatedAtelier.setId(atelier.getId());

        partialUpdatedAtelier
            .compteurChgtMoteur(UPDATED_COMPTEUR_CHGT_MOTEUR)
            .compteurCarrosserie(UPDATED_COMPTEUR_CARROSSERIE)
            .compteurHelisse(UPDATED_COMPTEUR_HELISSE)
            .date(UPDATED_DATE);

        restAtelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtelier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtelier))
            )
            .andExpect(status().isOk());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getCompteurChgtMoteur()).isEqualTo(UPDATED_COMPTEUR_CHGT_MOTEUR);
        assertThat(testAtelier.getCompteurCarrosserie()).isEqualTo(UPDATED_COMPTEUR_CARROSSERIE);
        assertThat(testAtelier.getCompteurHelisse()).isEqualTo(UPDATED_COMPTEUR_HELISSE);
        assertThat(testAtelier.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAtelierWithPatch() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();

        // Update the atelier using partial update
        Atelier partialUpdatedAtelier = new Atelier();
        partialUpdatedAtelier.setId(atelier.getId());

        partialUpdatedAtelier
            .compteurChgtMoteur(UPDATED_COMPTEUR_CHGT_MOTEUR)
            .compteurCarrosserie(UPDATED_COMPTEUR_CARROSSERIE)
            .compteurHelisse(UPDATED_COMPTEUR_HELISSE)
            .date(UPDATED_DATE);

        restAtelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtelier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAtelier))
            )
            .andExpect(status().isOk());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
        Atelier testAtelier = atelierList.get(atelierList.size() - 1);
        assertThat(testAtelier.getCompteurChgtMoteur()).isEqualTo(UPDATED_COMPTEUR_CHGT_MOTEUR);
        assertThat(testAtelier.getCompteurCarrosserie()).isEqualTo(UPDATED_COMPTEUR_CARROSSERIE);
        assertThat(testAtelier.getCompteurHelisse()).isEqualTo(UPDATED_COMPTEUR_HELISSE);
        assertThat(testAtelier.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, atelier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(atelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAtelier() throws Exception {
        int databaseSizeBeforeUpdate = atelierRepository.findAll().size();
        atelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtelierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(atelier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Atelier in the database
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAtelier() throws Exception {
        // Initialize the database
        atelierRepository.saveAndFlush(atelier);

        int databaseSizeBeforeDelete = atelierRepository.findAll().size();

        // Delete the atelier
        restAtelierMockMvc
            .perform(delete(ENTITY_API_URL_ID, atelier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Atelier> atelierList = atelierRepository.findAll();
        assertThat(atelierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
