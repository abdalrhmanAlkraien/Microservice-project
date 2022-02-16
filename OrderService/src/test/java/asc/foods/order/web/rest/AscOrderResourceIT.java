package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.AscOrder;
import asc.foods.order.domain.enumeration.OrderStatus;
import asc.foods.order.domain.enumeration.Payment;
import asc.foods.order.repository.AscOrderRepository;
import asc.foods.order.service.dto.AscOrderDTO;
import asc.foods.order.service.mapper.AscOrderMapper;
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
 * Integration tests for the {@link AscOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AscOrderResourceIT {

    private static final String DEFAULT_SPECIAL_REQUEST = "AAAAAAAAAA";
    private static final String UPDATED_SPECIAL_REQUEST = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_DELIVERY_PRICE = 1D;
    private static final Double UPDATED_DELIVERY_PRICE = 2D;

    private static final Double DEFAULT_TAX_PRICE = 1D;
    private static final Double UPDATED_TAX_PRICE = 2D;

    private static final Payment DEFAULT_PAYMENT_METHOD = Payment.CASH;
    private static final Payment UPDATED_PAYMENT_METHOD = Payment.CREDITCARD;

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.PENDING;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PREPARING;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final Instant DEFAULT_ESTIMATION_DELIVERY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ESTIMATION_DELIVERY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ACTUAL_DELIVERY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTUAL_DELIVERY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/asc-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AscOrderRepository ascOrderRepository;

    @Autowired
    private AscOrderMapper ascOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAscOrderMockMvc;

    private AscOrder ascOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AscOrder createEntity(EntityManager em) {
        AscOrder ascOrder = new AscOrder()
            .specialRequest(DEFAULT_SPECIAL_REQUEST)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .rating(DEFAULT_RATING)
            .price(DEFAULT_PRICE)
            .deliveryPrice(DEFAULT_DELIVERY_PRICE)
            .taxPrice(DEFAULT_TAX_PRICE)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .status(DEFAULT_STATUS)
            .discount(DEFAULT_DISCOUNT)
            .estimationDeliveryTime(DEFAULT_ESTIMATION_DELIVERY_TIME)
            .actualDeliveryTime(DEFAULT_ACTUAL_DELIVERY_TIME)
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY);
        return ascOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AscOrder createUpdatedEntity(EntityManager em) {
        AscOrder ascOrder = new AscOrder()
            .specialRequest(UPDATED_SPECIAL_REQUEST)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .rating(UPDATED_RATING)
            .price(UPDATED_PRICE)
            .deliveryPrice(UPDATED_DELIVERY_PRICE)
            .taxPrice(UPDATED_TAX_PRICE)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .status(UPDATED_STATUS)
            .discount(UPDATED_DISCOUNT)
            .estimationDeliveryTime(UPDATED_ESTIMATION_DELIVERY_TIME)
            .actualDeliveryTime(UPDATED_ACTUAL_DELIVERY_TIME)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);
        return ascOrder;
    }

    @BeforeEach
    public void initTest() {
        ascOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createAscOrder() throws Exception {
        int databaseSizeBeforeCreate = ascOrderRepository.findAll().size();
        // Create the AscOrder
        AscOrderDTO ascOrderDTO = ascOrderMapper.toDto(ascOrder);
        restAscOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AscOrder in the database
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeCreate + 1);
        AscOrder testAscOrder = ascOrderList.get(ascOrderList.size() - 1);
        assertThat(testAscOrder.getSpecialRequest()).isEqualTo(DEFAULT_SPECIAL_REQUEST);
        assertThat(testAscOrder.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testAscOrder.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testAscOrder.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testAscOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testAscOrder.getDeliveryPrice()).isEqualTo(DEFAULT_DELIVERY_PRICE);
        assertThat(testAscOrder.getTaxPrice()).isEqualTo(DEFAULT_TAX_PRICE);
        assertThat(testAscOrder.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testAscOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAscOrder.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testAscOrder.getEstimationDeliveryTime()).isEqualTo(DEFAULT_ESTIMATION_DELIVERY_TIME);
        assertThat(testAscOrder.getActualDeliveryTime()).isEqualTo(DEFAULT_ACTUAL_DELIVERY_TIME);
        assertThat(testAscOrder.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAscOrder.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void createAscOrderWithExistingId() throws Exception {
        // Create the AscOrder with an existing ID
        ascOrder.setId(1L);
        AscOrderDTO ascOrderDTO = ascOrderMapper.toDto(ascOrder);

        int databaseSizeBeforeCreate = ascOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAscOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AscOrder in the database
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAscOrders() throws Exception {
        // Initialize the database
        ascOrderRepository.saveAndFlush(ascOrder);

        // Get all the ascOrderList
        restAscOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ascOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].specialRequest").value(hasItem(DEFAULT_SPECIAL_REQUEST)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].deliveryPrice").value(hasItem(DEFAULT_DELIVERY_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxPrice").value(hasItem(DEFAULT_TAX_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].estimationDeliveryTime").value(hasItem(DEFAULT_ESTIMATION_DELIVERY_TIME.toString())))
            .andExpect(jsonPath("$.[*].actualDeliveryTime").value(hasItem(DEFAULT_ACTUAL_DELIVERY_TIME.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getAscOrder() throws Exception {
        // Initialize the database
        ascOrderRepository.saveAndFlush(ascOrder);

        // Get the ascOrder
        restAscOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, ascOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ascOrder.getId().intValue()))
            .andExpect(jsonPath("$.specialRequest").value(DEFAULT_SPECIAL_REQUEST))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.deliveryPrice").value(DEFAULT_DELIVERY_PRICE.doubleValue()))
            .andExpect(jsonPath("$.taxPrice").value(DEFAULT_TAX_PRICE.doubleValue()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.estimationDeliveryTime").value(DEFAULT_ESTIMATION_DELIVERY_TIME.toString()))
            .andExpect(jsonPath("$.actualDeliveryTime").value(DEFAULT_ACTUAL_DELIVERY_TIME.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingAscOrder() throws Exception {
        // Get the ascOrder
        restAscOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAscOrder() throws Exception {
        // Initialize the database
        ascOrderRepository.saveAndFlush(ascOrder);

        int databaseSizeBeforeUpdate = ascOrderRepository.findAll().size();

        // Update the ascOrder
        AscOrder updatedAscOrder = ascOrderRepository.findById(ascOrder.getId()).get();
        // Disconnect from session so that the updates on updatedAscOrder are not directly saved in db
        em.detach(updatedAscOrder);
        updatedAscOrder
            .specialRequest(UPDATED_SPECIAL_REQUEST)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .rating(UPDATED_RATING)
            .price(UPDATED_PRICE)
            .deliveryPrice(UPDATED_DELIVERY_PRICE)
            .taxPrice(UPDATED_TAX_PRICE)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .status(UPDATED_STATUS)
            .discount(UPDATED_DISCOUNT)
            .estimationDeliveryTime(UPDATED_ESTIMATION_DELIVERY_TIME)
            .actualDeliveryTime(UPDATED_ACTUAL_DELIVERY_TIME)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);
        AscOrderDTO ascOrderDTO = ascOrderMapper.toDto(updatedAscOrder);

        restAscOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ascOrderDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the AscOrder in the database
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeUpdate);
        AscOrder testAscOrder = ascOrderList.get(ascOrderList.size() - 1);
        assertThat(testAscOrder.getSpecialRequest()).isEqualTo(UPDATED_SPECIAL_REQUEST);
        assertThat(testAscOrder.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAscOrder.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testAscOrder.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testAscOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testAscOrder.getDeliveryPrice()).isEqualTo(UPDATED_DELIVERY_PRICE);
        assertThat(testAscOrder.getTaxPrice()).isEqualTo(UPDATED_TAX_PRICE);
        assertThat(testAscOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testAscOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAscOrder.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testAscOrder.getEstimationDeliveryTime()).isEqualTo(UPDATED_ESTIMATION_DELIVERY_TIME);
        assertThat(testAscOrder.getActualDeliveryTime()).isEqualTo(UPDATED_ACTUAL_DELIVERY_TIME);
        assertThat(testAscOrder.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAscOrder.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingAscOrder() throws Exception {
        int databaseSizeBeforeUpdate = ascOrderRepository.findAll().size();
        ascOrder.setId(count.incrementAndGet());

        // Create the AscOrder
        AscOrderDTO ascOrderDTO = ascOrderMapper.toDto(ascOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAscOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ascOrderDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AscOrder in the database
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAscOrder() throws Exception {
        int databaseSizeBeforeUpdate = ascOrderRepository.findAll().size();
        ascOrder.setId(count.incrementAndGet());

        // Create the AscOrder
        AscOrderDTO ascOrderDTO = ascOrderMapper.toDto(ascOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAscOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AscOrder in the database
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAscOrder() throws Exception {
        int databaseSizeBeforeUpdate = ascOrderRepository.findAll().size();
        ascOrder.setId(count.incrementAndGet());

        // Create the AscOrder
        AscOrderDTO ascOrderDTO = ascOrderMapper.toDto(ascOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAscOrderMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AscOrder in the database
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAscOrderWithPatch() throws Exception {
        // Initialize the database
        ascOrderRepository.saveAndFlush(ascOrder);

        int databaseSizeBeforeUpdate = ascOrderRepository.findAll().size();

        // Update the ascOrder using partial update
        AscOrder partialUpdatedAscOrder = new AscOrder();
        partialUpdatedAscOrder.setId(ascOrder.getId());

        partialUpdatedAscOrder
            .specialRequest(UPDATED_SPECIAL_REQUEST)
            .latitude(UPDATED_LATITUDE)
            .price(UPDATED_PRICE)
            .deliveryPrice(UPDATED_DELIVERY_PRICE)
            .taxPrice(UPDATED_TAX_PRICE)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .status(UPDATED_STATUS)
            .discount(UPDATED_DISCOUNT)
            .estimationDeliveryTime(UPDATED_ESTIMATION_DELIVERY_TIME)
            .actualDeliveryTime(UPDATED_ACTUAL_DELIVERY_TIME);

        restAscOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAscOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAscOrder))
            )
            .andExpect(status().isOk());

        // Validate the AscOrder in the database
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeUpdate);
        AscOrder testAscOrder = ascOrderList.get(ascOrderList.size() - 1);
        assertThat(testAscOrder.getSpecialRequest()).isEqualTo(UPDATED_SPECIAL_REQUEST);
        assertThat(testAscOrder.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAscOrder.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testAscOrder.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testAscOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testAscOrder.getDeliveryPrice()).isEqualTo(UPDATED_DELIVERY_PRICE);
        assertThat(testAscOrder.getTaxPrice()).isEqualTo(UPDATED_TAX_PRICE);
        assertThat(testAscOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testAscOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAscOrder.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testAscOrder.getEstimationDeliveryTime()).isEqualTo(UPDATED_ESTIMATION_DELIVERY_TIME);
        assertThat(testAscOrder.getActualDeliveryTime()).isEqualTo(UPDATED_ACTUAL_DELIVERY_TIME);
        assertThat(testAscOrder.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAscOrder.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateAscOrderWithPatch() throws Exception {
        // Initialize the database
        ascOrderRepository.saveAndFlush(ascOrder);

        int databaseSizeBeforeUpdate = ascOrderRepository.findAll().size();

        // Update the ascOrder using partial update
        AscOrder partialUpdatedAscOrder = new AscOrder();
        partialUpdatedAscOrder.setId(ascOrder.getId());

        partialUpdatedAscOrder
            .specialRequest(UPDATED_SPECIAL_REQUEST)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .rating(UPDATED_RATING)
            .price(UPDATED_PRICE)
            .deliveryPrice(UPDATED_DELIVERY_PRICE)
            .taxPrice(UPDATED_TAX_PRICE)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .status(UPDATED_STATUS)
            .discount(UPDATED_DISCOUNT)
            .estimationDeliveryTime(UPDATED_ESTIMATION_DELIVERY_TIME)
            .actualDeliveryTime(UPDATED_ACTUAL_DELIVERY_TIME)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restAscOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAscOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAscOrder))
            )
            .andExpect(status().isOk());

        // Validate the AscOrder in the database
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeUpdate);
        AscOrder testAscOrder = ascOrderList.get(ascOrderList.size() - 1);
        assertThat(testAscOrder.getSpecialRequest()).isEqualTo(UPDATED_SPECIAL_REQUEST);
        assertThat(testAscOrder.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAscOrder.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testAscOrder.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testAscOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testAscOrder.getDeliveryPrice()).isEqualTo(UPDATED_DELIVERY_PRICE);
        assertThat(testAscOrder.getTaxPrice()).isEqualTo(UPDATED_TAX_PRICE);
        assertThat(testAscOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testAscOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAscOrder.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testAscOrder.getEstimationDeliveryTime()).isEqualTo(UPDATED_ESTIMATION_DELIVERY_TIME);
        assertThat(testAscOrder.getActualDeliveryTime()).isEqualTo(UPDATED_ACTUAL_DELIVERY_TIME);
        assertThat(testAscOrder.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAscOrder.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingAscOrder() throws Exception {
        int databaseSizeBeforeUpdate = ascOrderRepository.findAll().size();
        ascOrder.setId(count.incrementAndGet());

        // Create the AscOrder
        AscOrderDTO ascOrderDTO = ascOrderMapper.toDto(ascOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAscOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ascOrderDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ascOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AscOrder in the database
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAscOrder() throws Exception {
        int databaseSizeBeforeUpdate = ascOrderRepository.findAll().size();
        ascOrder.setId(count.incrementAndGet());

        // Create the AscOrder
        AscOrderDTO ascOrderDTO = ascOrderMapper.toDto(ascOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAscOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ascOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AscOrder in the database
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAscOrder() throws Exception {
        int databaseSizeBeforeUpdate = ascOrderRepository.findAll().size();
        ascOrder.setId(count.incrementAndGet());

        // Create the AscOrder
        AscOrderDTO ascOrderDTO = ascOrderMapper.toDto(ascOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAscOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ascOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AscOrder in the database
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAscOrder() throws Exception {
        // Initialize the database
        ascOrderRepository.saveAndFlush(ascOrder);

        int databaseSizeBeforeDelete = ascOrderRepository.findAll().size();

        // Delete the ascOrder
        restAscOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, ascOrder.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AscOrder> ascOrderList = ascOrderRepository.findAll();
        assertThat(ascOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
