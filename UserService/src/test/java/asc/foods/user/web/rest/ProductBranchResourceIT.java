package asc.foods.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.user.IntegrationTest;
import asc.foods.user.domain.ProductBranch;
import asc.foods.user.domain.enumeration.ProductStatus;
import asc.foods.user.repository.ProductBranchRepository;
import asc.foods.user.service.dto.ProductBranchDTO;
import asc.foods.user.service.mapper.ProductBranchMapper;
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
 * Integration tests for the {@link ProductBranchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductBranchResourceIT {

    private static final ProductStatus DEFAULT_PRODUCT_STATUS = ProductStatus.UNAVAILABLE;
    private static final ProductStatus UPDATED_PRODUCT_STATUS = ProductStatus.OUTOFSTOCK;

    private static final String ENTITY_API_URL = "/api/product-branches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductBranchRepository productBranchRepository;

    @Autowired
    private ProductBranchMapper productBranchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductBranchMockMvc;

    private ProductBranch productBranch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductBranch createEntity(EntityManager em) {
        ProductBranch productBranch = new ProductBranch().productStatus(DEFAULT_PRODUCT_STATUS);
        return productBranch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductBranch createUpdatedEntity(EntityManager em) {
        ProductBranch productBranch = new ProductBranch().productStatus(UPDATED_PRODUCT_STATUS);
        return productBranch;
    }

    @BeforeEach
    public void initTest() {
        productBranch = createEntity(em);
    }

    @Test
    @Transactional
    void createProductBranch() throws Exception {
        int databaseSizeBeforeCreate = productBranchRepository.findAll().size();
        // Create the ProductBranch
        ProductBranchDTO productBranchDTO = productBranchMapper.toDto(productBranch);
        restProductBranchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productBranchDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductBranch in the database
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeCreate + 1);
        ProductBranch testProductBranch = productBranchList.get(productBranchList.size() - 1);
        assertThat(testProductBranch.getProductStatus()).isEqualTo(DEFAULT_PRODUCT_STATUS);
    }

    @Test
    @Transactional
    void createProductBranchWithExistingId() throws Exception {
        // Create the ProductBranch with an existing ID
        productBranch.setId(1L);
        ProductBranchDTO productBranchDTO = productBranchMapper.toDto(productBranch);

        int databaseSizeBeforeCreate = productBranchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductBranchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBranch in the database
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductBranches() throws Exception {
        // Initialize the database
        productBranchRepository.saveAndFlush(productBranch);

        // Get all the productBranchList
        restProductBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productBranch.getId().intValue())))
            .andExpect(jsonPath("$.[*].productStatus").value(hasItem(DEFAULT_PRODUCT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getProductBranch() throws Exception {
        // Initialize the database
        productBranchRepository.saveAndFlush(productBranch);

        // Get the productBranch
        restProductBranchMockMvc
            .perform(get(ENTITY_API_URL_ID, productBranch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productBranch.getId().intValue()))
            .andExpect(jsonPath("$.productStatus").value(DEFAULT_PRODUCT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductBranch() throws Exception {
        // Get the productBranch
        restProductBranchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductBranch() throws Exception {
        // Initialize the database
        productBranchRepository.saveAndFlush(productBranch);

        int databaseSizeBeforeUpdate = productBranchRepository.findAll().size();

        // Update the productBranch
        ProductBranch updatedProductBranch = productBranchRepository.findById(productBranch.getId()).get();
        // Disconnect from session so that the updates on updatedProductBranch are not directly saved in db
        em.detach(updatedProductBranch);
        updatedProductBranch.productStatus(UPDATED_PRODUCT_STATUS);
        ProductBranchDTO productBranchDTO = productBranchMapper.toDto(updatedProductBranch);

        restProductBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productBranchDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productBranchDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductBranch in the database
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeUpdate);
        ProductBranch testProductBranch = productBranchList.get(productBranchList.size() - 1);
        assertThat(testProductBranch.getProductStatus()).isEqualTo(UPDATED_PRODUCT_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingProductBranch() throws Exception {
        int databaseSizeBeforeUpdate = productBranchRepository.findAll().size();
        productBranch.setId(count.incrementAndGet());

        // Create the ProductBranch
        ProductBranchDTO productBranchDTO = productBranchMapper.toDto(productBranch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productBranchDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBranch in the database
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductBranch() throws Exception {
        int databaseSizeBeforeUpdate = productBranchRepository.findAll().size();
        productBranch.setId(count.incrementAndGet());

        // Create the ProductBranch
        ProductBranchDTO productBranchDTO = productBranchMapper.toDto(productBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBranch in the database
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductBranch() throws Exception {
        int databaseSizeBeforeUpdate = productBranchRepository.findAll().size();
        productBranch.setId(count.incrementAndGet());

        // Create the ProductBranch
        ProductBranchDTO productBranchDTO = productBranchMapper.toDto(productBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBranchMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productBranchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductBranch in the database
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductBranchWithPatch() throws Exception {
        // Initialize the database
        productBranchRepository.saveAndFlush(productBranch);

        int databaseSizeBeforeUpdate = productBranchRepository.findAll().size();

        // Update the productBranch using partial update
        ProductBranch partialUpdatedProductBranch = new ProductBranch();
        partialUpdatedProductBranch.setId(productBranch.getId());

        restProductBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductBranch.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductBranch))
            )
            .andExpect(status().isOk());

        // Validate the ProductBranch in the database
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeUpdate);
        ProductBranch testProductBranch = productBranchList.get(productBranchList.size() - 1);
        assertThat(testProductBranch.getProductStatus()).isEqualTo(DEFAULT_PRODUCT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateProductBranchWithPatch() throws Exception {
        // Initialize the database
        productBranchRepository.saveAndFlush(productBranch);

        int databaseSizeBeforeUpdate = productBranchRepository.findAll().size();

        // Update the productBranch using partial update
        ProductBranch partialUpdatedProductBranch = new ProductBranch();
        partialUpdatedProductBranch.setId(productBranch.getId());

        partialUpdatedProductBranch.productStatus(UPDATED_PRODUCT_STATUS);

        restProductBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductBranch.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductBranch))
            )
            .andExpect(status().isOk());

        // Validate the ProductBranch in the database
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeUpdate);
        ProductBranch testProductBranch = productBranchList.get(productBranchList.size() - 1);
        assertThat(testProductBranch.getProductStatus()).isEqualTo(UPDATED_PRODUCT_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingProductBranch() throws Exception {
        int databaseSizeBeforeUpdate = productBranchRepository.findAll().size();
        productBranch.setId(count.incrementAndGet());

        // Create the ProductBranch
        ProductBranchDTO productBranchDTO = productBranchMapper.toDto(productBranch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productBranchDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBranch in the database
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductBranch() throws Exception {
        int databaseSizeBeforeUpdate = productBranchRepository.findAll().size();
        productBranch.setId(count.incrementAndGet());

        // Create the ProductBranch
        ProductBranchDTO productBranchDTO = productBranchMapper.toDto(productBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBranch in the database
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductBranch() throws Exception {
        int databaseSizeBeforeUpdate = productBranchRepository.findAll().size();
        productBranch.setId(count.incrementAndGet());

        // Create the ProductBranch
        ProductBranchDTO productBranchDTO = productBranchMapper.toDto(productBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBranchMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productBranchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductBranch in the database
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductBranch() throws Exception {
        // Initialize the database
        productBranchRepository.saveAndFlush(productBranch);

        int databaseSizeBeforeDelete = productBranchRepository.findAll().size();

        // Delete the productBranch
        restProductBranchMockMvc
            .perform(delete(ENTITY_API_URL_ID, productBranch.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductBranch> productBranchList = productBranchRepository.findAll();
        assertThat(productBranchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
