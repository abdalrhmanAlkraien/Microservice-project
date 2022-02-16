package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.LikePost;
import asc.foods.order.domain.enumeration.LikeReactive;
import asc.foods.order.repository.LikePostRepository;
import asc.foods.order.service.dto.LikePostDTO;
import asc.foods.order.service.mapper.LikePostMapper;
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
 * Integration tests for the {@link LikePostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikePostResourceIT {

    private static final Instant DEFAULT_LIKE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LIKE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LikeReactive DEFAULT_LIKE_REACTIVE = LikeReactive.LIKE;
    private static final LikeReactive UPDATED_LIKE_REACTIVE = LikeReactive.DISLIKE;

    private static final String ENTITY_API_URL = "/api/like-posts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private LikePostMapper likePostMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikePostMockMvc;

    private LikePost likePost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikePost createEntity(EntityManager em) {
        LikePost likePost = new LikePost().likeDate(DEFAULT_LIKE_DATE).createdBy(DEFAULT_CREATED_BY).likeReactive(DEFAULT_LIKE_REACTIVE);
        return likePost;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikePost createUpdatedEntity(EntityManager em) {
        LikePost likePost = new LikePost().likeDate(UPDATED_LIKE_DATE).createdBy(UPDATED_CREATED_BY).likeReactive(UPDATED_LIKE_REACTIVE);
        return likePost;
    }

    @BeforeEach
    public void initTest() {
        likePost = createEntity(em);
    }

    @Test
    @Transactional
    void createLikePost() throws Exception {
        int databaseSizeBeforeCreate = likePostRepository.findAll().size();
        // Create the LikePost
        LikePostDTO likePostDTO = likePostMapper.toDto(likePost);
        restLikePostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likePostDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeCreate + 1);
        LikePost testLikePost = likePostList.get(likePostList.size() - 1);
        assertThat(testLikePost.getLikeDate()).isEqualTo(DEFAULT_LIKE_DATE);
        assertThat(testLikePost.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLikePost.getLikeReactive()).isEqualTo(DEFAULT_LIKE_REACTIVE);
    }

    @Test
    @Transactional
    void createLikePostWithExistingId() throws Exception {
        // Create the LikePost with an existing ID
        likePost.setId(1L);
        LikePostDTO likePostDTO = likePostMapper.toDto(likePost);

        int databaseSizeBeforeCreate = likePostRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikePostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likePostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLikePosts() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        // Get all the likePostList
        restLikePostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likePost.getId().intValue())))
            .andExpect(jsonPath("$.[*].likeDate").value(hasItem(DEFAULT_LIKE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].likeReactive").value(hasItem(DEFAULT_LIKE_REACTIVE.toString())));
    }

    @Test
    @Transactional
    void getLikePost() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        // Get the likePost
        restLikePostMockMvc
            .perform(get(ENTITY_API_URL_ID, likePost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likePost.getId().intValue()))
            .andExpect(jsonPath("$.likeDate").value(DEFAULT_LIKE_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.likeReactive").value(DEFAULT_LIKE_REACTIVE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLikePost() throws Exception {
        // Get the likePost
        restLikePostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLikePost() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();

        // Update the likePost
        LikePost updatedLikePost = likePostRepository.findById(likePost.getId()).get();
        // Disconnect from session so that the updates on updatedLikePost are not directly saved in db
        em.detach(updatedLikePost);
        updatedLikePost.likeDate(UPDATED_LIKE_DATE).createdBy(UPDATED_CREATED_BY).likeReactive(UPDATED_LIKE_REACTIVE);
        LikePostDTO likePostDTO = likePostMapper.toDto(updatedLikePost);

        restLikePostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likePostDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likePostDTO))
            )
            .andExpect(status().isOk());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
        LikePost testLikePost = likePostList.get(likePostList.size() - 1);
        assertThat(testLikePost.getLikeDate()).isEqualTo(UPDATED_LIKE_DATE);
        assertThat(testLikePost.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLikePost.getLikeReactive()).isEqualTo(UPDATED_LIKE_REACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // Create the LikePost
        LikePostDTO likePostDTO = likePostMapper.toDto(likePost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likePostDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likePostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // Create the LikePost
        LikePostDTO likePostDTO = likePostMapper.toDto(likePost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likePostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // Create the LikePost
        LikePostDTO likePostDTO = likePostMapper.toDto(likePost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likePostDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikePostWithPatch() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();

        // Update the likePost using partial update
        LikePost partialUpdatedLikePost = new LikePost();
        partialUpdatedLikePost.setId(likePost.getId());

        partialUpdatedLikePost.createdBy(UPDATED_CREATED_BY);

        restLikePostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikePost.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikePost))
            )
            .andExpect(status().isOk());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
        LikePost testLikePost = likePostList.get(likePostList.size() - 1);
        assertThat(testLikePost.getLikeDate()).isEqualTo(DEFAULT_LIKE_DATE);
        assertThat(testLikePost.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLikePost.getLikeReactive()).isEqualTo(DEFAULT_LIKE_REACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateLikePostWithPatch() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();

        // Update the likePost using partial update
        LikePost partialUpdatedLikePost = new LikePost();
        partialUpdatedLikePost.setId(likePost.getId());

        partialUpdatedLikePost.likeDate(UPDATED_LIKE_DATE).createdBy(UPDATED_CREATED_BY).likeReactive(UPDATED_LIKE_REACTIVE);

        restLikePostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikePost.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikePost))
            )
            .andExpect(status().isOk());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
        LikePost testLikePost = likePostList.get(likePostList.size() - 1);
        assertThat(testLikePost.getLikeDate()).isEqualTo(UPDATED_LIKE_DATE);
        assertThat(testLikePost.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLikePost.getLikeReactive()).isEqualTo(UPDATED_LIKE_REACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // Create the LikePost
        LikePostDTO likePostDTO = likePostMapper.toDto(likePost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likePostDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likePostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // Create the LikePost
        LikePostDTO likePostDTO = likePostMapper.toDto(likePost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likePostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // Create the LikePost
        LikePostDTO likePostDTO = likePostMapper.toDto(likePost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likePostDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikePost() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        int databaseSizeBeforeDelete = likePostRepository.findAll().size();

        // Delete the likePost
        restLikePostMockMvc
            .perform(delete(ENTITY_API_URL_ID, likePost.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
