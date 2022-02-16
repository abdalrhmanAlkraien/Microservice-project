package asc.foods.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.user.IntegrationTest;
import asc.foods.user.domain.MealCustmize;
import asc.foods.user.repository.MealCustmizeRepository;
import asc.foods.user.service.dto.MealCustmizeDTO;
import asc.foods.user.service.mapper.MealCustmizeMapper;
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
 * Integration tests for the {@link MealCustmizeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MealCustmizeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_ORDER_NUMBER = 1L;
    private static final Long UPDATED_ORDER_NUMBER = 2L;

    private static final String ENTITY_API_URL = "/api/meal-custmizes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MealCustmizeRepository mealCustmizeRepository;

    @Autowired
    private MealCustmizeMapper mealCustmizeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMealCustmizeMockMvc;

    private MealCustmize mealCustmize;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MealCustmize createEntity(EntityManager em) {
        MealCustmize mealCustmize = new MealCustmize()
            .title(DEFAULT_TITLE)
            .price(DEFAULT_PRICE)
            .imageUrl(DEFAULT_IMAGE_URL)
            .orderNumber(DEFAULT_ORDER_NUMBER);
        return mealCustmize;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MealCustmize createUpdatedEntity(EntityManager em) {
        MealCustmize mealCustmize = new MealCustmize()
            .title(UPDATED_TITLE)
            .price(UPDATED_PRICE)
            .imageUrl(UPDATED_IMAGE_URL)
            .orderNumber(UPDATED_ORDER_NUMBER);
        return mealCustmize;
    }

    @BeforeEach
    public void initTest() {
        mealCustmize = createEntity(em);
    }

    @Test
    @Transactional
    void createMealCustmize() throws Exception {
        int databaseSizeBeforeCreate = mealCustmizeRepository.findAll().size();
        // Create the MealCustmize
        MealCustmizeDTO mealCustmizeDTO = mealCustmizeMapper.toDto(mealCustmize);
        restMealCustmizeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealCustmizeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MealCustmize in the database
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeCreate + 1);
        MealCustmize testMealCustmize = mealCustmizeList.get(mealCustmizeList.size() - 1);
        assertThat(testMealCustmize.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMealCustmize.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMealCustmize.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testMealCustmize.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void createMealCustmizeWithExistingId() throws Exception {
        // Create the MealCustmize with an existing ID
        mealCustmize.setId(1L);
        MealCustmizeDTO mealCustmizeDTO = mealCustmizeMapper.toDto(mealCustmize);

        int databaseSizeBeforeCreate = mealCustmizeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMealCustmizeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealCustmizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealCustmize in the database
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMealCustmizes() throws Exception {
        // Initialize the database
        mealCustmizeRepository.saveAndFlush(mealCustmize);

        // Get all the mealCustmizeList
        restMealCustmizeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mealCustmize.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.intValue())));
    }

    @Test
    @Transactional
    void getMealCustmize() throws Exception {
        // Initialize the database
        mealCustmizeRepository.saveAndFlush(mealCustmize);

        // Get the mealCustmize
        restMealCustmizeMockMvc
            .perform(get(ENTITY_API_URL_ID, mealCustmize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mealCustmize.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMealCustmize() throws Exception {
        // Get the mealCustmize
        restMealCustmizeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMealCustmize() throws Exception {
        // Initialize the database
        mealCustmizeRepository.saveAndFlush(mealCustmize);

        int databaseSizeBeforeUpdate = mealCustmizeRepository.findAll().size();

        // Update the mealCustmize
        MealCustmize updatedMealCustmize = mealCustmizeRepository.findById(mealCustmize.getId()).get();
        // Disconnect from session so that the updates on updatedMealCustmize are not directly saved in db
        em.detach(updatedMealCustmize);
        updatedMealCustmize.title(UPDATED_TITLE).price(UPDATED_PRICE).imageUrl(UPDATED_IMAGE_URL).orderNumber(UPDATED_ORDER_NUMBER);
        MealCustmizeDTO mealCustmizeDTO = mealCustmizeMapper.toDto(updatedMealCustmize);

        restMealCustmizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mealCustmizeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealCustmizeDTO))
            )
            .andExpect(status().isOk());

        // Validate the MealCustmize in the database
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeUpdate);
        MealCustmize testMealCustmize = mealCustmizeList.get(mealCustmizeList.size() - 1);
        assertThat(testMealCustmize.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMealCustmize.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMealCustmize.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testMealCustmize.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingMealCustmize() throws Exception {
        int databaseSizeBeforeUpdate = mealCustmizeRepository.findAll().size();
        mealCustmize.setId(count.incrementAndGet());

        // Create the MealCustmize
        MealCustmizeDTO mealCustmizeDTO = mealCustmizeMapper.toDto(mealCustmize);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMealCustmizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mealCustmizeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealCustmizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealCustmize in the database
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMealCustmize() throws Exception {
        int databaseSizeBeforeUpdate = mealCustmizeRepository.findAll().size();
        mealCustmize.setId(count.incrementAndGet());

        // Create the MealCustmize
        MealCustmizeDTO mealCustmizeDTO = mealCustmizeMapper.toDto(mealCustmize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealCustmizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealCustmizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealCustmize in the database
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMealCustmize() throws Exception {
        int databaseSizeBeforeUpdate = mealCustmizeRepository.findAll().size();
        mealCustmize.setId(count.incrementAndGet());

        // Create the MealCustmize
        MealCustmizeDTO mealCustmizeDTO = mealCustmizeMapper.toDto(mealCustmize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealCustmizeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealCustmizeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MealCustmize in the database
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMealCustmizeWithPatch() throws Exception {
        // Initialize the database
        mealCustmizeRepository.saveAndFlush(mealCustmize);

        int databaseSizeBeforeUpdate = mealCustmizeRepository.findAll().size();

        // Update the mealCustmize using partial update
        MealCustmize partialUpdatedMealCustmize = new MealCustmize();
        partialUpdatedMealCustmize.setId(mealCustmize.getId());

        partialUpdatedMealCustmize.price(UPDATED_PRICE).imageUrl(UPDATED_IMAGE_URL).orderNumber(UPDATED_ORDER_NUMBER);

        restMealCustmizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMealCustmize.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMealCustmize))
            )
            .andExpect(status().isOk());

        // Validate the MealCustmize in the database
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeUpdate);
        MealCustmize testMealCustmize = mealCustmizeList.get(mealCustmizeList.size() - 1);
        assertThat(testMealCustmize.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMealCustmize.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMealCustmize.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testMealCustmize.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateMealCustmizeWithPatch() throws Exception {
        // Initialize the database
        mealCustmizeRepository.saveAndFlush(mealCustmize);

        int databaseSizeBeforeUpdate = mealCustmizeRepository.findAll().size();

        // Update the mealCustmize using partial update
        MealCustmize partialUpdatedMealCustmize = new MealCustmize();
        partialUpdatedMealCustmize.setId(mealCustmize.getId());

        partialUpdatedMealCustmize.title(UPDATED_TITLE).price(UPDATED_PRICE).imageUrl(UPDATED_IMAGE_URL).orderNumber(UPDATED_ORDER_NUMBER);

        restMealCustmizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMealCustmize.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMealCustmize))
            )
            .andExpect(status().isOk());

        // Validate the MealCustmize in the database
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeUpdate);
        MealCustmize testMealCustmize = mealCustmizeList.get(mealCustmizeList.size() - 1);
        assertThat(testMealCustmize.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMealCustmize.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMealCustmize.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testMealCustmize.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingMealCustmize() throws Exception {
        int databaseSizeBeforeUpdate = mealCustmizeRepository.findAll().size();
        mealCustmize.setId(count.incrementAndGet());

        // Create the MealCustmize
        MealCustmizeDTO mealCustmizeDTO = mealCustmizeMapper.toDto(mealCustmize);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMealCustmizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mealCustmizeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealCustmizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealCustmize in the database
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMealCustmize() throws Exception {
        int databaseSizeBeforeUpdate = mealCustmizeRepository.findAll().size();
        mealCustmize.setId(count.incrementAndGet());

        // Create the MealCustmize
        MealCustmizeDTO mealCustmizeDTO = mealCustmizeMapper.toDto(mealCustmize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealCustmizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealCustmizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealCustmize in the database
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMealCustmize() throws Exception {
        int databaseSizeBeforeUpdate = mealCustmizeRepository.findAll().size();
        mealCustmize.setId(count.incrementAndGet());

        // Create the MealCustmize
        MealCustmizeDTO mealCustmizeDTO = mealCustmizeMapper.toDto(mealCustmize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealCustmizeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealCustmizeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MealCustmize in the database
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMealCustmize() throws Exception {
        // Initialize the database
        mealCustmizeRepository.saveAndFlush(mealCustmize);

        int databaseSizeBeforeDelete = mealCustmizeRepository.findAll().size();

        // Delete the mealCustmize
        restMealCustmizeMockMvc
            .perform(delete(ENTITY_API_URL_ID, mealCustmize.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MealCustmize> mealCustmizeList = mealCustmizeRepository.findAll();
        assertThat(mealCustmizeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
