package asc.foods.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.user.IntegrationTest;
import asc.foods.user.domain.PostMultimedia;
import asc.foods.user.domain.enumeration.MultiMedia;
import asc.foods.user.repository.PostMultimediaRepository;
import asc.foods.user.service.dto.PostMultimediaDTO;
import asc.foods.user.service.mapper.PostMultimediaMapper;
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
 * Integration tests for the {@link PostMultimediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostMultimediaResourceIT {

    private static final String DEFAULT_CAPTION = "AAAAAAAAAA";
    private static final String UPDATED_CAPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final MultiMedia DEFAULT_MULTI_MEDIA = MultiMedia.PHOTO;
    private static final MultiMedia UPDATED_MULTI_MEDIA = MultiMedia.VIDEO;

    private static final String ENTITY_API_URL = "/api/post-multimedias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PostMultimediaRepository postMultimediaRepository;

    @Autowired
    private PostMultimediaMapper postMultimediaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostMultimediaMockMvc;

    private PostMultimedia postMultimedia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostMultimedia createEntity(EntityManager em) {
        PostMultimedia postMultimedia = new PostMultimedia().caption(DEFAULT_CAPTION).url(DEFAULT_URL).multiMedia(DEFAULT_MULTI_MEDIA);
        return postMultimedia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostMultimedia createUpdatedEntity(EntityManager em) {
        PostMultimedia postMultimedia = new PostMultimedia().caption(UPDATED_CAPTION).url(UPDATED_URL).multiMedia(UPDATED_MULTI_MEDIA);
        return postMultimedia;
    }

    @BeforeEach
    public void initTest() {
        postMultimedia = createEntity(em);
    }

    @Test
    @Transactional
    void createPostMultimedia() throws Exception {
        int databaseSizeBeforeCreate = postMultimediaRepository.findAll().size();
        // Create the PostMultimedia
        PostMultimediaDTO postMultimediaDTO = postMultimediaMapper.toDto(postMultimedia);
        restPostMultimediaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postMultimediaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PostMultimedia in the database
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeCreate + 1);
        PostMultimedia testPostMultimedia = postMultimediaList.get(postMultimediaList.size() - 1);
        assertThat(testPostMultimedia.getCaption()).isEqualTo(DEFAULT_CAPTION);
        assertThat(testPostMultimedia.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testPostMultimedia.getMultiMedia()).isEqualTo(DEFAULT_MULTI_MEDIA);
    }

    @Test
    @Transactional
    void createPostMultimediaWithExistingId() throws Exception {
        // Create the PostMultimedia with an existing ID
        postMultimedia.setId(1L);
        PostMultimediaDTO postMultimediaDTO = postMultimediaMapper.toDto(postMultimedia);

        int databaseSizeBeforeCreate = postMultimediaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostMultimediaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postMultimediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostMultimedia in the database
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPostMultimedias() throws Exception {
        // Initialize the database
        postMultimediaRepository.saveAndFlush(postMultimedia);

        // Get all the postMultimediaList
        restPostMultimediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postMultimedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].multiMedia").value(hasItem(DEFAULT_MULTI_MEDIA.toString())));
    }

    @Test
    @Transactional
    void getPostMultimedia() throws Exception {
        // Initialize the database
        postMultimediaRepository.saveAndFlush(postMultimedia);

        // Get the postMultimedia
        restPostMultimediaMockMvc
            .perform(get(ENTITY_API_URL_ID, postMultimedia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postMultimedia.getId().intValue()))
            .andExpect(jsonPath("$.caption").value(DEFAULT_CAPTION))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.multiMedia").value(DEFAULT_MULTI_MEDIA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPostMultimedia() throws Exception {
        // Get the postMultimedia
        restPostMultimediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPostMultimedia() throws Exception {
        // Initialize the database
        postMultimediaRepository.saveAndFlush(postMultimedia);

        int databaseSizeBeforeUpdate = postMultimediaRepository.findAll().size();

        // Update the postMultimedia
        PostMultimedia updatedPostMultimedia = postMultimediaRepository.findById(postMultimedia.getId()).get();
        // Disconnect from session so that the updates on updatedPostMultimedia are not directly saved in db
        em.detach(updatedPostMultimedia);
        updatedPostMultimedia.caption(UPDATED_CAPTION).url(UPDATED_URL).multiMedia(UPDATED_MULTI_MEDIA);
        PostMultimediaDTO postMultimediaDTO = postMultimediaMapper.toDto(updatedPostMultimedia);

        restPostMultimediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postMultimediaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postMultimediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the PostMultimedia in the database
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeUpdate);
        PostMultimedia testPostMultimedia = postMultimediaList.get(postMultimediaList.size() - 1);
        assertThat(testPostMultimedia.getCaption()).isEqualTo(UPDATED_CAPTION);
        assertThat(testPostMultimedia.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPostMultimedia.getMultiMedia()).isEqualTo(UPDATED_MULTI_MEDIA);
    }

    @Test
    @Transactional
    void putNonExistingPostMultimedia() throws Exception {
        int databaseSizeBeforeUpdate = postMultimediaRepository.findAll().size();
        postMultimedia.setId(count.incrementAndGet());

        // Create the PostMultimedia
        PostMultimediaDTO postMultimediaDTO = postMultimediaMapper.toDto(postMultimedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostMultimediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postMultimediaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postMultimediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostMultimedia in the database
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostMultimedia() throws Exception {
        int databaseSizeBeforeUpdate = postMultimediaRepository.findAll().size();
        postMultimedia.setId(count.incrementAndGet());

        // Create the PostMultimedia
        PostMultimediaDTO postMultimediaDTO = postMultimediaMapper.toDto(postMultimedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMultimediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postMultimediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostMultimedia in the database
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostMultimedia() throws Exception {
        int databaseSizeBeforeUpdate = postMultimediaRepository.findAll().size();
        postMultimedia.setId(count.incrementAndGet());

        // Create the PostMultimedia
        PostMultimediaDTO postMultimediaDTO = postMultimediaMapper.toDto(postMultimedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMultimediaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postMultimediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostMultimedia in the database
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostMultimediaWithPatch() throws Exception {
        // Initialize the database
        postMultimediaRepository.saveAndFlush(postMultimedia);

        int databaseSizeBeforeUpdate = postMultimediaRepository.findAll().size();

        // Update the postMultimedia using partial update
        PostMultimedia partialUpdatedPostMultimedia = new PostMultimedia();
        partialUpdatedPostMultimedia.setId(postMultimedia.getId());

        partialUpdatedPostMultimedia.url(UPDATED_URL).multiMedia(UPDATED_MULTI_MEDIA);

        restPostMultimediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostMultimedia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostMultimedia))
            )
            .andExpect(status().isOk());

        // Validate the PostMultimedia in the database
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeUpdate);
        PostMultimedia testPostMultimedia = postMultimediaList.get(postMultimediaList.size() - 1);
        assertThat(testPostMultimedia.getCaption()).isEqualTo(DEFAULT_CAPTION);
        assertThat(testPostMultimedia.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPostMultimedia.getMultiMedia()).isEqualTo(UPDATED_MULTI_MEDIA);
    }

    @Test
    @Transactional
    void fullUpdatePostMultimediaWithPatch() throws Exception {
        // Initialize the database
        postMultimediaRepository.saveAndFlush(postMultimedia);

        int databaseSizeBeforeUpdate = postMultimediaRepository.findAll().size();

        // Update the postMultimedia using partial update
        PostMultimedia partialUpdatedPostMultimedia = new PostMultimedia();
        partialUpdatedPostMultimedia.setId(postMultimedia.getId());

        partialUpdatedPostMultimedia.caption(UPDATED_CAPTION).url(UPDATED_URL).multiMedia(UPDATED_MULTI_MEDIA);

        restPostMultimediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostMultimedia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostMultimedia))
            )
            .andExpect(status().isOk());

        // Validate the PostMultimedia in the database
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeUpdate);
        PostMultimedia testPostMultimedia = postMultimediaList.get(postMultimediaList.size() - 1);
        assertThat(testPostMultimedia.getCaption()).isEqualTo(UPDATED_CAPTION);
        assertThat(testPostMultimedia.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPostMultimedia.getMultiMedia()).isEqualTo(UPDATED_MULTI_MEDIA);
    }

    @Test
    @Transactional
    void patchNonExistingPostMultimedia() throws Exception {
        int databaseSizeBeforeUpdate = postMultimediaRepository.findAll().size();
        postMultimedia.setId(count.incrementAndGet());

        // Create the PostMultimedia
        PostMultimediaDTO postMultimediaDTO = postMultimediaMapper.toDto(postMultimedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostMultimediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postMultimediaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postMultimediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostMultimedia in the database
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostMultimedia() throws Exception {
        int databaseSizeBeforeUpdate = postMultimediaRepository.findAll().size();
        postMultimedia.setId(count.incrementAndGet());

        // Create the PostMultimedia
        PostMultimediaDTO postMultimediaDTO = postMultimediaMapper.toDto(postMultimedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMultimediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postMultimediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostMultimedia in the database
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostMultimedia() throws Exception {
        int databaseSizeBeforeUpdate = postMultimediaRepository.findAll().size();
        postMultimedia.setId(count.incrementAndGet());

        // Create the PostMultimedia
        PostMultimediaDTO postMultimediaDTO = postMultimediaMapper.toDto(postMultimedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMultimediaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postMultimediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostMultimedia in the database
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostMultimedia() throws Exception {
        // Initialize the database
        postMultimediaRepository.saveAndFlush(postMultimedia);

        int databaseSizeBeforeDelete = postMultimediaRepository.findAll().size();

        // Delete the postMultimedia
        restPostMultimediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, postMultimedia.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostMultimedia> postMultimediaList = postMultimediaRepository.findAll();
        assertThat(postMultimediaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
