package asc.foods.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.user.IntegrationTest;
import asc.foods.user.domain.StoreFollower;
import asc.foods.user.repository.StoreFollowerRepository;
import asc.foods.user.service.dto.StoreFollowerDTO;
import asc.foods.user.service.mapper.StoreFollowerMapper;
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
 * Integration tests for the {@link StoreFollowerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StoreFollowerResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/store-followers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StoreFollowerRepository storeFollowerRepository;

    @Autowired
    private StoreFollowerMapper storeFollowerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStoreFollowerMockMvc;

    private StoreFollower storeFollower;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoreFollower createEntity(EntityManager em) {
        StoreFollower storeFollower = new StoreFollower().creationDate(DEFAULT_CREATION_DATE);
        return storeFollower;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoreFollower createUpdatedEntity(EntityManager em) {
        StoreFollower storeFollower = new StoreFollower().creationDate(UPDATED_CREATION_DATE);
        return storeFollower;
    }

    @BeforeEach
    public void initTest() {
        storeFollower = createEntity(em);
    }

    @Test
    @Transactional
    void createStoreFollower() throws Exception {
        int databaseSizeBeforeCreate = storeFollowerRepository.findAll().size();
        // Create the StoreFollower
        StoreFollowerDTO storeFollowerDTO = storeFollowerMapper.toDto(storeFollower);
        restStoreFollowerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeFollowerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StoreFollower in the database
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeCreate + 1);
        StoreFollower testStoreFollower = storeFollowerList.get(storeFollowerList.size() - 1);
        assertThat(testStoreFollower.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createStoreFollowerWithExistingId() throws Exception {
        // Create the StoreFollower with an existing ID
        storeFollower.setId(1L);
        StoreFollowerDTO storeFollowerDTO = storeFollowerMapper.toDto(storeFollower);

        int databaseSizeBeforeCreate = storeFollowerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreFollowerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeFollowerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreFollower in the database
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStoreFollowers() throws Exception {
        // Initialize the database
        storeFollowerRepository.saveAndFlush(storeFollower);

        // Get all the storeFollowerList
        restStoreFollowerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeFollower.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getStoreFollower() throws Exception {
        // Initialize the database
        storeFollowerRepository.saveAndFlush(storeFollower);

        // Get the storeFollower
        restStoreFollowerMockMvc
            .perform(get(ENTITY_API_URL_ID, storeFollower.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storeFollower.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStoreFollower() throws Exception {
        // Get the storeFollower
        restStoreFollowerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStoreFollower() throws Exception {
        // Initialize the database
        storeFollowerRepository.saveAndFlush(storeFollower);

        int databaseSizeBeforeUpdate = storeFollowerRepository.findAll().size();

        // Update the storeFollower
        StoreFollower updatedStoreFollower = storeFollowerRepository.findById(storeFollower.getId()).get();
        // Disconnect from session so that the updates on updatedStoreFollower are not directly saved in db
        em.detach(updatedStoreFollower);
        updatedStoreFollower.creationDate(UPDATED_CREATION_DATE);
        StoreFollowerDTO storeFollowerDTO = storeFollowerMapper.toDto(updatedStoreFollower);

        restStoreFollowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storeFollowerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeFollowerDTO))
            )
            .andExpect(status().isOk());

        // Validate the StoreFollower in the database
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeUpdate);
        StoreFollower testStoreFollower = storeFollowerList.get(storeFollowerList.size() - 1);
        assertThat(testStoreFollower.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStoreFollower() throws Exception {
        int databaseSizeBeforeUpdate = storeFollowerRepository.findAll().size();
        storeFollower.setId(count.incrementAndGet());

        // Create the StoreFollower
        StoreFollowerDTO storeFollowerDTO = storeFollowerMapper.toDto(storeFollower);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreFollowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storeFollowerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeFollowerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreFollower in the database
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStoreFollower() throws Exception {
        int databaseSizeBeforeUpdate = storeFollowerRepository.findAll().size();
        storeFollower.setId(count.incrementAndGet());

        // Create the StoreFollower
        StoreFollowerDTO storeFollowerDTO = storeFollowerMapper.toDto(storeFollower);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreFollowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeFollowerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreFollower in the database
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStoreFollower() throws Exception {
        int databaseSizeBeforeUpdate = storeFollowerRepository.findAll().size();
        storeFollower.setId(count.incrementAndGet());

        // Create the StoreFollower
        StoreFollowerDTO storeFollowerDTO = storeFollowerMapper.toDto(storeFollower);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreFollowerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeFollowerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StoreFollower in the database
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStoreFollowerWithPatch() throws Exception {
        // Initialize the database
        storeFollowerRepository.saveAndFlush(storeFollower);

        int databaseSizeBeforeUpdate = storeFollowerRepository.findAll().size();

        // Update the storeFollower using partial update
        StoreFollower partialUpdatedStoreFollower = new StoreFollower();
        partialUpdatedStoreFollower.setId(storeFollower.getId());

        restStoreFollowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStoreFollower.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStoreFollower))
            )
            .andExpect(status().isOk());

        // Validate the StoreFollower in the database
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeUpdate);
        StoreFollower testStoreFollower = storeFollowerList.get(storeFollowerList.size() - 1);
        assertThat(testStoreFollower.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStoreFollowerWithPatch() throws Exception {
        // Initialize the database
        storeFollowerRepository.saveAndFlush(storeFollower);

        int databaseSizeBeforeUpdate = storeFollowerRepository.findAll().size();

        // Update the storeFollower using partial update
        StoreFollower partialUpdatedStoreFollower = new StoreFollower();
        partialUpdatedStoreFollower.setId(storeFollower.getId());

        partialUpdatedStoreFollower.creationDate(UPDATED_CREATION_DATE);

        restStoreFollowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStoreFollower.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStoreFollower))
            )
            .andExpect(status().isOk());

        // Validate the StoreFollower in the database
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeUpdate);
        StoreFollower testStoreFollower = storeFollowerList.get(storeFollowerList.size() - 1);
        assertThat(testStoreFollower.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStoreFollower() throws Exception {
        int databaseSizeBeforeUpdate = storeFollowerRepository.findAll().size();
        storeFollower.setId(count.incrementAndGet());

        // Create the StoreFollower
        StoreFollowerDTO storeFollowerDTO = storeFollowerMapper.toDto(storeFollower);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreFollowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storeFollowerDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storeFollowerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreFollower in the database
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStoreFollower() throws Exception {
        int databaseSizeBeforeUpdate = storeFollowerRepository.findAll().size();
        storeFollower.setId(count.incrementAndGet());

        // Create the StoreFollower
        StoreFollowerDTO storeFollowerDTO = storeFollowerMapper.toDto(storeFollower);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreFollowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storeFollowerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreFollower in the database
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStoreFollower() throws Exception {
        int databaseSizeBeforeUpdate = storeFollowerRepository.findAll().size();
        storeFollower.setId(count.incrementAndGet());

        // Create the StoreFollower
        StoreFollowerDTO storeFollowerDTO = storeFollowerMapper.toDto(storeFollower);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreFollowerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storeFollowerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StoreFollower in the database
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStoreFollower() throws Exception {
        // Initialize the database
        storeFollowerRepository.saveAndFlush(storeFollower);

        int databaseSizeBeforeDelete = storeFollowerRepository.findAll().size();

        // Delete the storeFollower
        restStoreFollowerMockMvc
            .perform(delete(ENTITY_API_URL_ID, storeFollower.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StoreFollower> storeFollowerList = storeFollowerRepository.findAll();
        assertThat(storeFollowerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
