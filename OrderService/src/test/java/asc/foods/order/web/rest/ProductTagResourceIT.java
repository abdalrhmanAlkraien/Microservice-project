package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.ProductTag;
import asc.foods.order.repository.ProductTagRepository;
import asc.foods.order.service.dto.ProductTagDTO;
import asc.foods.order.service.mapper.ProductTagMapper;
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
 * Integration tests for the {@link ProductTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductTagResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductTagRepository productTagRepository;

    @Autowired
    private ProductTagMapper productTagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductTagMockMvc;

    private ProductTag productTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductTag createEntity(EntityManager em) {
        ProductTag productTag = new ProductTag()
            .categoryName(DEFAULT_CATEGORY_NAME)
            .imageUrl(DEFAULT_IMAGE_URL)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY);
        return productTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductTag createUpdatedEntity(EntityManager em) {
        ProductTag productTag = new ProductTag()
            .categoryName(UPDATED_CATEGORY_NAME)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);
        return productTag;
    }

    @BeforeEach
    public void initTest() {
        productTag = createEntity(em);
    }

    @Test
    @Transactional
    void createProductTag() throws Exception {
        int databaseSizeBeforeCreate = productTagRepository.findAll().size();
        // Create the ProductTag
        ProductTagDTO productTagDTO = productTagMapper.toDto(productTag);
        restProductTagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductTag in the database
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeCreate + 1);
        ProductTag testProductTag = productTagList.get(productTagList.size() - 1);
        assertThat(testProductTag.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testProductTag.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testProductTag.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductTag.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProductTag.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void createProductTagWithExistingId() throws Exception {
        // Create the ProductTag with an existing ID
        productTag.setId(1L);
        ProductTagDTO productTagDTO = productTagMapper.toDto(productTag);

        int databaseSizeBeforeCreate = productTagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductTagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTag in the database
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductTags() throws Exception {
        // Initialize the database
        productTagRepository.saveAndFlush(productTag);

        // Get all the productTagList
        restProductTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getProductTag() throws Exception {
        // Initialize the database
        productTagRepository.saveAndFlush(productTag);

        // Get the productTag
        restProductTagMockMvc
            .perform(get(ENTITY_API_URL_ID, productTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productTag.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingProductTag() throws Exception {
        // Get the productTag
        restProductTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductTag() throws Exception {
        // Initialize the database
        productTagRepository.saveAndFlush(productTag);

        int databaseSizeBeforeUpdate = productTagRepository.findAll().size();

        // Update the productTag
        ProductTag updatedProductTag = productTagRepository.findById(productTag.getId()).get();
        // Disconnect from session so that the updates on updatedProductTag are not directly saved in db
        em.detach(updatedProductTag);
        updatedProductTag
            .categoryName(UPDATED_CATEGORY_NAME)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);
        ProductTagDTO productTagDTO = productTagMapper.toDto(updatedProductTag);

        restProductTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTagDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTagDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductTag in the database
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeUpdate);
        ProductTag testProductTag = productTagList.get(productTagList.size() - 1);
        assertThat(testProductTag.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testProductTag.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testProductTag.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductTag.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProductTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingProductTag() throws Exception {
        int databaseSizeBeforeUpdate = productTagRepository.findAll().size();
        productTag.setId(count.incrementAndGet());

        // Create the ProductTag
        ProductTagDTO productTagDTO = productTagMapper.toDto(productTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTagDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTag in the database
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductTag() throws Exception {
        int databaseSizeBeforeUpdate = productTagRepository.findAll().size();
        productTag.setId(count.incrementAndGet());

        // Create the ProductTag
        ProductTagDTO productTagDTO = productTagMapper.toDto(productTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTag in the database
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductTag() throws Exception {
        int databaseSizeBeforeUpdate = productTagRepository.findAll().size();
        productTag.setId(count.incrementAndGet());

        // Create the ProductTag
        ProductTagDTO productTagDTO = productTagMapper.toDto(productTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTagMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductTag in the database
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductTagWithPatch() throws Exception {
        // Initialize the database
        productTagRepository.saveAndFlush(productTag);

        int databaseSizeBeforeUpdate = productTagRepository.findAll().size();

        // Update the productTag using partial update
        ProductTag partialUpdatedProductTag = new ProductTag();
        partialUpdatedProductTag.setId(productTag.getId());

        partialUpdatedProductTag.imageUrl(UPDATED_IMAGE_URL).creationDate(UPDATED_CREATION_DATE).createdBy(UPDATED_CREATED_BY);

        restProductTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductTag.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductTag))
            )
            .andExpect(status().isOk());

        // Validate the ProductTag in the database
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeUpdate);
        ProductTag testProductTag = productTagList.get(productTagList.size() - 1);
        assertThat(testProductTag.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testProductTag.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testProductTag.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductTag.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProductTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateProductTagWithPatch() throws Exception {
        // Initialize the database
        productTagRepository.saveAndFlush(productTag);

        int databaseSizeBeforeUpdate = productTagRepository.findAll().size();

        // Update the productTag using partial update
        ProductTag partialUpdatedProductTag = new ProductTag();
        partialUpdatedProductTag.setId(productTag.getId());

        partialUpdatedProductTag
            .categoryName(UPDATED_CATEGORY_NAME)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restProductTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductTag.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductTag))
            )
            .andExpect(status().isOk());

        // Validate the ProductTag in the database
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeUpdate);
        ProductTag testProductTag = productTagList.get(productTagList.size() - 1);
        assertThat(testProductTag.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testProductTag.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testProductTag.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductTag.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProductTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingProductTag() throws Exception {
        int databaseSizeBeforeUpdate = productTagRepository.findAll().size();
        productTag.setId(count.incrementAndGet());

        // Create the ProductTag
        ProductTagDTO productTagDTO = productTagMapper.toDto(productTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productTagDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTag in the database
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductTag() throws Exception {
        int databaseSizeBeforeUpdate = productTagRepository.findAll().size();
        productTag.setId(count.incrementAndGet());

        // Create the ProductTag
        ProductTagDTO productTagDTO = productTagMapper.toDto(productTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTag in the database
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductTag() throws Exception {
        int databaseSizeBeforeUpdate = productTagRepository.findAll().size();
        productTag.setId(count.incrementAndGet());

        // Create the ProductTag
        ProductTagDTO productTagDTO = productTagMapper.toDto(productTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductTag in the database
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductTag() throws Exception {
        // Initialize the database
        productTagRepository.saveAndFlush(productTag);

        int databaseSizeBeforeDelete = productTagRepository.findAll().size();

        // Delete the productTag
        restProductTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, productTag.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductTag> productTagList = productTagRepository.findAll();
        assertThat(productTagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
