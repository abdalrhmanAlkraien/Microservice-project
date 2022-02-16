package asc.foods.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.user.IntegrationTest;
import asc.foods.user.domain.OrderStatusHistory;
import asc.foods.user.domain.enumeration.OrderStatus;
import asc.foods.user.repository.OrderStatusHistoryRepository;
import asc.foods.user.service.dto.OrderStatusHistoryDTO;
import asc.foods.user.service.mapper.OrderStatusHistoryMapper;
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
 * Integration tests for the {@link OrderStatusHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderStatusHistoryResourceIT {

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.PENDING;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PREPARING;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/order-status-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Autowired
    private OrderStatusHistoryMapper orderStatusHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderStatusHistoryMockMvc;

    private OrderStatusHistory orderStatusHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderStatusHistory createEntity(EntityManager em) {
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory()
            .orderId(DEFAULT_ORDER_ID)
            .status(DEFAULT_STATUS)
            .creationDate(DEFAULT_CREATION_DATE);
        return orderStatusHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderStatusHistory createUpdatedEntity(EntityManager em) {
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory()
            .orderId(UPDATED_ORDER_ID)
            .status(UPDATED_STATUS)
            .creationDate(UPDATED_CREATION_DATE);
        return orderStatusHistory;
    }

    @BeforeEach
    public void initTest() {
        orderStatusHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderStatusHistory() throws Exception {
        int databaseSizeBeforeCreate = orderStatusHistoryRepository.findAll().size();
        // Create the OrderStatusHistory
        OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryMapper.toDto(orderStatusHistory);
        restOrderStatusHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderStatusHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderStatusHistory in the database
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        OrderStatusHistory testOrderStatusHistory = orderStatusHistoryList.get(orderStatusHistoryList.size() - 1);
        assertThat(testOrderStatusHistory.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrderStatusHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrderStatusHistory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createOrderStatusHistoryWithExistingId() throws Exception {
        // Create the OrderStatusHistory with an existing ID
        orderStatusHistory.setId(1L);
        OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryMapper.toDto(orderStatusHistory);

        int databaseSizeBeforeCreate = orderStatusHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderStatusHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatusHistory in the database
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderStatusHistories() throws Exception {
        // Initialize the database
        orderStatusHistoryRepository.saveAndFlush(orderStatusHistory);

        // Get all the orderStatusHistoryList
        restOrderStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderStatusHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrderStatusHistory() throws Exception {
        // Initialize the database
        orderStatusHistoryRepository.saveAndFlush(orderStatusHistory);

        // Get the orderStatusHistory
        restOrderStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, orderStatusHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderStatusHistory.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrderStatusHistory() throws Exception {
        // Get the orderStatusHistory
        restOrderStatusHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderStatusHistory() throws Exception {
        // Initialize the database
        orderStatusHistoryRepository.saveAndFlush(orderStatusHistory);

        int databaseSizeBeforeUpdate = orderStatusHistoryRepository.findAll().size();

        // Update the orderStatusHistory
        OrderStatusHistory updatedOrderStatusHistory = orderStatusHistoryRepository.findById(orderStatusHistory.getId()).get();
        // Disconnect from session so that the updates on updatedOrderStatusHistory are not directly saved in db
        em.detach(updatedOrderStatusHistory);
        updatedOrderStatusHistory.orderId(UPDATED_ORDER_ID).status(UPDATED_STATUS).creationDate(UPDATED_CREATION_DATE);
        OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryMapper.toDto(updatedOrderStatusHistory);

        restOrderStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderStatusHistoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderStatusHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderStatusHistory in the database
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
        OrderStatusHistory testOrderStatusHistory = orderStatusHistoryList.get(orderStatusHistoryList.size() - 1);
        assertThat(testOrderStatusHistory.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderStatusHistory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderStatusHistory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrderStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusHistoryRepository.findAll().size();
        orderStatusHistory.setId(count.incrementAndGet());

        // Create the OrderStatusHistory
        OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryMapper.toDto(orderStatusHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderStatusHistoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatusHistory in the database
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusHistoryRepository.findAll().size();
        orderStatusHistory.setId(count.incrementAndGet());

        // Create the OrderStatusHistory
        OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryMapper.toDto(orderStatusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatusHistory in the database
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusHistoryRepository.findAll().size();
        orderStatusHistory.setId(count.incrementAndGet());

        // Create the OrderStatusHistory
        OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryMapper.toDto(orderStatusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderStatusHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderStatusHistory in the database
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderStatusHistoryWithPatch() throws Exception {
        // Initialize the database
        orderStatusHistoryRepository.saveAndFlush(orderStatusHistory);

        int databaseSizeBeforeUpdate = orderStatusHistoryRepository.findAll().size();

        // Update the orderStatusHistory using partial update
        OrderStatusHistory partialUpdatedOrderStatusHistory = new OrderStatusHistory();
        partialUpdatedOrderStatusHistory.setId(orderStatusHistory.getId());

        restOrderStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderStatusHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderStatusHistory))
            )
            .andExpect(status().isOk());

        // Validate the OrderStatusHistory in the database
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
        OrderStatusHistory testOrderStatusHistory = orderStatusHistoryList.get(orderStatusHistoryList.size() - 1);
        assertThat(testOrderStatusHistory.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrderStatusHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrderStatusHistory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrderStatusHistoryWithPatch() throws Exception {
        // Initialize the database
        orderStatusHistoryRepository.saveAndFlush(orderStatusHistory);

        int databaseSizeBeforeUpdate = orderStatusHistoryRepository.findAll().size();

        // Update the orderStatusHistory using partial update
        OrderStatusHistory partialUpdatedOrderStatusHistory = new OrderStatusHistory();
        partialUpdatedOrderStatusHistory.setId(orderStatusHistory.getId());

        partialUpdatedOrderStatusHistory.orderId(UPDATED_ORDER_ID).status(UPDATED_STATUS).creationDate(UPDATED_CREATION_DATE);

        restOrderStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderStatusHistory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderStatusHistory))
            )
            .andExpect(status().isOk());

        // Validate the OrderStatusHistory in the database
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
        OrderStatusHistory testOrderStatusHistory = orderStatusHistoryList.get(orderStatusHistoryList.size() - 1);
        assertThat(testOrderStatusHistory.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderStatusHistory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderStatusHistory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusHistoryRepository.findAll().size();
        orderStatusHistory.setId(count.incrementAndGet());

        // Create the OrderStatusHistory
        OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryMapper.toDto(orderStatusHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderStatusHistoryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatusHistory in the database
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusHistoryRepository.findAll().size();
        orderStatusHistory.setId(count.incrementAndGet());

        // Create the OrderStatusHistory
        OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryMapper.toDto(orderStatusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatusHistory in the database
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusHistoryRepository.findAll().size();
        orderStatusHistory.setId(count.incrementAndGet());

        // Create the OrderStatusHistory
        OrderStatusHistoryDTO orderStatusHistoryDTO = orderStatusHistoryMapper.toDto(orderStatusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderStatusHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderStatusHistory in the database
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderStatusHistory() throws Exception {
        // Initialize the database
        orderStatusHistoryRepository.saveAndFlush(orderStatusHistory);

        int databaseSizeBeforeDelete = orderStatusHistoryRepository.findAll().size();

        // Delete the orderStatusHistory
        restOrderStatusHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderStatusHistory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderStatusHistory> orderStatusHistoryList = orderStatusHistoryRepository.findAll();
        assertThat(orderStatusHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
