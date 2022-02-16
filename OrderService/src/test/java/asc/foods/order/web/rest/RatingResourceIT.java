package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.Rating;
import asc.foods.order.domain.enumeration.RatingType;
import asc.foods.order.repository.RatingRepository;
import asc.foods.order.service.dto.RatingDTO;
import asc.foods.order.service.mapper.RatingMapper;
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
 * Integration tests for the {@link RatingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RatingResourceIT {

    private static final Integer DEFAULT_RATE = 1;
    private static final Integer UPDATED_RATE = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final RatingType DEFAULT_TYPE = RatingType.ORDER;
    private static final RatingType UPDATED_TYPE = RatingType.DRIVER_TO_USER;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ratings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRatingMockMvc;

    private Rating rating;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rating createEntity(EntityManager em) {
        Rating rating = new Rating()
            .rate(DEFAULT_RATE)
            .comment(DEFAULT_COMMENT)
            .type(DEFAULT_TYPE)
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY);
        return rating;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rating createUpdatedEntity(EntityManager em) {
        Rating rating = new Rating()
            .rate(UPDATED_RATE)
            .comment(UPDATED_COMMENT)
            .type(UPDATED_TYPE)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);
        return rating;
    }

    @BeforeEach
    public void initTest() {
        rating = createEntity(em);
    }

    @Test
    @Transactional
    void createRating() throws Exception {
        int databaseSizeBeforeCreate = ratingRepository.findAll().size();
        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);
        restRatingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ratingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate + 1);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testRating.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testRating.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRating.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testRating.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void createRatingWithExistingId() throws Exception {
        // Create the Rating with an existing ID
        rating.setId(1L);
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        int databaseSizeBeforeCreate = ratingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRatings() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        // Get all the ratingList
        restRatingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rating.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getRating() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        // Get the rating
        restRatingMockMvc
            .perform(get(ENTITY_API_URL_ID, rating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rating.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingRating() throws Exception {
        // Get the rating
        restRatingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRating() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        // Disconnect from session so that the updates on updatedRating are not directly saved in db
        em.detach(updatedRating);
        updatedRating
            .rate(UPDATED_RATE)
            .comment(UPDATED_COMMENT)
            .type(UPDATED_TYPE)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);
        RatingDTO ratingDTO = ratingMapper.toDto(updatedRating);

        restRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ratingDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ratingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testRating.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRating.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRating.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testRating.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();
        rating.setId(count.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ratingDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();
        rating.setId(count.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();
        rating.setId(count.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ratingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRatingWithPatch() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating using partial update
        Rating partialUpdatedRating = new Rating();
        partialUpdatedRating.setId(rating.getId());

        partialUpdatedRating.rate(UPDATED_RATE).comment(UPDATED_COMMENT).createdBy(UPDATED_CREATED_BY);

        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRating.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRating))
            )
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testRating.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRating.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRating.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testRating.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateRatingWithPatch() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating using partial update
        Rating partialUpdatedRating = new Rating();
        partialUpdatedRating.setId(rating.getId());

        partialUpdatedRating
            .rate(UPDATED_RATE)
            .comment(UPDATED_COMMENT)
            .type(UPDATED_TYPE)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRating.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRating))
            )
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testRating.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRating.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRating.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testRating.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();
        rating.setId(count.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ratingDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();
        rating.setId(count.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();
        rating.setId(count.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ratingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRating() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        int databaseSizeBeforeDelete = ratingRepository.findAll().size();

        // Delete the rating
        restRatingMockMvc
            .perform(delete(ENTITY_API_URL_ID, rating.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
