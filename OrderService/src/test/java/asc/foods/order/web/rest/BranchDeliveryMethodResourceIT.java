package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.BranchDeliveryMethod;
import asc.foods.order.domain.enumeration.DeliveryMethod;
import asc.foods.order.repository.BranchDeliveryMethodRepository;
import asc.foods.order.service.dto.BranchDeliveryMethodDTO;
import asc.foods.order.service.mapper.BranchDeliveryMethodMapper;
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
 * Integration tests for the {@link BranchDeliveryMethodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BranchDeliveryMethodResourceIT {

    private static final DeliveryMethod DEFAULT_DELIVERY_METOD = DeliveryMethod.STORE;
    private static final DeliveryMethod UPDATED_DELIVERY_METOD = DeliveryMethod.US;

    private static final String ENTITY_API_URL = "/api/branch-delivery-methods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BranchDeliveryMethodRepository branchDeliveryMethodRepository;

    @Autowired
    private BranchDeliveryMethodMapper branchDeliveryMethodMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBranchDeliveryMethodMockMvc;

    private BranchDeliveryMethod branchDeliveryMethod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchDeliveryMethod createEntity(EntityManager em) {
        BranchDeliveryMethod branchDeliveryMethod = new BranchDeliveryMethod().deliveryMetod(DEFAULT_DELIVERY_METOD);
        return branchDeliveryMethod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchDeliveryMethod createUpdatedEntity(EntityManager em) {
        BranchDeliveryMethod branchDeliveryMethod = new BranchDeliveryMethod().deliveryMetod(UPDATED_DELIVERY_METOD);
        return branchDeliveryMethod;
    }

    @BeforeEach
    public void initTest() {
        branchDeliveryMethod = createEntity(em);
    }

    @Test
    @Transactional
    void createBranchDeliveryMethod() throws Exception {
        int databaseSizeBeforeCreate = branchDeliveryMethodRepository.findAll().size();
        // Create the BranchDeliveryMethod
        BranchDeliveryMethodDTO branchDeliveryMethodDTO = branchDeliveryMethodMapper.toDto(branchDeliveryMethod);
        restBranchDeliveryMethodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDeliveryMethodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BranchDeliveryMethod in the database
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeCreate + 1);
        BranchDeliveryMethod testBranchDeliveryMethod = branchDeliveryMethodList.get(branchDeliveryMethodList.size() - 1);
        assertThat(testBranchDeliveryMethod.getDeliveryMetod()).isEqualTo(DEFAULT_DELIVERY_METOD);
    }

    @Test
    @Transactional
    void createBranchDeliveryMethodWithExistingId() throws Exception {
        // Create the BranchDeliveryMethod with an existing ID
        branchDeliveryMethod.setId(1L);
        BranchDeliveryMethodDTO branchDeliveryMethodDTO = branchDeliveryMethodMapper.toDto(branchDeliveryMethod);

        int databaseSizeBeforeCreate = branchDeliveryMethodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchDeliveryMethodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDeliveryMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchDeliveryMethod in the database
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBranchDeliveryMethods() throws Exception {
        // Initialize the database
        branchDeliveryMethodRepository.saveAndFlush(branchDeliveryMethod);

        // Get all the branchDeliveryMethodList
        restBranchDeliveryMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchDeliveryMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].deliveryMetod").value(hasItem(DEFAULT_DELIVERY_METOD.toString())));
    }

    @Test
    @Transactional
    void getBranchDeliveryMethod() throws Exception {
        // Initialize the database
        branchDeliveryMethodRepository.saveAndFlush(branchDeliveryMethod);

        // Get the branchDeliveryMethod
        restBranchDeliveryMethodMockMvc
            .perform(get(ENTITY_API_URL_ID, branchDeliveryMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branchDeliveryMethod.getId().intValue()))
            .andExpect(jsonPath("$.deliveryMetod").value(DEFAULT_DELIVERY_METOD.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBranchDeliveryMethod() throws Exception {
        // Get the branchDeliveryMethod
        restBranchDeliveryMethodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBranchDeliveryMethod() throws Exception {
        // Initialize the database
        branchDeliveryMethodRepository.saveAndFlush(branchDeliveryMethod);

        int databaseSizeBeforeUpdate = branchDeliveryMethodRepository.findAll().size();

        // Update the branchDeliveryMethod
        BranchDeliveryMethod updatedBranchDeliveryMethod = branchDeliveryMethodRepository.findById(branchDeliveryMethod.getId()).get();
        // Disconnect from session so that the updates on updatedBranchDeliveryMethod are not directly saved in db
        em.detach(updatedBranchDeliveryMethod);
        updatedBranchDeliveryMethod.deliveryMetod(UPDATED_DELIVERY_METOD);
        BranchDeliveryMethodDTO branchDeliveryMethodDTO = branchDeliveryMethodMapper.toDto(updatedBranchDeliveryMethod);

        restBranchDeliveryMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchDeliveryMethodDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDeliveryMethodDTO))
            )
            .andExpect(status().isOk());

        // Validate the BranchDeliveryMethod in the database
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeUpdate);
        BranchDeliveryMethod testBranchDeliveryMethod = branchDeliveryMethodList.get(branchDeliveryMethodList.size() - 1);
        assertThat(testBranchDeliveryMethod.getDeliveryMetod()).isEqualTo(UPDATED_DELIVERY_METOD);
    }

    @Test
    @Transactional
    void putNonExistingBranchDeliveryMethod() throws Exception {
        int databaseSizeBeforeUpdate = branchDeliveryMethodRepository.findAll().size();
        branchDeliveryMethod.setId(count.incrementAndGet());

        // Create the BranchDeliveryMethod
        BranchDeliveryMethodDTO branchDeliveryMethodDTO = branchDeliveryMethodMapper.toDto(branchDeliveryMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchDeliveryMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchDeliveryMethodDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDeliveryMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchDeliveryMethod in the database
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBranchDeliveryMethod() throws Exception {
        int databaseSizeBeforeUpdate = branchDeliveryMethodRepository.findAll().size();
        branchDeliveryMethod.setId(count.incrementAndGet());

        // Create the BranchDeliveryMethod
        BranchDeliveryMethodDTO branchDeliveryMethodDTO = branchDeliveryMethodMapper.toDto(branchDeliveryMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchDeliveryMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDeliveryMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchDeliveryMethod in the database
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBranchDeliveryMethod() throws Exception {
        int databaseSizeBeforeUpdate = branchDeliveryMethodRepository.findAll().size();
        branchDeliveryMethod.setId(count.incrementAndGet());

        // Create the BranchDeliveryMethod
        BranchDeliveryMethodDTO branchDeliveryMethodDTO = branchDeliveryMethodMapper.toDto(branchDeliveryMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchDeliveryMethodMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDeliveryMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BranchDeliveryMethod in the database
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBranchDeliveryMethodWithPatch() throws Exception {
        // Initialize the database
        branchDeliveryMethodRepository.saveAndFlush(branchDeliveryMethod);

        int databaseSizeBeforeUpdate = branchDeliveryMethodRepository.findAll().size();

        // Update the branchDeliveryMethod using partial update
        BranchDeliveryMethod partialUpdatedBranchDeliveryMethod = new BranchDeliveryMethod();
        partialUpdatedBranchDeliveryMethod.setId(branchDeliveryMethod.getId());

        partialUpdatedBranchDeliveryMethod.deliveryMetod(UPDATED_DELIVERY_METOD);

        restBranchDeliveryMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchDeliveryMethod.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranchDeliveryMethod))
            )
            .andExpect(status().isOk());

        // Validate the BranchDeliveryMethod in the database
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeUpdate);
        BranchDeliveryMethod testBranchDeliveryMethod = branchDeliveryMethodList.get(branchDeliveryMethodList.size() - 1);
        assertThat(testBranchDeliveryMethod.getDeliveryMetod()).isEqualTo(UPDATED_DELIVERY_METOD);
    }

    @Test
    @Transactional
    void fullUpdateBranchDeliveryMethodWithPatch() throws Exception {
        // Initialize the database
        branchDeliveryMethodRepository.saveAndFlush(branchDeliveryMethod);

        int databaseSizeBeforeUpdate = branchDeliveryMethodRepository.findAll().size();

        // Update the branchDeliveryMethod using partial update
        BranchDeliveryMethod partialUpdatedBranchDeliveryMethod = new BranchDeliveryMethod();
        partialUpdatedBranchDeliveryMethod.setId(branchDeliveryMethod.getId());

        partialUpdatedBranchDeliveryMethod.deliveryMetod(UPDATED_DELIVERY_METOD);

        restBranchDeliveryMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchDeliveryMethod.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranchDeliveryMethod))
            )
            .andExpect(status().isOk());

        // Validate the BranchDeliveryMethod in the database
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeUpdate);
        BranchDeliveryMethod testBranchDeliveryMethod = branchDeliveryMethodList.get(branchDeliveryMethodList.size() - 1);
        assertThat(testBranchDeliveryMethod.getDeliveryMetod()).isEqualTo(UPDATED_DELIVERY_METOD);
    }

    @Test
    @Transactional
    void patchNonExistingBranchDeliveryMethod() throws Exception {
        int databaseSizeBeforeUpdate = branchDeliveryMethodRepository.findAll().size();
        branchDeliveryMethod.setId(count.incrementAndGet());

        // Create the BranchDeliveryMethod
        BranchDeliveryMethodDTO branchDeliveryMethodDTO = branchDeliveryMethodMapper.toDto(branchDeliveryMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchDeliveryMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, branchDeliveryMethodDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchDeliveryMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchDeliveryMethod in the database
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBranchDeliveryMethod() throws Exception {
        int databaseSizeBeforeUpdate = branchDeliveryMethodRepository.findAll().size();
        branchDeliveryMethod.setId(count.incrementAndGet());

        // Create the BranchDeliveryMethod
        BranchDeliveryMethodDTO branchDeliveryMethodDTO = branchDeliveryMethodMapper.toDto(branchDeliveryMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchDeliveryMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchDeliveryMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchDeliveryMethod in the database
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBranchDeliveryMethod() throws Exception {
        int databaseSizeBeforeUpdate = branchDeliveryMethodRepository.findAll().size();
        branchDeliveryMethod.setId(count.incrementAndGet());

        // Create the BranchDeliveryMethod
        BranchDeliveryMethodDTO branchDeliveryMethodDTO = branchDeliveryMethodMapper.toDto(branchDeliveryMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchDeliveryMethodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchDeliveryMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BranchDeliveryMethod in the database
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBranchDeliveryMethod() throws Exception {
        // Initialize the database
        branchDeliveryMethodRepository.saveAndFlush(branchDeliveryMethod);

        int databaseSizeBeforeDelete = branchDeliveryMethodRepository.findAll().size();

        // Delete the branchDeliveryMethod
        restBranchDeliveryMethodMockMvc
            .perform(delete(ENTITY_API_URL_ID, branchDeliveryMethod.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BranchDeliveryMethod> branchDeliveryMethodList = branchDeliveryMethodRepository.findAll();
        assertThat(branchDeliveryMethodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
