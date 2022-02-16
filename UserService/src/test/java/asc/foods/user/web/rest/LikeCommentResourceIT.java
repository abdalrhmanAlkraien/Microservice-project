package asc.foods.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.user.IntegrationTest;
import asc.foods.user.domain.LikeComment;
import asc.foods.user.domain.enumeration.LikeReactive;
import asc.foods.user.repository.LikeCommentRepository;
import asc.foods.user.service.dto.LikeCommentDTO;
import asc.foods.user.service.mapper.LikeCommentMapper;
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
 * Integration tests for the {@link LikeCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikeCommentResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LikeReactive DEFAULT_LIKE_REACTIVE = LikeReactive.LIKE;
    private static final LikeReactive UPDATED_LIKE_REACTIVE = LikeReactive.DISLIKE;

    private static final String ENTITY_API_URL = "/api/like-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LikeCommentRepository likeCommentRepository;

    @Autowired
    private LikeCommentMapper likeCommentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikeCommentMockMvc;

    private LikeComment likeComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeComment createEntity(EntityManager em) {
        LikeComment likeComment = new LikeComment()
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .likeReactive(DEFAULT_LIKE_REACTIVE);
        return likeComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeComment createUpdatedEntity(EntityManager em) {
        LikeComment likeComment = new LikeComment()
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .likeReactive(UPDATED_LIKE_REACTIVE);
        return likeComment;
    }

    @BeforeEach
    public void initTest() {
        likeComment = createEntity(em);
    }

    @Test
    @Transactional
    void createLikeComment() throws Exception {
        int databaseSizeBeforeCreate = likeCommentRepository.findAll().size();
        // Create the LikeComment
        LikeCommentDTO likeCommentDTO = likeCommentMapper.toDto(likeComment);
        restLikeCommentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LikeComment in the database
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeCreate + 1);
        LikeComment testLikeComment = likeCommentList.get(likeCommentList.size() - 1);
        assertThat(testLikeComment.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testLikeComment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLikeComment.getLikeReactive()).isEqualTo(DEFAULT_LIKE_REACTIVE);
    }

    @Test
    @Transactional
    void createLikeCommentWithExistingId() throws Exception {
        // Create the LikeComment with an existing ID
        likeComment.setId(1L);
        LikeCommentDTO likeCommentDTO = likeCommentMapper.toDto(likeComment);

        int databaseSizeBeforeCreate = likeCommentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikeCommentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeComment in the database
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLikeComments() throws Exception {
        // Initialize the database
        likeCommentRepository.saveAndFlush(likeComment);

        // Get all the likeCommentList
        restLikeCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likeComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].likeReactive").value(hasItem(DEFAULT_LIKE_REACTIVE.toString())));
    }

    @Test
    @Transactional
    void getLikeComment() throws Exception {
        // Initialize the database
        likeCommentRepository.saveAndFlush(likeComment);

        // Get the likeComment
        restLikeCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, likeComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likeComment.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.likeReactive").value(DEFAULT_LIKE_REACTIVE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLikeComment() throws Exception {
        // Get the likeComment
        restLikeCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLikeComment() throws Exception {
        // Initialize the database
        likeCommentRepository.saveAndFlush(likeComment);

        int databaseSizeBeforeUpdate = likeCommentRepository.findAll().size();

        // Update the likeComment
        LikeComment updatedLikeComment = likeCommentRepository.findById(likeComment.getId()).get();
        // Disconnect from session so that the updates on updatedLikeComment are not directly saved in db
        em.detach(updatedLikeComment);
        updatedLikeComment.creationDate(UPDATED_CREATION_DATE).createdBy(UPDATED_CREATED_BY).likeReactive(UPDATED_LIKE_REACTIVE);
        LikeCommentDTO likeCommentDTO = likeCommentMapper.toDto(updatedLikeComment);

        restLikeCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeCommentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentDTO))
            )
            .andExpect(status().isOk());

        // Validate the LikeComment in the database
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeUpdate);
        LikeComment testLikeComment = likeCommentList.get(likeCommentList.size() - 1);
        assertThat(testLikeComment.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testLikeComment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLikeComment.getLikeReactive()).isEqualTo(UPDATED_LIKE_REACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentRepository.findAll().size();
        likeComment.setId(count.incrementAndGet());

        // Create the LikeComment
        LikeCommentDTO likeCommentDTO = likeCommentMapper.toDto(likeComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeCommentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeComment in the database
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentRepository.findAll().size();
        likeComment.setId(count.incrementAndGet());

        // Create the LikeComment
        LikeCommentDTO likeCommentDTO = likeCommentMapper.toDto(likeComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeComment in the database
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentRepository.findAll().size();
        likeComment.setId(count.incrementAndGet());

        // Create the LikeComment
        LikeCommentDTO likeCommentDTO = likeCommentMapper.toDto(likeComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeCommentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeComment in the database
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikeCommentWithPatch() throws Exception {
        // Initialize the database
        likeCommentRepository.saveAndFlush(likeComment);

        int databaseSizeBeforeUpdate = likeCommentRepository.findAll().size();

        // Update the likeComment using partial update
        LikeComment partialUpdatedLikeComment = new LikeComment();
        partialUpdatedLikeComment.setId(likeComment.getId());

        restLikeCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeComment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeComment))
            )
            .andExpect(status().isOk());

        // Validate the LikeComment in the database
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeUpdate);
        LikeComment testLikeComment = likeCommentList.get(likeCommentList.size() - 1);
        assertThat(testLikeComment.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testLikeComment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLikeComment.getLikeReactive()).isEqualTo(DEFAULT_LIKE_REACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateLikeCommentWithPatch() throws Exception {
        // Initialize the database
        likeCommentRepository.saveAndFlush(likeComment);

        int databaseSizeBeforeUpdate = likeCommentRepository.findAll().size();

        // Update the likeComment using partial update
        LikeComment partialUpdatedLikeComment = new LikeComment();
        partialUpdatedLikeComment.setId(likeComment.getId());

        partialUpdatedLikeComment.creationDate(UPDATED_CREATION_DATE).createdBy(UPDATED_CREATED_BY).likeReactive(UPDATED_LIKE_REACTIVE);

        restLikeCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeComment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeComment))
            )
            .andExpect(status().isOk());

        // Validate the LikeComment in the database
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeUpdate);
        LikeComment testLikeComment = likeCommentList.get(likeCommentList.size() - 1);
        assertThat(testLikeComment.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testLikeComment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLikeComment.getLikeReactive()).isEqualTo(UPDATED_LIKE_REACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentRepository.findAll().size();
        likeComment.setId(count.incrementAndGet());

        // Create the LikeComment
        LikeCommentDTO likeCommentDTO = likeCommentMapper.toDto(likeComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likeCommentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeComment in the database
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentRepository.findAll().size();
        likeComment.setId(count.incrementAndGet());

        // Create the LikeComment
        LikeCommentDTO likeCommentDTO = likeCommentMapper.toDto(likeComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeComment in the database
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = likeCommentRepository.findAll().size();
        likeComment.setId(count.incrementAndGet());

        // Create the LikeComment
        LikeCommentDTO likeCommentDTO = likeCommentMapper.toDto(likeComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeCommentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeCommentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeComment in the database
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikeComment() throws Exception {
        // Initialize the database
        likeCommentRepository.saveAndFlush(likeComment);

        int databaseSizeBeforeDelete = likeCommentRepository.findAll().size();

        // Delete the likeComment
        restLikeCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, likeComment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LikeComment> likeCommentList = likeCommentRepository.findAll();
        assertThat(likeCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
