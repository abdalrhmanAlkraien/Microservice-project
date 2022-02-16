package asc.foods.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.user.IntegrationTest;
import asc.foods.user.domain.StoreType;
import asc.foods.user.repository.StoreTypeRepository;
import asc.foods.user.service.dto.StoreTypeDTO;
import asc.foods.user.service.mapper.StoreTypeMapper;
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
 * Integration tests for the {@link StoreTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StoreTypeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_CARD_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_BACKGROUND_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_BACKGROUND_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/store-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StoreTypeRepository storeTypeRepository;

    @Autowired
    private StoreTypeMapper storeTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStoreTypeMockMvc;

    private StoreType storeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoreType createEntity(EntityManager em) {
        StoreType storeType = new StoreType()
            .type(DEFAULT_TYPE)
            .imageUrl(DEFAULT_IMAGE_URL)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .cardColor(DEFAULT_CARD_COLOR)
            .backgroundImage(DEFAULT_BACKGROUND_IMAGE);
        return storeType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoreType createUpdatedEntity(EntityManager em) {
        StoreType storeType = new StoreType()
            .type(UPDATED_TYPE)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .cardColor(UPDATED_CARD_COLOR)
            .backgroundImage(UPDATED_BACKGROUND_IMAGE);
        return storeType;
    }

    @BeforeEach
    public void initTest() {
        storeType = createEntity(em);
    }

    @Test
    @Transactional
    void createStoreType() throws Exception {
        int databaseSizeBeforeCreate = storeTypeRepository.findAll().size();
        // Create the StoreType
        StoreTypeDTO storeTypeDTO = storeTypeMapper.toDto(storeType);
        restStoreTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StoreType in the database
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        StoreType testStoreType = storeTypeList.get(storeTypeList.size() - 1);
        assertThat(testStoreType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testStoreType.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testStoreType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStoreType.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testStoreType.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testStoreType.getCardColor()).isEqualTo(DEFAULT_CARD_COLOR);
        assertThat(testStoreType.getBackgroundImage()).isEqualTo(DEFAULT_BACKGROUND_IMAGE);
    }

    @Test
    @Transactional
    void createStoreTypeWithExistingId() throws Exception {
        // Create the StoreType with an existing ID
        storeType.setId(1L);
        StoreTypeDTO storeTypeDTO = storeTypeMapper.toDto(storeType);

        int databaseSizeBeforeCreate = storeTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreType in the database
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStoreTypes() throws Exception {
        // Initialize the database
        storeTypeRepository.saveAndFlush(storeType);

        // Get all the storeTypeList
        restStoreTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].cardColor").value(hasItem(DEFAULT_CARD_COLOR)))
            .andExpect(jsonPath("$.[*].backgroundImage").value(hasItem(DEFAULT_BACKGROUND_IMAGE)));
    }

    @Test
    @Transactional
    void getStoreType() throws Exception {
        // Initialize the database
        storeTypeRepository.saveAndFlush(storeType);

        // Get the storeType
        restStoreTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, storeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storeType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.cardColor").value(DEFAULT_CARD_COLOR))
            .andExpect(jsonPath("$.backgroundImage").value(DEFAULT_BACKGROUND_IMAGE));
    }

    @Test
    @Transactional
    void getNonExistingStoreType() throws Exception {
        // Get the storeType
        restStoreTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStoreType() throws Exception {
        // Initialize the database
        storeTypeRepository.saveAndFlush(storeType);

        int databaseSizeBeforeUpdate = storeTypeRepository.findAll().size();

        // Update the storeType
        StoreType updatedStoreType = storeTypeRepository.findById(storeType.getId()).get();
        // Disconnect from session so that the updates on updatedStoreType are not directly saved in db
        em.detach(updatedStoreType);
        updatedStoreType
            .type(UPDATED_TYPE)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .cardColor(UPDATED_CARD_COLOR)
            .backgroundImage(UPDATED_BACKGROUND_IMAGE);
        StoreTypeDTO storeTypeDTO = storeTypeMapper.toDto(updatedStoreType);

        restStoreTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storeTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the StoreType in the database
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeUpdate);
        StoreType testStoreType = storeTypeList.get(storeTypeList.size() - 1);
        assertThat(testStoreType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testStoreType.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testStoreType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStoreType.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testStoreType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStoreType.getCardColor()).isEqualTo(UPDATED_CARD_COLOR);
        assertThat(testStoreType.getBackgroundImage()).isEqualTo(UPDATED_BACKGROUND_IMAGE);
    }

    @Test
    @Transactional
    void putNonExistingStoreType() throws Exception {
        int databaseSizeBeforeUpdate = storeTypeRepository.findAll().size();
        storeType.setId(count.incrementAndGet());

        // Create the StoreType
        StoreTypeDTO storeTypeDTO = storeTypeMapper.toDto(storeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storeTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreType in the database
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStoreType() throws Exception {
        int databaseSizeBeforeUpdate = storeTypeRepository.findAll().size();
        storeType.setId(count.incrementAndGet());

        // Create the StoreType
        StoreTypeDTO storeTypeDTO = storeTypeMapper.toDto(storeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreType in the database
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStoreType() throws Exception {
        int databaseSizeBeforeUpdate = storeTypeRepository.findAll().size();
        storeType.setId(count.incrementAndGet());

        // Create the StoreType
        StoreTypeDTO storeTypeDTO = storeTypeMapper.toDto(storeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StoreType in the database
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStoreTypeWithPatch() throws Exception {
        // Initialize the database
        storeTypeRepository.saveAndFlush(storeType);

        int databaseSizeBeforeUpdate = storeTypeRepository.findAll().size();

        // Update the storeType using partial update
        StoreType partialUpdatedStoreType = new StoreType();
        partialUpdatedStoreType.setId(storeType.getId());

        partialUpdatedStoreType.imageUrl(UPDATED_IMAGE_URL).creationDate(UPDATED_CREATION_DATE);

        restStoreTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStoreType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStoreType))
            )
            .andExpect(status().isOk());

        // Validate the StoreType in the database
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeUpdate);
        StoreType testStoreType = storeTypeList.get(storeTypeList.size() - 1);
        assertThat(testStoreType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testStoreType.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testStoreType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStoreType.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testStoreType.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testStoreType.getCardColor()).isEqualTo(DEFAULT_CARD_COLOR);
        assertThat(testStoreType.getBackgroundImage()).isEqualTo(DEFAULT_BACKGROUND_IMAGE);
    }

    @Test
    @Transactional
    void fullUpdateStoreTypeWithPatch() throws Exception {
        // Initialize the database
        storeTypeRepository.saveAndFlush(storeType);

        int databaseSizeBeforeUpdate = storeTypeRepository.findAll().size();

        // Update the storeType using partial update
        StoreType partialUpdatedStoreType = new StoreType();
        partialUpdatedStoreType.setId(storeType.getId());

        partialUpdatedStoreType
            .type(UPDATED_TYPE)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .cardColor(UPDATED_CARD_COLOR)
            .backgroundImage(UPDATED_BACKGROUND_IMAGE);

        restStoreTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStoreType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStoreType))
            )
            .andExpect(status().isOk());

        // Validate the StoreType in the database
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeUpdate);
        StoreType testStoreType = storeTypeList.get(storeTypeList.size() - 1);
        assertThat(testStoreType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testStoreType.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testStoreType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStoreType.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testStoreType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStoreType.getCardColor()).isEqualTo(UPDATED_CARD_COLOR);
        assertThat(testStoreType.getBackgroundImage()).isEqualTo(UPDATED_BACKGROUND_IMAGE);
    }

    @Test
    @Transactional
    void patchNonExistingStoreType() throws Exception {
        int databaseSizeBeforeUpdate = storeTypeRepository.findAll().size();
        storeType.setId(count.incrementAndGet());

        // Create the StoreType
        StoreTypeDTO storeTypeDTO = storeTypeMapper.toDto(storeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storeTypeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreType in the database
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStoreType() throws Exception {
        int databaseSizeBeforeUpdate = storeTypeRepository.findAll().size();
        storeType.setId(count.incrementAndGet());

        // Create the StoreType
        StoreTypeDTO storeTypeDTO = storeTypeMapper.toDto(storeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreType in the database
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStoreType() throws Exception {
        int databaseSizeBeforeUpdate = storeTypeRepository.findAll().size();
        storeType.setId(count.incrementAndGet());

        // Create the StoreType
        StoreTypeDTO storeTypeDTO = storeTypeMapper.toDto(storeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storeTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StoreType in the database
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStoreType() throws Exception {
        // Initialize the database
        storeTypeRepository.saveAndFlush(storeType);

        int databaseSizeBeforeDelete = storeTypeRepository.findAll().size();

        // Delete the storeType
        restStoreTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, storeType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StoreType> storeTypeList = storeTypeRepository.findAll();
        assertThat(storeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
