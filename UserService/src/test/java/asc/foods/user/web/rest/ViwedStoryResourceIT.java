package asc.foods.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.user.IntegrationTest;
import asc.foods.user.domain.ViwedStory;
import asc.foods.user.repository.ViwedStoryRepository;
import asc.foods.user.service.dto.ViwedStoryDTO;
import asc.foods.user.service.mapper.ViwedStoryMapper;
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
 * Integration tests for the {@link ViwedStoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ViwedStoryResourceIT {

    private static final Instant DEFAULT_VIEWD_TINE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VIEWD_TINE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/viwed-stories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ViwedStoryRepository viwedStoryRepository;

    @Autowired
    private ViwedStoryMapper viwedStoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restViwedStoryMockMvc;

    private ViwedStory viwedStory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViwedStory createEntity(EntityManager em) {
        ViwedStory viwedStory = new ViwedStory().viewdTine(DEFAULT_VIEWD_TINE);
        return viwedStory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViwedStory createUpdatedEntity(EntityManager em) {
        ViwedStory viwedStory = new ViwedStory().viewdTine(UPDATED_VIEWD_TINE);
        return viwedStory;
    }

    @BeforeEach
    public void initTest() {
        viwedStory = createEntity(em);
    }

    @Test
    @Transactional
    void createViwedStory() throws Exception {
        int databaseSizeBeforeCreate = viwedStoryRepository.findAll().size();
        // Create the ViwedStory
        ViwedStoryDTO viwedStoryDTO = viwedStoryMapper.toDto(viwedStory);
        restViwedStoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viwedStoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ViwedStory in the database
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeCreate + 1);
        ViwedStory testViwedStory = viwedStoryList.get(viwedStoryList.size() - 1);
        assertThat(testViwedStory.getViewdTine()).isEqualTo(DEFAULT_VIEWD_TINE);
    }

    @Test
    @Transactional
    void createViwedStoryWithExistingId() throws Exception {
        // Create the ViwedStory with an existing ID
        viwedStory.setId(1L);
        ViwedStoryDTO viwedStoryDTO = viwedStoryMapper.toDto(viwedStory);

        int databaseSizeBeforeCreate = viwedStoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restViwedStoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viwedStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViwedStory in the database
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllViwedStories() throws Exception {
        // Initialize the database
        viwedStoryRepository.saveAndFlush(viwedStory);

        // Get all the viwedStoryList
        restViwedStoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viwedStory.getId().intValue())))
            .andExpect(jsonPath("$.[*].viewdTine").value(hasItem(DEFAULT_VIEWD_TINE.toString())));
    }

    @Test
    @Transactional
    void getViwedStory() throws Exception {
        // Initialize the database
        viwedStoryRepository.saveAndFlush(viwedStory);

        // Get the viwedStory
        restViwedStoryMockMvc
            .perform(get(ENTITY_API_URL_ID, viwedStory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(viwedStory.getId().intValue()))
            .andExpect(jsonPath("$.viewdTine").value(DEFAULT_VIEWD_TINE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingViwedStory() throws Exception {
        // Get the viwedStory
        restViwedStoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewViwedStory() throws Exception {
        // Initialize the database
        viwedStoryRepository.saveAndFlush(viwedStory);

        int databaseSizeBeforeUpdate = viwedStoryRepository.findAll().size();

        // Update the viwedStory
        ViwedStory updatedViwedStory = viwedStoryRepository.findById(viwedStory.getId()).get();
        // Disconnect from session so that the updates on updatedViwedStory are not directly saved in db
        em.detach(updatedViwedStory);
        updatedViwedStory.viewdTine(UPDATED_VIEWD_TINE);
        ViwedStoryDTO viwedStoryDTO = viwedStoryMapper.toDto(updatedViwedStory);

        restViwedStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viwedStoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viwedStoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ViwedStory in the database
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeUpdate);
        ViwedStory testViwedStory = viwedStoryList.get(viwedStoryList.size() - 1);
        assertThat(testViwedStory.getViewdTine()).isEqualTo(UPDATED_VIEWD_TINE);
    }

    @Test
    @Transactional
    void putNonExistingViwedStory() throws Exception {
        int databaseSizeBeforeUpdate = viwedStoryRepository.findAll().size();
        viwedStory.setId(count.incrementAndGet());

        // Create the ViwedStory
        ViwedStoryDTO viwedStoryDTO = viwedStoryMapper.toDto(viwedStory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViwedStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viwedStoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viwedStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViwedStory in the database
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchViwedStory() throws Exception {
        int databaseSizeBeforeUpdate = viwedStoryRepository.findAll().size();
        viwedStory.setId(count.incrementAndGet());

        // Create the ViwedStory
        ViwedStoryDTO viwedStoryDTO = viwedStoryMapper.toDto(viwedStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViwedStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viwedStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViwedStory in the database
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamViwedStory() throws Exception {
        int databaseSizeBeforeUpdate = viwedStoryRepository.findAll().size();
        viwedStory.setId(count.incrementAndGet());

        // Create the ViwedStory
        ViwedStoryDTO viwedStoryDTO = viwedStoryMapper.toDto(viwedStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViwedStoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viwedStoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViwedStory in the database
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateViwedStoryWithPatch() throws Exception {
        // Initialize the database
        viwedStoryRepository.saveAndFlush(viwedStory);

        int databaseSizeBeforeUpdate = viwedStoryRepository.findAll().size();

        // Update the viwedStory using partial update
        ViwedStory partialUpdatedViwedStory = new ViwedStory();
        partialUpdatedViwedStory.setId(viwedStory.getId());

        partialUpdatedViwedStory.viewdTine(UPDATED_VIEWD_TINE);

        restViwedStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViwedStory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedViwedStory))
            )
            .andExpect(status().isOk());

        // Validate the ViwedStory in the database
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeUpdate);
        ViwedStory testViwedStory = viwedStoryList.get(viwedStoryList.size() - 1);
        assertThat(testViwedStory.getViewdTine()).isEqualTo(UPDATED_VIEWD_TINE);
    }

    @Test
    @Transactional
    void fullUpdateViwedStoryWithPatch() throws Exception {
        // Initialize the database
        viwedStoryRepository.saveAndFlush(viwedStory);

        int databaseSizeBeforeUpdate = viwedStoryRepository.findAll().size();

        // Update the viwedStory using partial update
        ViwedStory partialUpdatedViwedStory = new ViwedStory();
        partialUpdatedViwedStory.setId(viwedStory.getId());

        partialUpdatedViwedStory.viewdTine(UPDATED_VIEWD_TINE);

        restViwedStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViwedStory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedViwedStory))
            )
            .andExpect(status().isOk());

        // Validate the ViwedStory in the database
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeUpdate);
        ViwedStory testViwedStory = viwedStoryList.get(viwedStoryList.size() - 1);
        assertThat(testViwedStory.getViewdTine()).isEqualTo(UPDATED_VIEWD_TINE);
    }

    @Test
    @Transactional
    void patchNonExistingViwedStory() throws Exception {
        int databaseSizeBeforeUpdate = viwedStoryRepository.findAll().size();
        viwedStory.setId(count.incrementAndGet());

        // Create the ViwedStory
        ViwedStoryDTO viwedStoryDTO = viwedStoryMapper.toDto(viwedStory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViwedStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, viwedStoryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viwedStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViwedStory in the database
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchViwedStory() throws Exception {
        int databaseSizeBeforeUpdate = viwedStoryRepository.findAll().size();
        viwedStory.setId(count.incrementAndGet());

        // Create the ViwedStory
        ViwedStoryDTO viwedStoryDTO = viwedStoryMapper.toDto(viwedStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViwedStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viwedStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViwedStory in the database
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamViwedStory() throws Exception {
        int databaseSizeBeforeUpdate = viwedStoryRepository.findAll().size();
        viwedStory.setId(count.incrementAndGet());

        // Create the ViwedStory
        ViwedStoryDTO viwedStoryDTO = viwedStoryMapper.toDto(viwedStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViwedStoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viwedStoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViwedStory in the database
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteViwedStory() throws Exception {
        // Initialize the database
        viwedStoryRepository.saveAndFlush(viwedStory);

        int databaseSizeBeforeDelete = viwedStoryRepository.findAll().size();

        // Delete the viwedStory
        restViwedStoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, viwedStory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ViwedStory> viwedStoryList = viwedStoryRepository.findAll();
        assertThat(viwedStoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
