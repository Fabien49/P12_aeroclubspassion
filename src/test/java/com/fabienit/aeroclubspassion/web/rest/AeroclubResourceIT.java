package com.fabienit.aeroclubspassion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fabienit.aeroclubspassion.IntegrationTest;
import com.fabienit.aeroclubspassion.domain.Aeroclub;
import com.fabienit.aeroclubspassion.repository.AeroclubRepository;
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
 * Integration tests for the {@link AeroclubResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AeroclubResourceIT {

    private static final String DEFAULT_OACI = "AAAAAAAAAA";
    private static final String UPDATED_OACI = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL = "BBBBBBBBBB";

    private static final String DEFAULT_COMMUNE = "AAAAAAAAAA";
    private static final String UPDATED_COMMUNE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aeroclubs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AeroclubRepository aeroclubRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAeroclubMockMvc;

    private Aeroclub aeroclub;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aeroclub createEntity(EntityManager em) {
        Aeroclub aeroclub = new Aeroclub()
            .oaci(DEFAULT_OACI)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .mail(DEFAULT_MAIL)
            .adresse(DEFAULT_ADRESSE)
            .codePostal(DEFAULT_CODE_POSTAL)
            .commune(DEFAULT_COMMUNE);
        return aeroclub;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aeroclub createUpdatedEntity(EntityManager em) {
        Aeroclub aeroclub = new Aeroclub()
            .oaci(UPDATED_OACI)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .mail(UPDATED_MAIL)
            .adresse(UPDATED_ADRESSE)
            .codePostal(UPDATED_CODE_POSTAL)
            .commune(UPDATED_COMMUNE);
        return aeroclub;
    }

    @BeforeEach
    public void initTest() {
        aeroclub = createEntity(em);
    }

    @Test
    @Transactional
    void createAeroclub() throws Exception {
        int databaseSizeBeforeCreate = aeroclubRepository.findAll().size();
        // Create the Aeroclub
        restAeroclubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isCreated());

        // Validate the Aeroclub in the database
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeCreate + 1);
        Aeroclub testAeroclub = aeroclubList.get(aeroclubList.size() - 1);
        assertThat(testAeroclub.getOaci()).isEqualTo(DEFAULT_OACI);
        assertThat(testAeroclub.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAeroclub.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAeroclub.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testAeroclub.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testAeroclub.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAeroclub.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testAeroclub.getCommune()).isEqualTo(DEFAULT_COMMUNE);
    }

    @Test
    @Transactional
    void createAeroclubWithExistingId() throws Exception {
        // Create the Aeroclub with an existing ID
        aeroclub.setId(1L);

        int databaseSizeBeforeCreate = aeroclubRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAeroclubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isBadRequest());

        // Validate the Aeroclub in the database
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOaciIsRequired() throws Exception {
        int databaseSizeBeforeTest = aeroclubRepository.findAll().size();
        // set the field null
        aeroclub.setOaci(null);

        // Create the Aeroclub, which fails.

        restAeroclubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isBadRequest());

        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = aeroclubRepository.findAll().size();
        // set the field null
        aeroclub.setName(null);

        // Create the Aeroclub, which fails.

        restAeroclubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isBadRequest());

        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = aeroclubRepository.findAll().size();
        // set the field null
        aeroclub.setType(null);

        // Create the Aeroclub, which fails.

        restAeroclubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isBadRequest());

        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = aeroclubRepository.findAll().size();
        // set the field null
        aeroclub.setPhoneNumber(null);

        // Create the Aeroclub, which fails.

        restAeroclubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isBadRequest());

        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMailIsRequired() throws Exception {
        int databaseSizeBeforeTest = aeroclubRepository.findAll().size();
        // set the field null
        aeroclub.setMail(null);

        // Create the Aeroclub, which fails.

        restAeroclubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isBadRequest());

        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = aeroclubRepository.findAll().size();
        // set the field null
        aeroclub.setAdresse(null);

        // Create the Aeroclub, which fails.

        restAeroclubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isBadRequest());

        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodePostalIsRequired() throws Exception {
        int databaseSizeBeforeTest = aeroclubRepository.findAll().size();
        // set the field null
        aeroclub.setCodePostal(null);

        // Create the Aeroclub, which fails.

        restAeroclubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isBadRequest());

        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommuneIsRequired() throws Exception {
        int databaseSizeBeforeTest = aeroclubRepository.findAll().size();
        // set the field null
        aeroclub.setCommune(null);

        // Create the Aeroclub, which fails.

        restAeroclubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isBadRequest());

        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAeroclubs() throws Exception {
        // Initialize the database
        aeroclubRepository.saveAndFlush(aeroclub);

        // Get all the aeroclubList
        restAeroclubMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aeroclub.getId().intValue())))
            .andExpect(jsonPath("$.[*].oaci").value(hasItem(DEFAULT_OACI)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].commune").value(hasItem(DEFAULT_COMMUNE)));
    }

    @Test
    @Transactional
    void getAeroclub() throws Exception {
        // Initialize the database
        aeroclubRepository.saveAndFlush(aeroclub);

        // Get the aeroclub
        restAeroclubMockMvc
            .perform(get(ENTITY_API_URL_ID, aeroclub.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aeroclub.getId().intValue()))
            .andExpect(jsonPath("$.oaci").value(DEFAULT_OACI))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.commune").value(DEFAULT_COMMUNE));
    }

    @Test
    @Transactional
    void getNonExistingAeroclub() throws Exception {
        // Get the aeroclub
        restAeroclubMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAeroclub() throws Exception {
        // Initialize the database
        aeroclubRepository.saveAndFlush(aeroclub);

        int databaseSizeBeforeUpdate = aeroclubRepository.findAll().size();

        // Update the aeroclub
        Aeroclub updatedAeroclub = aeroclubRepository.findById(aeroclub.getId()).get();
        // Disconnect from session so that the updates on updatedAeroclub are not directly saved in db
        em.detach(updatedAeroclub);
        updatedAeroclub
            .oaci(UPDATED_OACI)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .mail(UPDATED_MAIL)
            .adresse(UPDATED_ADRESSE)
            .codePostal(UPDATED_CODE_POSTAL)
            .commune(UPDATED_COMMUNE);

        restAeroclubMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAeroclub.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAeroclub))
            )
            .andExpect(status().isOk());

        // Validate the Aeroclub in the database
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeUpdate);
        Aeroclub testAeroclub = aeroclubList.get(aeroclubList.size() - 1);
        assertThat(testAeroclub.getOaci()).isEqualTo(UPDATED_OACI);
        assertThat(testAeroclub.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAeroclub.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAeroclub.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAeroclub.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testAeroclub.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAeroclub.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testAeroclub.getCommune()).isEqualTo(UPDATED_COMMUNE);
    }

    @Test
    @Transactional
    void putNonExistingAeroclub() throws Exception {
        int databaseSizeBeforeUpdate = aeroclubRepository.findAll().size();
        aeroclub.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAeroclubMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aeroclub.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aeroclub))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aeroclub in the database
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAeroclub() throws Exception {
        int databaseSizeBeforeUpdate = aeroclubRepository.findAll().size();
        aeroclub.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAeroclubMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aeroclub))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aeroclub in the database
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAeroclub() throws Exception {
        int databaseSizeBeforeUpdate = aeroclubRepository.findAll().size();
        aeroclub.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAeroclubMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aeroclub in the database
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAeroclubWithPatch() throws Exception {
        // Initialize the database
        aeroclubRepository.saveAndFlush(aeroclub);

        int databaseSizeBeforeUpdate = aeroclubRepository.findAll().size();

        // Update the aeroclub using partial update
        Aeroclub partialUpdatedAeroclub = new Aeroclub();
        partialUpdatedAeroclub.setId(aeroclub.getId());

        partialUpdatedAeroclub.name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).adresse(UPDATED_ADRESSE);

        restAeroclubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAeroclub.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAeroclub))
            )
            .andExpect(status().isOk());

        // Validate the Aeroclub in the database
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeUpdate);
        Aeroclub testAeroclub = aeroclubList.get(aeroclubList.size() - 1);
        assertThat(testAeroclub.getOaci()).isEqualTo(DEFAULT_OACI);
        assertThat(testAeroclub.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAeroclub.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAeroclub.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAeroclub.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testAeroclub.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAeroclub.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testAeroclub.getCommune()).isEqualTo(DEFAULT_COMMUNE);
    }

    @Test
    @Transactional
    void fullUpdateAeroclubWithPatch() throws Exception {
        // Initialize the database
        aeroclubRepository.saveAndFlush(aeroclub);

        int databaseSizeBeforeUpdate = aeroclubRepository.findAll().size();

        // Update the aeroclub using partial update
        Aeroclub partialUpdatedAeroclub = new Aeroclub();
        partialUpdatedAeroclub.setId(aeroclub.getId());

        partialUpdatedAeroclub
            .oaci(UPDATED_OACI)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .mail(UPDATED_MAIL)
            .adresse(UPDATED_ADRESSE)
            .codePostal(UPDATED_CODE_POSTAL)
            .commune(UPDATED_COMMUNE);

        restAeroclubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAeroclub.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAeroclub))
            )
            .andExpect(status().isOk());

        // Validate the Aeroclub in the database
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeUpdate);
        Aeroclub testAeroclub = aeroclubList.get(aeroclubList.size() - 1);
        assertThat(testAeroclub.getOaci()).isEqualTo(UPDATED_OACI);
        assertThat(testAeroclub.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAeroclub.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAeroclub.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAeroclub.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testAeroclub.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAeroclub.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testAeroclub.getCommune()).isEqualTo(UPDATED_COMMUNE);
    }

    @Test
    @Transactional
    void patchNonExistingAeroclub() throws Exception {
        int databaseSizeBeforeUpdate = aeroclubRepository.findAll().size();
        aeroclub.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAeroclubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aeroclub.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aeroclub))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aeroclub in the database
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAeroclub() throws Exception {
        int databaseSizeBeforeUpdate = aeroclubRepository.findAll().size();
        aeroclub.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAeroclubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aeroclub))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aeroclub in the database
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAeroclub() throws Exception {
        int databaseSizeBeforeUpdate = aeroclubRepository.findAll().size();
        aeroclub.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAeroclubMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aeroclub)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aeroclub in the database
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAeroclub() throws Exception {
        // Initialize the database
        aeroclubRepository.saveAndFlush(aeroclub);

        int databaseSizeBeforeDelete = aeroclubRepository.findAll().size();

        // Delete the aeroclub
        restAeroclubMockMvc
            .perform(delete(ENTITY_API_URL_ID, aeroclub.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Aeroclub> aeroclubList = aeroclubRepository.findAll();
        assertThat(aeroclubList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
