package asc.foods.store.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.store.IntegrationTest;
import asc.foods.store.domain.FoodGenre;
import asc.foods.store.repository.FoodGenreRepository;
import asc.foods.store.service.dto.FoodGenreDTO;
import asc.foods.store.service.mapper.FoodGenreMapper;
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
 * Integration tests for the {@link FoodGenreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FoodGenreResourceIT {

    private static final String DEFAULT_GENER = "AAAAAAAAAA";
    private static final String UPDATED_GENER = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/food-genres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FoodGenreRepository foodGenreRepository;

    @Autowired
    private FoodGenreMapper foodGenreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFoodGenreMockMvc;

    private FoodGenre foodGenre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodGenre createEntity(EntityManager em) {
        FoodGenre foodGenre = new FoodGenre()
            .gener(DEFAULT_GENER)
            .imageUrl(DEFAULT_IMAGE_URL)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY);
        return foodGenre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodGenre createUpdatedEntity(EntityManager em) {
        FoodGenre foodGenre = new FoodGenre()
            .gener(UPDATED_GENER)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);
        return foodGenre;
    }

    @BeforeEach
    public void initTest() {
        foodGenre = createEntity(em);
    }

    @Test
    @Transactional
    void createFoodGenre() throws Exception {
        int databaseSizeBeforeCreate = foodGenreRepository.findAll().size();
        // Create the FoodGenre
        FoodGenreDTO foodGenreDTO = foodGenreMapper.toDto(foodGenre);
        restFoodGenreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodGenreDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FoodGenre in the database
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeCreate + 1);
        FoodGenre testFoodGenre = foodGenreList.get(foodGenreList.size() - 1);
        assertThat(testFoodGenre.getGener()).isEqualTo(DEFAULT_GENER);
        assertThat(testFoodGenre.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testFoodGenre.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFoodGenre.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFoodGenre.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void createFoodGenreWithExistingId() throws Exception {
        // Create the FoodGenre with an existing ID
        foodGenre.setId(1L);
        FoodGenreDTO foodGenreDTO = foodGenreMapper.toDto(foodGenre);

        int databaseSizeBeforeCreate = foodGenreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodGenreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodGenreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodGenre in the database
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFoodGenres() throws Exception {
        // Initialize the database
        foodGenreRepository.saveAndFlush(foodGenre);

        // Get all the foodGenreList
        restFoodGenreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodGenre.getId().intValue())))
            .andExpect(jsonPath("$.[*].gener").value(hasItem(DEFAULT_GENER)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getFoodGenre() throws Exception {
        // Initialize the database
        foodGenreRepository.saveAndFlush(foodGenre);

        // Get the foodGenre
        restFoodGenreMockMvc
            .perform(get(ENTITY_API_URL_ID, foodGenre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(foodGenre.getId().intValue()))
            .andExpect(jsonPath("$.gener").value(DEFAULT_GENER))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingFoodGenre() throws Exception {
        // Get the foodGenre
        restFoodGenreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFoodGenre() throws Exception {
        // Initialize the database
        foodGenreRepository.saveAndFlush(foodGenre);

        int databaseSizeBeforeUpdate = foodGenreRepository.findAll().size();

        // Update the foodGenre
        FoodGenre updatedFoodGenre = foodGenreRepository.findById(foodGenre.getId()).get();
        // Disconnect from session so that the updates on updatedFoodGenre are not directly saved in db
        em.detach(updatedFoodGenre);
        updatedFoodGenre
            .gener(UPDATED_GENER)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);
        FoodGenreDTO foodGenreDTO = foodGenreMapper.toDto(updatedFoodGenre);

        restFoodGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodGenreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodGenreDTO))
            )
            .andExpect(status().isOk());

        // Validate the FoodGenre in the database
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeUpdate);
        FoodGenre testFoodGenre = foodGenreList.get(foodGenreList.size() - 1);
        assertThat(testFoodGenre.getGener()).isEqualTo(UPDATED_GENER);
        assertThat(testFoodGenre.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testFoodGenre.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFoodGenre.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFoodGenre.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingFoodGenre() throws Exception {
        int databaseSizeBeforeUpdate = foodGenreRepository.findAll().size();
        foodGenre.setId(count.incrementAndGet());

        // Create the FoodGenre
        FoodGenreDTO foodGenreDTO = foodGenreMapper.toDto(foodGenre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodGenreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodGenreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodGenre in the database
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFoodGenre() throws Exception {
        int databaseSizeBeforeUpdate = foodGenreRepository.findAll().size();
        foodGenre.setId(count.incrementAndGet());

        // Create the FoodGenre
        FoodGenreDTO foodGenreDTO = foodGenreMapper.toDto(foodGenre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodGenreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodGenre in the database
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFoodGenre() throws Exception {
        int databaseSizeBeforeUpdate = foodGenreRepository.findAll().size();
        foodGenre.setId(count.incrementAndGet());

        // Create the FoodGenre
        FoodGenreDTO foodGenreDTO = foodGenreMapper.toDto(foodGenre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodGenreMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodGenreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodGenre in the database
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFoodGenreWithPatch() throws Exception {
        // Initialize the database
        foodGenreRepository.saveAndFlush(foodGenre);

        int databaseSizeBeforeUpdate = foodGenreRepository.findAll().size();

        // Update the foodGenre using partial update
        FoodGenre partialUpdatedFoodGenre = new FoodGenre();
        partialUpdatedFoodGenre.setId(foodGenre.getId());

        partialUpdatedFoodGenre.imageUrl(UPDATED_IMAGE_URL).description(UPDATED_DESCRIPTION);

        restFoodGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodGenre.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodGenre))
            )
            .andExpect(status().isOk());

        // Validate the FoodGenre in the database
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeUpdate);
        FoodGenre testFoodGenre = foodGenreList.get(foodGenreList.size() - 1);
        assertThat(testFoodGenre.getGener()).isEqualTo(DEFAULT_GENER);
        assertThat(testFoodGenre.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testFoodGenre.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFoodGenre.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testFoodGenre.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateFoodGenreWithPatch() throws Exception {
        // Initialize the database
        foodGenreRepository.saveAndFlush(foodGenre);

        int databaseSizeBeforeUpdate = foodGenreRepository.findAll().size();

        // Update the foodGenre using partial update
        FoodGenre partialUpdatedFoodGenre = new FoodGenre();
        partialUpdatedFoodGenre.setId(foodGenre.getId());

        partialUpdatedFoodGenre
            .gener(UPDATED_GENER)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restFoodGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodGenre.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodGenre))
            )
            .andExpect(status().isOk());

        // Validate the FoodGenre in the database
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeUpdate);
        FoodGenre testFoodGenre = foodGenreList.get(foodGenreList.size() - 1);
        assertThat(testFoodGenre.getGener()).isEqualTo(UPDATED_GENER);
        assertThat(testFoodGenre.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testFoodGenre.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFoodGenre.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testFoodGenre.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingFoodGenre() throws Exception {
        int databaseSizeBeforeUpdate = foodGenreRepository.findAll().size();
        foodGenre.setId(count.incrementAndGet());

        // Create the FoodGenre
        FoodGenreDTO foodGenreDTO = foodGenreMapper.toDto(foodGenre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, foodGenreDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodGenreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodGenre in the database
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFoodGenre() throws Exception {
        int databaseSizeBeforeUpdate = foodGenreRepository.findAll().size();
        foodGenre.setId(count.incrementAndGet());

        // Create the FoodGenre
        FoodGenreDTO foodGenreDTO = foodGenreMapper.toDto(foodGenre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodGenreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodGenre in the database
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFoodGenre() throws Exception {
        int databaseSizeBeforeUpdate = foodGenreRepository.findAll().size();
        foodGenre.setId(count.incrementAndGet());

        // Create the FoodGenre
        FoodGenreDTO foodGenreDTO = foodGenreMapper.toDto(foodGenre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodGenreMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodGenreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodGenre in the database
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFoodGenre() throws Exception {
        // Initialize the database
        foodGenreRepository.saveAndFlush(foodGenre);

        int databaseSizeBeforeDelete = foodGenreRepository.findAll().size();

        // Delete the foodGenre
        restFoodGenreMockMvc
            .perform(delete(ENTITY_API_URL_ID, foodGenre.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FoodGenre> foodGenreList = foodGenreRepository.findAll();
        assertThat(foodGenreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
