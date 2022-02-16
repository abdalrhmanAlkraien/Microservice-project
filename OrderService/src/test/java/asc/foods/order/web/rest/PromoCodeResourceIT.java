package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.PromoCode;
import asc.foods.order.repository.PromoCodeRepository;
import asc.foods.order.service.dto.PromoCodeDTO;
import asc.foods.order.service.mapper.PromoCodeMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PromoCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PromoCodeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIMES = 1;
    private static final Integer UPDATED_TIMES = 2;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/promo-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Autowired
    private PromoCodeMapper promoCodeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromoCodeMockMvc;

    private PromoCode promoCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromoCode createEntity(EntityManager em) {
        PromoCode promoCode = new PromoCode()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .times(DEFAULT_TIMES)
            .creationDate(DEFAULT_CREATION_DATE)
            .expireDate(DEFAULT_EXPIRE_DATE)
            .createdBy(DEFAULT_CREATED_BY);
        return promoCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromoCode createUpdatedEntity(EntityManager em) {
        PromoCode promoCode = new PromoCode()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .times(UPDATED_TIMES)
            .creationDate(UPDATED_CREATION_DATE)
            .expireDate(UPDATED_EXPIRE_DATE)
            .createdBy(UPDATED_CREATED_BY);
        return promoCode;
    }

    @BeforeEach
    public void initTest() {
        promoCode = createEntity(em);
    }

    @Test
    @Transactional
    void createPromoCode() throws Exception {
        int databaseSizeBeforeCreate = promoCodeRepository.findAll().size();
        // Create the PromoCode
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);
        restPromoCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeCreate + 1);
        PromoCode testPromoCode = promoCodeList.get(promoCodeList.size() - 1);
        assertThat(testPromoCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPromoCode.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPromoCode.getTimes()).isEqualTo(DEFAULT_TIMES);
        assertThat(testPromoCode.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPromoCode.getExpireDate()).isEqualTo(DEFAULT_EXPIRE_DATE);
        assertThat(testPromoCode.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void createPromoCodeWithExistingId() throws Exception {
        // Create the PromoCode with an existing ID
        promoCode.setId(1L);
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);

        int databaseSizeBeforeCreate = promoCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromoCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPromoCodes() throws Exception {
        // Initialize the database
        promoCodeRepository.saveAndFlush(promoCode);

        // Get all the promoCodeList
        restPromoCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promoCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].times").value(hasItem(DEFAULT_TIMES)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expireDate").value(hasItem(DEFAULT_EXPIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getPromoCode() throws Exception {
        // Initialize the database
        promoCodeRepository.saveAndFlush(promoCode);

        // Get the promoCode
        restPromoCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, promoCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promoCode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.times").value(DEFAULT_TIMES))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.expireDate").value(DEFAULT_EXPIRE_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingPromoCode() throws Exception {
        // Get the promoCode
        restPromoCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPromoCode() throws Exception {
        // Initialize the database
        promoCodeRepository.saveAndFlush(promoCode);

        int databaseSizeBeforeUpdate = promoCodeRepository.findAll().size();

        // Update the promoCode
        PromoCode updatedPromoCode = promoCodeRepository.findById(promoCode.getId()).get();
        // Disconnect from session so that the updates on updatedPromoCode are not directly saved in db
        em.detach(updatedPromoCode);
        updatedPromoCode
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .times(UPDATED_TIMES)
            .creationDate(UPDATED_CREATION_DATE)
            .expireDate(UPDATED_EXPIRE_DATE)
            .createdBy(UPDATED_CREATED_BY);
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(updatedPromoCode);

        restPromoCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promoCodeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeUpdate);
        PromoCode testPromoCode = promoCodeList.get(promoCodeList.size() - 1);
        assertThat(testPromoCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPromoCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPromoCode.getTimes()).isEqualTo(UPDATED_TIMES);
        assertThat(testPromoCode.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPromoCode.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
        assertThat(testPromoCode.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingPromoCode() throws Exception {
        int databaseSizeBeforeUpdate = promoCodeRepository.findAll().size();
        promoCode.setId(count.incrementAndGet());

        // Create the PromoCode
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromoCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promoCodeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPromoCode() throws Exception {
        int databaseSizeBeforeUpdate = promoCodeRepository.findAll().size();
        promoCode.setId(count.incrementAndGet());

        // Create the PromoCode
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPromoCode() throws Exception {
        int databaseSizeBeforeUpdate = promoCodeRepository.findAll().size();
        promoCode.setId(count.incrementAndGet());

        // Create the PromoCode
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoCodeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePromoCodeWithPatch() throws Exception {
        // Initialize the database
        promoCodeRepository.saveAndFlush(promoCode);

        int databaseSizeBeforeUpdate = promoCodeRepository.findAll().size();

        // Update the promoCode using partial update
        PromoCode partialUpdatedPromoCode = new PromoCode();
        partialUpdatedPromoCode.setId(promoCode.getId());

        partialUpdatedPromoCode.description(UPDATED_DESCRIPTION).times(UPDATED_TIMES).expireDate(UPDATED_EXPIRE_DATE);

        restPromoCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromoCode.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromoCode))
            )
            .andExpect(status().isOk());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeUpdate);
        PromoCode testPromoCode = promoCodeList.get(promoCodeList.size() - 1);
        assertThat(testPromoCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPromoCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPromoCode.getTimes()).isEqualTo(UPDATED_TIMES);
        assertThat(testPromoCode.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPromoCode.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
        assertThat(testPromoCode.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void fullUpdatePromoCodeWithPatch() throws Exception {
        // Initialize the database
        promoCodeRepository.saveAndFlush(promoCode);

        int databaseSizeBeforeUpdate = promoCodeRepository.findAll().size();

        // Update the promoCode using partial update
        PromoCode partialUpdatedPromoCode = new PromoCode();
        partialUpdatedPromoCode.setId(promoCode.getId());

        partialUpdatedPromoCode
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .times(UPDATED_TIMES)
            .creationDate(UPDATED_CREATION_DATE)
            .expireDate(UPDATED_EXPIRE_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restPromoCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromoCode.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromoCode))
            )
            .andExpect(status().isOk());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeUpdate);
        PromoCode testPromoCode = promoCodeList.get(promoCodeList.size() - 1);
        assertThat(testPromoCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPromoCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPromoCode.getTimes()).isEqualTo(UPDATED_TIMES);
        assertThat(testPromoCode.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPromoCode.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
        assertThat(testPromoCode.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingPromoCode() throws Exception {
        int databaseSizeBeforeUpdate = promoCodeRepository.findAll().size();
        promoCode.setId(count.incrementAndGet());

        // Create the PromoCode
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromoCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, promoCodeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPromoCode() throws Exception {
        int databaseSizeBeforeUpdate = promoCodeRepository.findAll().size();
        promoCode.setId(count.incrementAndGet());

        // Create the PromoCode
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPromoCode() throws Exception {
        int databaseSizeBeforeUpdate = promoCodeRepository.findAll().size();
        promoCode.setId(count.incrementAndGet());

        // Create the PromoCode
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromoCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePromoCode() throws Exception {
        // Initialize the database
        promoCodeRepository.saveAndFlush(promoCode);

        int databaseSizeBeforeDelete = promoCodeRepository.findAll().size();

        // Delete the promoCode
        restPromoCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, promoCode.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
