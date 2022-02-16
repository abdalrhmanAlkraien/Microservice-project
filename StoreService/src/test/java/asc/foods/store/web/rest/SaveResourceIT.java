package asc.foods.store.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.store.IntegrationTest;
import asc.foods.store.domain.Save;
import asc.foods.store.repository.SaveRepository;
import asc.foods.store.service.dto.SaveDTO;
import asc.foods.store.service.mapper.SaveMapper;
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
 * Integration tests for the {@link SaveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SaveResourceIT {

    private static final String DEFAULT_SAVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_SAVED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_SAVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SAVED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/saves";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SaveRepository saveRepository;

    @Autowired
    private SaveMapper saveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaveMockMvc;

    private Save save;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Save createEntity(EntityManager em) {
        Save save = new Save().savedBy(DEFAULT_SAVED_BY).savedDate(DEFAULT_SAVED_DATE);
        return save;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Save createUpdatedEntity(EntityManager em) {
        Save save = new Save().savedBy(UPDATED_SAVED_BY).savedDate(UPDATED_SAVED_DATE);
        return save;
    }

    @BeforeEach
    public void initTest() {
        save = createEntity(em);
    }

    @Test
    @Transactional
    void createSave() throws Exception {
        int databaseSizeBeforeCreate = saveRepository.findAll().size();
        // Create the Save
        SaveDTO saveDTO = saveMapper.toDto(save);
        restSaveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(saveDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Save in the database
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeCreate + 1);
        Save testSave = saveList.get(saveList.size() - 1);
        assertThat(testSave.getSavedBy()).isEqualTo(DEFAULT_SAVED_BY);
        assertThat(testSave.getSavedDate()).isEqualTo(DEFAULT_SAVED_DATE);
    }

    @Test
    @Transactional
    void createSaveWithExistingId() throws Exception {
        // Create the Save with an existing ID
        save.setId(1L);
        SaveDTO saveDTO = saveMapper.toDto(save);

        int databaseSizeBeforeCreate = saveRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(saveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Save in the database
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSaves() throws Exception {
        // Initialize the database
        saveRepository.saveAndFlush(save);

        // Get all the saveList
        restSaveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(save.getId().intValue())))
            .andExpect(jsonPath("$.[*].savedBy").value(hasItem(DEFAULT_SAVED_BY)))
            .andExpect(jsonPath("$.[*].savedDate").value(hasItem(DEFAULT_SAVED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSave() throws Exception {
        // Initialize the database
        saveRepository.saveAndFlush(save);

        // Get the save
        restSaveMockMvc
            .perform(get(ENTITY_API_URL_ID, save.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(save.getId().intValue()))
            .andExpect(jsonPath("$.savedBy").value(DEFAULT_SAVED_BY))
            .andExpect(jsonPath("$.savedDate").value(DEFAULT_SAVED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSave() throws Exception {
        // Get the save
        restSaveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSave() throws Exception {
        // Initialize the database
        saveRepository.saveAndFlush(save);

        int databaseSizeBeforeUpdate = saveRepository.findAll().size();

        // Update the save
        Save updatedSave = saveRepository.findById(save.getId()).get();
        // Disconnect from session so that the updates on updatedSave are not directly saved in db
        em.detach(updatedSave);
        updatedSave.savedBy(UPDATED_SAVED_BY).savedDate(UPDATED_SAVED_DATE);
        SaveDTO saveDTO = saveMapper.toDto(updatedSave);

        restSaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saveDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(saveDTO))
            )
            .andExpect(status().isOk());

        // Validate the Save in the database
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeUpdate);
        Save testSave = saveList.get(saveList.size() - 1);
        assertThat(testSave.getSavedBy()).isEqualTo(UPDATED_SAVED_BY);
        assertThat(testSave.getSavedDate()).isEqualTo(UPDATED_SAVED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSave() throws Exception {
        int databaseSizeBeforeUpdate = saveRepository.findAll().size();
        save.setId(count.incrementAndGet());

        // Create the Save
        SaveDTO saveDTO = saveMapper.toDto(save);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saveDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(saveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Save in the database
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSave() throws Exception {
        int databaseSizeBeforeUpdate = saveRepository.findAll().size();
        save.setId(count.incrementAndGet());

        // Create the Save
        SaveDTO saveDTO = saveMapper.toDto(save);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(saveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Save in the database
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSave() throws Exception {
        int databaseSizeBeforeUpdate = saveRepository.findAll().size();
        save.setId(count.incrementAndGet());

        // Create the Save
        SaveDTO saveDTO = saveMapper.toDto(save);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaveMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(saveDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Save in the database
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaveWithPatch() throws Exception {
        // Initialize the database
        saveRepository.saveAndFlush(save);

        int databaseSizeBeforeUpdate = saveRepository.findAll().size();

        // Update the save using partial update
        Save partialUpdatedSave = new Save();
        partialUpdatedSave.setId(save.getId());

        partialUpdatedSave.savedBy(UPDATED_SAVED_BY);

        restSaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSave.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSave))
            )
            .andExpect(status().isOk());

        // Validate the Save in the database
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeUpdate);
        Save testSave = saveList.get(saveList.size() - 1);
        assertThat(testSave.getSavedBy()).isEqualTo(UPDATED_SAVED_BY);
        assertThat(testSave.getSavedDate()).isEqualTo(DEFAULT_SAVED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSaveWithPatch() throws Exception {
        // Initialize the database
        saveRepository.saveAndFlush(save);

        int databaseSizeBeforeUpdate = saveRepository.findAll().size();

        // Update the save using partial update
        Save partialUpdatedSave = new Save();
        partialUpdatedSave.setId(save.getId());

        partialUpdatedSave.savedBy(UPDATED_SAVED_BY).savedDate(UPDATED_SAVED_DATE);

        restSaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSave.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSave))
            )
            .andExpect(status().isOk());

        // Validate the Save in the database
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeUpdate);
        Save testSave = saveList.get(saveList.size() - 1);
        assertThat(testSave.getSavedBy()).isEqualTo(UPDATED_SAVED_BY);
        assertThat(testSave.getSavedDate()).isEqualTo(UPDATED_SAVED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSave() throws Exception {
        int databaseSizeBeforeUpdate = saveRepository.findAll().size();
        save.setId(count.incrementAndGet());

        // Create the Save
        SaveDTO saveDTO = saveMapper.toDto(save);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, saveDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(saveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Save in the database
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSave() throws Exception {
        int databaseSizeBeforeUpdate = saveRepository.findAll().size();
        save.setId(count.incrementAndGet());

        // Create the Save
        SaveDTO saveDTO = saveMapper.toDto(save);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(saveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Save in the database
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSave() throws Exception {
        int databaseSizeBeforeUpdate = saveRepository.findAll().size();
        save.setId(count.incrementAndGet());

        // Create the Save
        SaveDTO saveDTO = saveMapper.toDto(save);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaveMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(saveDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Save in the database
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSave() throws Exception {
        // Initialize the database
        saveRepository.saveAndFlush(save);

        int databaseSizeBeforeDelete = saveRepository.findAll().size();

        // Delete the save
        restSaveMockMvc
            .perform(delete(ENTITY_API_URL_ID, save.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Save> saveList = saveRepository.findAll();
        assertThat(saveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
