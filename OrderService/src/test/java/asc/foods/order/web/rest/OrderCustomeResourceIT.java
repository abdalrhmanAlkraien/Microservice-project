package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.OrderCustome;
import asc.foods.order.repository.OrderCustomeRepository;
import asc.foods.order.service.dto.OrderCustomeDTO;
import asc.foods.order.service.mapper.OrderCustomeMapper;
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
 * Integration tests for the {@link OrderCustomeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderCustomeResourceIT {

    private static final String ENTITY_API_URL = "/api/order-customes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderCustomeRepository orderCustomeRepository;

    @Autowired
    private OrderCustomeMapper orderCustomeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderCustomeMockMvc;

    private OrderCustome orderCustome;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderCustome createEntity(EntityManager em) {
        OrderCustome orderCustome = new OrderCustome();
        return orderCustome;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderCustome createUpdatedEntity(EntityManager em) {
        OrderCustome orderCustome = new OrderCustome();
        return orderCustome;
    }

    @BeforeEach
    public void initTest() {
        orderCustome = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderCustome() throws Exception {
        int databaseSizeBeforeCreate = orderCustomeRepository.findAll().size();
        // Create the OrderCustome
        OrderCustomeDTO orderCustomeDTO = orderCustomeMapper.toDto(orderCustome);
        restOrderCustomeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderCustome in the database
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeCreate + 1);
        OrderCustome testOrderCustome = orderCustomeList.get(orderCustomeList.size() - 1);
    }

    @Test
    @Transactional
    void createOrderCustomeWithExistingId() throws Exception {
        // Create the OrderCustome with an existing ID
        orderCustome.setId(1L);
        OrderCustomeDTO orderCustomeDTO = orderCustomeMapper.toDto(orderCustome);

        int databaseSizeBeforeCreate = orderCustomeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderCustomeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustome in the database
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderCustomes() throws Exception {
        // Initialize the database
        orderCustomeRepository.saveAndFlush(orderCustome);

        // Get all the orderCustomeList
        restOrderCustomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderCustome.getId().intValue())));
    }

    @Test
    @Transactional
    void getOrderCustome() throws Exception {
        // Initialize the database
        orderCustomeRepository.saveAndFlush(orderCustome);

        // Get the orderCustome
        restOrderCustomeMockMvc
            .perform(get(ENTITY_API_URL_ID, orderCustome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderCustome.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrderCustome() throws Exception {
        // Get the orderCustome
        restOrderCustomeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderCustome() throws Exception {
        // Initialize the database
        orderCustomeRepository.saveAndFlush(orderCustome);

        int databaseSizeBeforeUpdate = orderCustomeRepository.findAll().size();

        // Update the orderCustome
        OrderCustome updatedOrderCustome = orderCustomeRepository.findById(orderCustome.getId()).get();
        // Disconnect from session so that the updates on updatedOrderCustome are not directly saved in db
        em.detach(updatedOrderCustome);
        OrderCustomeDTO orderCustomeDTO = orderCustomeMapper.toDto(updatedOrderCustome);

        restOrderCustomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderCustomeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomeDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderCustome in the database
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeUpdate);
        OrderCustome testOrderCustome = orderCustomeList.get(orderCustomeList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingOrderCustome() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomeRepository.findAll().size();
        orderCustome.setId(count.incrementAndGet());

        // Create the OrderCustome
        OrderCustomeDTO orderCustomeDTO = orderCustomeMapper.toDto(orderCustome);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderCustomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderCustomeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustome in the database
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderCustome() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomeRepository.findAll().size();
        orderCustome.setId(count.incrementAndGet());

        // Create the OrderCustome
        OrderCustomeDTO orderCustomeDTO = orderCustomeMapper.toDto(orderCustome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustome in the database
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderCustome() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomeRepository.findAll().size();
        orderCustome.setId(count.incrementAndGet());

        // Create the OrderCustome
        OrderCustomeDTO orderCustomeDTO = orderCustomeMapper.toDto(orderCustome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderCustome in the database
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderCustomeWithPatch() throws Exception {
        // Initialize the database
        orderCustomeRepository.saveAndFlush(orderCustome);

        int databaseSizeBeforeUpdate = orderCustomeRepository.findAll().size();

        // Update the orderCustome using partial update
        OrderCustome partialUpdatedOrderCustome = new OrderCustome();
        partialUpdatedOrderCustome.setId(orderCustome.getId());

        restOrderCustomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderCustome.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderCustome))
            )
            .andExpect(status().isOk());

        // Validate the OrderCustome in the database
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeUpdate);
        OrderCustome testOrderCustome = orderCustomeList.get(orderCustomeList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateOrderCustomeWithPatch() throws Exception {
        // Initialize the database
        orderCustomeRepository.saveAndFlush(orderCustome);

        int databaseSizeBeforeUpdate = orderCustomeRepository.findAll().size();

        // Update the orderCustome using partial update
        OrderCustome partialUpdatedOrderCustome = new OrderCustome();
        partialUpdatedOrderCustome.setId(orderCustome.getId());

        restOrderCustomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderCustome.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderCustome))
            )
            .andExpect(status().isOk());

        // Validate the OrderCustome in the database
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeUpdate);
        OrderCustome testOrderCustome = orderCustomeList.get(orderCustomeList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingOrderCustome() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomeRepository.findAll().size();
        orderCustome.setId(count.incrementAndGet());

        // Create the OrderCustome
        OrderCustomeDTO orderCustomeDTO = orderCustomeMapper.toDto(orderCustome);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderCustomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderCustomeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustome in the database
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderCustome() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomeRepository.findAll().size();
        orderCustome.setId(count.incrementAndGet());

        // Create the OrderCustome
        OrderCustomeDTO orderCustomeDTO = orderCustomeMapper.toDto(orderCustome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustome in the database
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderCustome() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomeRepository.findAll().size();
        orderCustome.setId(count.incrementAndGet());

        // Create the OrderCustome
        OrderCustomeDTO orderCustomeDTO = orderCustomeMapper.toDto(orderCustome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderCustome in the database
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderCustome() throws Exception {
        // Initialize the database
        orderCustomeRepository.saveAndFlush(orderCustome);

        int databaseSizeBeforeDelete = orderCustomeRepository.findAll().size();

        // Delete the orderCustome
        restOrderCustomeMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderCustome.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderCustome> orderCustomeList = orderCustomeRepository.findAll();
        assertThat(orderCustomeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
