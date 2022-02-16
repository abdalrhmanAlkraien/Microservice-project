package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.Story;
import asc.foods.order.domain.enumeration.MultiMedia;
import asc.foods.order.repository.StoryRepository;
import asc.foods.order.service.dto.StoryDTO;
import asc.foods.order.service.mapper.StoryMapper;
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
 * Integration tests for the {@link StoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StoryResourceIT {

    private static final String DEFAULT_SAVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_SAVED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_SAVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SAVED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STORY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_STORY_TEXT = "BBBBBBBBBB";

    private static final MultiMedia DEFAULT_MULTI_MEDIA = MultiMedia.PHOTO;
    private static final MultiMedia UPDATED_MULTI_MEDIA = MultiMedia.VIDEO;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private StoryMapper storyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStoryMockMvc;

    private Story story;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Story createEntity(EntityManager em) {
        Story story = new Story()
            .savedBy(DEFAULT_SAVED_BY)
            .savedDate(DEFAULT_SAVED_DATE)
            .endDate(DEFAULT_END_DATE)
            .storyText(DEFAULT_STORY_TEXT)
            .multiMedia(DEFAULT_MULTI_MEDIA)
            .url(DEFAULT_URL)
            .description(DEFAULT_DESCRIPTION);
        return story;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Story createUpdatedEntity(EntityManager em) {
        Story story = new Story()
            .savedBy(UPDATED_SAVED_BY)
            .savedDate(UPDATED_SAVED_DATE)
            .endDate(UPDATED_END_DATE)
            .storyText(UPDATED_STORY_TEXT)
            .multiMedia(UPDATED_MULTI_MEDIA)
            .url(UPDATED_URL)
            .description(UPDATED_DESCRIPTION);
        return story;
    }

    @BeforeEach
    public void initTest() {
        story = createEntity(em);
    }

    @Test
    @Transactional
    void createStory() throws Exception {
        int databaseSizeBeforeCreate = storyRepository.findAll().size();
        // Create the Story
        StoryDTO storyDTO = storyMapper.toDto(story);
        restStoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storyDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeCreate + 1);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getSavedBy()).isEqualTo(DEFAULT_SAVED_BY);
        assertThat(testStory.getSavedDate()).isEqualTo(DEFAULT_SAVED_DATE);
        assertThat(testStory.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testStory.getStoryText()).isEqualTo(DEFAULT_STORY_TEXT);
        assertThat(testStory.getMultiMedia()).isEqualTo(DEFAULT_MULTI_MEDIA);
        assertThat(testStory.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testStory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createStoryWithExistingId() throws Exception {
        // Create the Story with an existing ID
        story.setId(1L);
        StoryDTO storyDTO = storyMapper.toDto(story);

        int databaseSizeBeforeCreate = storyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStories() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        // Get all the storyList
        restStoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(story.getId().intValue())))
            .andExpect(jsonPath("$.[*].savedBy").value(hasItem(DEFAULT_SAVED_BY)))
            .andExpect(jsonPath("$.[*].savedDate").value(hasItem(DEFAULT_SAVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].storyText").value(hasItem(DEFAULT_STORY_TEXT)))
            .andExpect(jsonPath("$.[*].multiMedia").value(hasItem(DEFAULT_MULTI_MEDIA.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        // Get the story
        restStoryMockMvc
            .perform(get(ENTITY_API_URL_ID, story.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(story.getId().intValue()))
            .andExpect(jsonPath("$.savedBy").value(DEFAULT_SAVED_BY))
            .andExpect(jsonPath("$.savedDate").value(DEFAULT_SAVED_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.storyText").value(DEFAULT_STORY_TEXT))
            .andExpect(jsonPath("$.multiMedia").value(DEFAULT_MULTI_MEDIA.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingStory() throws Exception {
        // Get the story
        restStoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Update the story
        Story updatedStory = storyRepository.findById(story.getId()).get();
        // Disconnect from session so that the updates on updatedStory are not directly saved in db
        em.detach(updatedStory);
        updatedStory
            .savedBy(UPDATED_SAVED_BY)
            .savedDate(UPDATED_SAVED_DATE)
            .endDate(UPDATED_END_DATE)
            .storyText(UPDATED_STORY_TEXT)
            .multiMedia(UPDATED_MULTI_MEDIA)
            .url(UPDATED_URL)
            .description(UPDATED_DESCRIPTION);
        StoryDTO storyDTO = storyMapper.toDto(updatedStory);

        restStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getSavedBy()).isEqualTo(UPDATED_SAVED_BY);
        assertThat(testStory.getSavedDate()).isEqualTo(UPDATED_SAVED_DATE);
        assertThat(testStory.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testStory.getStoryText()).isEqualTo(UPDATED_STORY_TEXT);
        assertThat(testStory.getMultiMedia()).isEqualTo(UPDATED_MULTI_MEDIA);
        assertThat(testStory.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testStory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // Create the Story
        StoryDTO storyDTO = storyMapper.toDto(story);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // Create the Story
        StoryDTO storyDTO = storyMapper.toDto(story);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // Create the Story
        StoryDTO storyDTO = storyMapper.toDto(story);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStoryWithPatch() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Update the story using partial update
        Story partialUpdatedStory = new Story();
        partialUpdatedStory.setId(story.getId());

        partialUpdatedStory.savedBy(UPDATED_SAVED_BY).savedDate(UPDATED_SAVED_DATE);

        restStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStory))
            )
            .andExpect(status().isOk());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getSavedBy()).isEqualTo(UPDATED_SAVED_BY);
        assertThat(testStory.getSavedDate()).isEqualTo(UPDATED_SAVED_DATE);
        assertThat(testStory.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testStory.getStoryText()).isEqualTo(DEFAULT_STORY_TEXT);
        assertThat(testStory.getMultiMedia()).isEqualTo(DEFAULT_MULTI_MEDIA);
        assertThat(testStory.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testStory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateStoryWithPatch() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Update the story using partial update
        Story partialUpdatedStory = new Story();
        partialUpdatedStory.setId(story.getId());

        partialUpdatedStory
            .savedBy(UPDATED_SAVED_BY)
            .savedDate(UPDATED_SAVED_DATE)
            .endDate(UPDATED_END_DATE)
            .storyText(UPDATED_STORY_TEXT)
            .multiMedia(UPDATED_MULTI_MEDIA)
            .url(UPDATED_URL)
            .description(UPDATED_DESCRIPTION);

        restStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStory))
            )
            .andExpect(status().isOk());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getSavedBy()).isEqualTo(UPDATED_SAVED_BY);
        assertThat(testStory.getSavedDate()).isEqualTo(UPDATED_SAVED_DATE);
        assertThat(testStory.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testStory.getStoryText()).isEqualTo(UPDATED_STORY_TEXT);
        assertThat(testStory.getMultiMedia()).isEqualTo(UPDATED_MULTI_MEDIA);
        assertThat(testStory.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testStory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // Create the Story
        StoryDTO storyDTO = storyMapper.toDto(story);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storyDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // Create the Story
        StoryDTO storyDTO = storyMapper.toDto(story);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();
        story.setId(count.incrementAndGet());

        // Create the Story
        StoryDTO storyDTO = storyMapper.toDto(story);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        int databaseSizeBeforeDelete = storyRepository.findAll().size();

        // Delete the story
        restStoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, story.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
