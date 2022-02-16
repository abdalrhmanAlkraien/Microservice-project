package asc.foods.store.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.store.IntegrationTest;
import asc.foods.store.domain.UserProduct;
import asc.foods.store.repository.UserProductRepository;
import asc.foods.store.service.dto.UserProductDTO;
import asc.foods.store.service.mapper.UserProductMapper;
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
 * Integration tests for the {@link UserProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserProductResourceIT {

    private static final Boolean DEFAULT_IS_FAVORITE = false;
    private static final Boolean UPDATED_IS_FAVORITE = true;

    private static final Long DEFAULT_ORDERED_TIMES = 1L;
    private static final Long UPDATED_ORDERED_TIMES = 2L;

    private static final Instant DEFAULT_LAST_TIME_ORDERED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_TIME_ORDERED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserProductRepository userProductRepository;

    @Autowired
    private UserProductMapper userProductMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserProductMockMvc;

    private UserProduct userProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProduct createEntity(EntityManager em) {
        UserProduct userProduct = new UserProduct()
            .isFavorite(DEFAULT_IS_FAVORITE)
            .orderedTimes(DEFAULT_ORDERED_TIMES)
            .lastTimeOrdered(DEFAULT_LAST_TIME_ORDERED);
        return userProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProduct createUpdatedEntity(EntityManager em) {
        UserProduct userProduct = new UserProduct()
            .isFavorite(UPDATED_IS_FAVORITE)
            .orderedTimes(UPDATED_ORDERED_TIMES)
            .lastTimeOrdered(UPDATED_LAST_TIME_ORDERED);
        return userProduct;
    }

    @BeforeEach
    public void initTest() {
        userProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createUserProduct() throws Exception {
        int databaseSizeBeforeCreate = userProductRepository.findAll().size();
        // Create the UserProduct
        UserProductDTO userProductDTO = userProductMapper.toDto(userProduct);
        restUserProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProductDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserProduct in the database
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeCreate + 1);
        UserProduct testUserProduct = userProductList.get(userProductList.size() - 1);
        assertThat(testUserProduct.getIsFavorite()).isEqualTo(DEFAULT_IS_FAVORITE);
        assertThat(testUserProduct.getOrderedTimes()).isEqualTo(DEFAULT_ORDERED_TIMES);
        assertThat(testUserProduct.getLastTimeOrdered()).isEqualTo(DEFAULT_LAST_TIME_ORDERED);
    }

    @Test
    @Transactional
    void createUserProductWithExistingId() throws Exception {
        // Create the UserProduct with an existing ID
        userProduct.setId(1L);
        UserProductDTO userProductDTO = userProductMapper.toDto(userProduct);

        int databaseSizeBeforeCreate = userProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProduct in the database
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserProducts() throws Exception {
        // Initialize the database
        userProductRepository.saveAndFlush(userProduct);

        // Get all the userProductList
        restUserProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].isFavorite").value(hasItem(DEFAULT_IS_FAVORITE.booleanValue())))
            .andExpect(jsonPath("$.[*].orderedTimes").value(hasItem(DEFAULT_ORDERED_TIMES.intValue())))
            .andExpect(jsonPath("$.[*].lastTimeOrdered").value(hasItem(DEFAULT_LAST_TIME_ORDERED.toString())));
    }

    @Test
    @Transactional
    void getUserProduct() throws Exception {
        // Initialize the database
        userProductRepository.saveAndFlush(userProduct);

        // Get the userProduct
        restUserProductMockMvc
            .perform(get(ENTITY_API_URL_ID, userProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userProduct.getId().intValue()))
            .andExpect(jsonPath("$.isFavorite").value(DEFAULT_IS_FAVORITE.booleanValue()))
            .andExpect(jsonPath("$.orderedTimes").value(DEFAULT_ORDERED_TIMES.intValue()))
            .andExpect(jsonPath("$.lastTimeOrdered").value(DEFAULT_LAST_TIME_ORDERED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserProduct() throws Exception {
        // Get the userProduct
        restUserProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserProduct() throws Exception {
        // Initialize the database
        userProductRepository.saveAndFlush(userProduct);

        int databaseSizeBeforeUpdate = userProductRepository.findAll().size();

        // Update the userProduct
        UserProduct updatedUserProduct = userProductRepository.findById(userProduct.getId()).get();
        // Disconnect from session so that the updates on updatedUserProduct are not directly saved in db
        em.detach(updatedUserProduct);
        updatedUserProduct.isFavorite(UPDATED_IS_FAVORITE).orderedTimes(UPDATED_ORDERED_TIMES).lastTimeOrdered(UPDATED_LAST_TIME_ORDERED);
        UserProductDTO userProductDTO = userProductMapper.toDto(updatedUserProduct);

        restUserProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userProductDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProductDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserProduct in the database
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeUpdate);
        UserProduct testUserProduct = userProductList.get(userProductList.size() - 1);
        assertThat(testUserProduct.getIsFavorite()).isEqualTo(UPDATED_IS_FAVORITE);
        assertThat(testUserProduct.getOrderedTimes()).isEqualTo(UPDATED_ORDERED_TIMES);
        assertThat(testUserProduct.getLastTimeOrdered()).isEqualTo(UPDATED_LAST_TIME_ORDERED);
    }

    @Test
    @Transactional
    void putNonExistingUserProduct() throws Exception {
        int databaseSizeBeforeUpdate = userProductRepository.findAll().size();
        userProduct.setId(count.incrementAndGet());

        // Create the UserProduct
        UserProductDTO userProductDTO = userProductMapper.toDto(userProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userProductDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProduct in the database
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserProduct() throws Exception {
        int databaseSizeBeforeUpdate = userProductRepository.findAll().size();
        userProduct.setId(count.incrementAndGet());

        // Create the UserProduct
        UserProductDTO userProductDTO = userProductMapper.toDto(userProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProduct in the database
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserProduct() throws Exception {
        int databaseSizeBeforeUpdate = userProductRepository.findAll().size();
        userProduct.setId(count.incrementAndGet());

        // Create the UserProduct
        UserProductDTO userProductDTO = userProductMapper.toDto(userProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProductMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProduct in the database
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserProductWithPatch() throws Exception {
        // Initialize the database
        userProductRepository.saveAndFlush(userProduct);

        int databaseSizeBeforeUpdate = userProductRepository.findAll().size();

        // Update the userProduct using partial update
        UserProduct partialUpdatedUserProduct = new UserProduct();
        partialUpdatedUserProduct.setId(userProduct.getId());

        partialUpdatedUserProduct.lastTimeOrdered(UPDATED_LAST_TIME_ORDERED);

        restUserProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProduct.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProduct))
            )
            .andExpect(status().isOk());

        // Validate the UserProduct in the database
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeUpdate);
        UserProduct testUserProduct = userProductList.get(userProductList.size() - 1);
        assertThat(testUserProduct.getIsFavorite()).isEqualTo(DEFAULT_IS_FAVORITE);
        assertThat(testUserProduct.getOrderedTimes()).isEqualTo(DEFAULT_ORDERED_TIMES);
        assertThat(testUserProduct.getLastTimeOrdered()).isEqualTo(UPDATED_LAST_TIME_ORDERED);
    }

    @Test
    @Transactional
    void fullUpdateUserProductWithPatch() throws Exception {
        // Initialize the database
        userProductRepository.saveAndFlush(userProduct);

        int databaseSizeBeforeUpdate = userProductRepository.findAll().size();

        // Update the userProduct using partial update
        UserProduct partialUpdatedUserProduct = new UserProduct();
        partialUpdatedUserProduct.setId(userProduct.getId());

        partialUpdatedUserProduct
            .isFavorite(UPDATED_IS_FAVORITE)
            .orderedTimes(UPDATED_ORDERED_TIMES)
            .lastTimeOrdered(UPDATED_LAST_TIME_ORDERED);

        restUserProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProduct.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProduct))
            )
            .andExpect(status().isOk());

        // Validate the UserProduct in the database
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeUpdate);
        UserProduct testUserProduct = userProductList.get(userProductList.size() - 1);
        assertThat(testUserProduct.getIsFavorite()).isEqualTo(UPDATED_IS_FAVORITE);
        assertThat(testUserProduct.getOrderedTimes()).isEqualTo(UPDATED_ORDERED_TIMES);
        assertThat(testUserProduct.getLastTimeOrdered()).isEqualTo(UPDATED_LAST_TIME_ORDERED);
    }

    @Test
    @Transactional
    void patchNonExistingUserProduct() throws Exception {
        int databaseSizeBeforeUpdate = userProductRepository.findAll().size();
        userProduct.setId(count.incrementAndGet());

        // Create the UserProduct
        UserProductDTO userProductDTO = userProductMapper.toDto(userProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userProductDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProduct in the database
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserProduct() throws Exception {
        int databaseSizeBeforeUpdate = userProductRepository.findAll().size();
        userProduct.setId(count.incrementAndGet());

        // Create the UserProduct
        UserProductDTO userProductDTO = userProductMapper.toDto(userProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProduct in the database
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserProduct() throws Exception {
        int databaseSizeBeforeUpdate = userProductRepository.findAll().size();
        userProduct.setId(count.incrementAndGet());

        // Create the UserProduct
        UserProductDTO userProductDTO = userProductMapper.toDto(userProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProduct in the database
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserProduct() throws Exception {
        // Initialize the database
        userProductRepository.saveAndFlush(userProduct);

        int databaseSizeBeforeDelete = userProductRepository.findAll().size();

        // Delete the userProduct
        restUserProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, userProduct.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserProduct> userProductList = userProductRepository.findAll();
        assertThat(userProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
