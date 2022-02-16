package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.OrderProduct;
import asc.foods.order.repository.OrderProductRepository;
import asc.foods.order.service.dto.OrderProductDTO;
import asc.foods.order.service.mapper.OrderProductMapper;
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
 * Integration tests for the {@link OrderProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderProductResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_OPTIONS_DESC = "AAAAAAAAAA";
    private static final String UPDATED_OPTIONS_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIAL_REQUEST = "AAAAAAAAAA";
    private static final String UPDATED_SPECIAL_REQUEST = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/order-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderProductMockMvc;

    private OrderProduct orderProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderProduct createEntity(EntityManager em) {
        OrderProduct orderProduct = new OrderProduct()
            .quantity(DEFAULT_QUANTITY)
            .optionsDesc(DEFAULT_OPTIONS_DESC)
            .specialRequest(DEFAULT_SPECIAL_REQUEST)
            .price(DEFAULT_PRICE)
            .discount(DEFAULT_DISCOUNT)
            .creationDate(DEFAULT_CREATION_DATE);
        return orderProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderProduct createUpdatedEntity(EntityManager em) {
        OrderProduct orderProduct = new OrderProduct()
            .quantity(UPDATED_QUANTITY)
            .optionsDesc(UPDATED_OPTIONS_DESC)
            .specialRequest(UPDATED_SPECIAL_REQUEST)
            .price(UPDATED_PRICE)
            .discount(UPDATED_DISCOUNT)
            .creationDate(UPDATED_CREATION_DATE);
        return orderProduct;
    }

    @BeforeEach
    public void initTest() {
        orderProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderProduct() throws Exception {
        int databaseSizeBeforeCreate = orderProductRepository.findAll().size();
        // Create the OrderProduct
        OrderProductDTO orderProductDTO = orderProductMapper.toDto(orderProduct);
        restOrderProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderProductDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderProduct in the database
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeCreate + 1);
        OrderProduct testOrderProduct = orderProductList.get(orderProductList.size() - 1);
        assertThat(testOrderProduct.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrderProduct.getOptionsDesc()).isEqualTo(DEFAULT_OPTIONS_DESC);
        assertThat(testOrderProduct.getSpecialRequest()).isEqualTo(DEFAULT_SPECIAL_REQUEST);
        assertThat(testOrderProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrderProduct.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testOrderProduct.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createOrderProductWithExistingId() throws Exception {
        // Create the OrderProduct with an existing ID
        orderProduct.setId(1L);
        OrderProductDTO orderProductDTO = orderProductMapper.toDto(orderProduct);

        int databaseSizeBeforeCreate = orderProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderProduct in the database
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderProducts() throws Exception {
        // Initialize the database
        orderProductRepository.saveAndFlush(orderProduct);

        // Get all the orderProductList
        restOrderProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].optionsDesc").value(hasItem(DEFAULT_OPTIONS_DESC)))
            .andExpect(jsonPath("$.[*].specialRequest").value(hasItem(DEFAULT_SPECIAL_REQUEST)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrderProduct() throws Exception {
        // Initialize the database
        orderProductRepository.saveAndFlush(orderProduct);

        // Get the orderProduct
        restOrderProductMockMvc
            .perform(get(ENTITY_API_URL_ID, orderProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderProduct.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.optionsDesc").value(DEFAULT_OPTIONS_DESC))
            .andExpect(jsonPath("$.specialRequest").value(DEFAULT_SPECIAL_REQUEST))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrderProduct() throws Exception {
        // Get the orderProduct
        restOrderProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderProduct() throws Exception {
        // Initialize the database
        orderProductRepository.saveAndFlush(orderProduct);

        int databaseSizeBeforeUpdate = orderProductRepository.findAll().size();

        // Update the orderProduct
        OrderProduct updatedOrderProduct = orderProductRepository.findById(orderProduct.getId()).get();
        // Disconnect from session so that the updates on updatedOrderProduct are not directly saved in db
        em.detach(updatedOrderProduct);
        updatedOrderProduct
            .quantity(UPDATED_QUANTITY)
            .optionsDesc(UPDATED_OPTIONS_DESC)
            .specialRequest(UPDATED_SPECIAL_REQUEST)
            .price(UPDATED_PRICE)
            .discount(UPDATED_DISCOUNT)
            .creationDate(UPDATED_CREATION_DATE);
        OrderProductDTO orderProductDTO = orderProductMapper.toDto(updatedOrderProduct);

        restOrderProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderProductDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderProductDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderProduct in the database
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeUpdate);
        OrderProduct testOrderProduct = orderProductList.get(orderProductList.size() - 1);
        assertThat(testOrderProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderProduct.getOptionsDesc()).isEqualTo(UPDATED_OPTIONS_DESC);
        assertThat(testOrderProduct.getSpecialRequest()).isEqualTo(UPDATED_SPECIAL_REQUEST);
        assertThat(testOrderProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrderProduct.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testOrderProduct.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrderProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderProductRepository.findAll().size();
        orderProduct.setId(count.incrementAndGet());

        // Create the OrderProduct
        OrderProductDTO orderProductDTO = orderProductMapper.toDto(orderProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderProductDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderProduct in the database
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderProductRepository.findAll().size();
        orderProduct.setId(count.incrementAndGet());

        // Create the OrderProduct
        OrderProductDTO orderProductDTO = orderProductMapper.toDto(orderProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderProduct in the database
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderProductRepository.findAll().size();
        orderProduct.setId(count.incrementAndGet());

        // Create the OrderProduct
        OrderProductDTO orderProductDTO = orderProductMapper.toDto(orderProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderProductMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderProduct in the database
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderProductWithPatch() throws Exception {
        // Initialize the database
        orderProductRepository.saveAndFlush(orderProduct);

        int databaseSizeBeforeUpdate = orderProductRepository.findAll().size();

        // Update the orderProduct using partial update
        OrderProduct partialUpdatedOrderProduct = new OrderProduct();
        partialUpdatedOrderProduct.setId(orderProduct.getId());

        partialUpdatedOrderProduct.optionsDesc(UPDATED_OPTIONS_DESC).specialRequest(UPDATED_SPECIAL_REQUEST);

        restOrderProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderProduct.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderProduct))
            )
            .andExpect(status().isOk());

        // Validate the OrderProduct in the database
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeUpdate);
        OrderProduct testOrderProduct = orderProductList.get(orderProductList.size() - 1);
        assertThat(testOrderProduct.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrderProduct.getOptionsDesc()).isEqualTo(UPDATED_OPTIONS_DESC);
        assertThat(testOrderProduct.getSpecialRequest()).isEqualTo(UPDATED_SPECIAL_REQUEST);
        assertThat(testOrderProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrderProduct.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testOrderProduct.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrderProductWithPatch() throws Exception {
        // Initialize the database
        orderProductRepository.saveAndFlush(orderProduct);

        int databaseSizeBeforeUpdate = orderProductRepository.findAll().size();

        // Update the orderProduct using partial update
        OrderProduct partialUpdatedOrderProduct = new OrderProduct();
        partialUpdatedOrderProduct.setId(orderProduct.getId());

        partialUpdatedOrderProduct
            .quantity(UPDATED_QUANTITY)
            .optionsDesc(UPDATED_OPTIONS_DESC)
            .specialRequest(UPDATED_SPECIAL_REQUEST)
            .price(UPDATED_PRICE)
            .discount(UPDATED_DISCOUNT)
            .creationDate(UPDATED_CREATION_DATE);

        restOrderProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderProduct.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderProduct))
            )
            .andExpect(status().isOk());

        // Validate the OrderProduct in the database
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeUpdate);
        OrderProduct testOrderProduct = orderProductList.get(orderProductList.size() - 1);
        assertThat(testOrderProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderProduct.getOptionsDesc()).isEqualTo(UPDATED_OPTIONS_DESC);
        assertThat(testOrderProduct.getSpecialRequest()).isEqualTo(UPDATED_SPECIAL_REQUEST);
        assertThat(testOrderProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrderProduct.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testOrderProduct.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderProductRepository.findAll().size();
        orderProduct.setId(count.incrementAndGet());

        // Create the OrderProduct
        OrderProductDTO orderProductDTO = orderProductMapper.toDto(orderProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderProductDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderProduct in the database
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderProductRepository.findAll().size();
        orderProduct.setId(count.incrementAndGet());

        // Create the OrderProduct
        OrderProductDTO orderProductDTO = orderProductMapper.toDto(orderProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderProduct in the database
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderProductRepository.findAll().size();
        orderProduct.setId(count.incrementAndGet());

        // Create the OrderProduct
        OrderProductDTO orderProductDTO = orderProductMapper.toDto(orderProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderProduct in the database
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderProduct() throws Exception {
        // Initialize the database
        orderProductRepository.saveAndFlush(orderProduct);

        int databaseSizeBeforeDelete = orderProductRepository.findAll().size();

        // Delete the orderProduct
        restOrderProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderProduct.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        assertThat(orderProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
