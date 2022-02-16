package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.Driver;
import asc.foods.order.domain.enumeration.DriverStatus;
import asc.foods.order.repository.DriverRepository;
import asc.foods.order.service.dto.DriverDTO;
import asc.foods.order.service.mapper.DriverMapper;
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
 * Integration tests for the {@link DriverResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DriverResourceIT {

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Integer DEFAULT_TOTAL_TRIPS = 1;
    private static final Integer UPDATED_TOTAL_TRIPS = 2;

    private static final Instant DEFAULT_AVERAGE_TRIP_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_AVERAGE_TRIP_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_AVERAGE_RATING = 1D;
    private static final Double UPDATED_AVERAGE_RATING = 2D;

    private static final Long DEFAULT_TOTAL_RATING = 1L;
    private static final Long UPDATED_TOTAL_RATING = 2L;

    private static final DriverStatus DEFAULT_STATUS = DriverStatus.AVAILABLE;
    private static final DriverStatus UPDATED_STATUS = DriverStatus.UNAVAILABLE;

    private static final String ENTITY_API_URL = "/api/drivers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDriverMockMvc;

    private Driver driver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Driver createEntity(EntityManager em) {
        Driver driver = new Driver()
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .totalTrips(DEFAULT_TOTAL_TRIPS)
            .averageTripTime(DEFAULT_AVERAGE_TRIP_TIME)
            .averageRating(DEFAULT_AVERAGE_RATING)
            .totalRating(DEFAULT_TOTAL_RATING)
            .status(DEFAULT_STATUS);
        return driver;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Driver createUpdatedEntity(EntityManager em) {
        Driver driver = new Driver()
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .totalTrips(UPDATED_TOTAL_TRIPS)
            .averageTripTime(UPDATED_AVERAGE_TRIP_TIME)
            .averageRating(UPDATED_AVERAGE_RATING)
            .totalRating(UPDATED_TOTAL_RATING)
            .status(UPDATED_STATUS);
        return driver;
    }

    @BeforeEach
    public void initTest() {
        driver = createEntity(em);
    }

    @Test
    @Transactional
    void createDriver() throws Exception {
        int databaseSizeBeforeCreate = driverRepository.findAll().size();
        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);
        restDriverMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeCreate + 1);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testDriver.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testDriver.getTotalTrips()).isEqualTo(DEFAULT_TOTAL_TRIPS);
        assertThat(testDriver.getAverageTripTime()).isEqualTo(DEFAULT_AVERAGE_TRIP_TIME);
        assertThat(testDriver.getAverageRating()).isEqualTo(DEFAULT_AVERAGE_RATING);
        assertThat(testDriver.getTotalRating()).isEqualTo(DEFAULT_TOTAL_RATING);
        assertThat(testDriver.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createDriverWithExistingId() throws Exception {
        // Create the Driver with an existing ID
        driver.setId(1L);
        DriverDTO driverDTO = driverMapper.toDto(driver);

        int databaseSizeBeforeCreate = driverRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDriverMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDrivers() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList
        restDriverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driver.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalTrips").value(hasItem(DEFAULT_TOTAL_TRIPS)))
            .andExpect(jsonPath("$.[*].averageTripTime").value(hasItem(DEFAULT_AVERAGE_TRIP_TIME.toString())))
            .andExpect(jsonPath("$.[*].averageRating").value(hasItem(DEFAULT_AVERAGE_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].totalRating").value(hasItem(DEFAULT_TOTAL_RATING.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getDriver() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get the driver
        restDriverMockMvc
            .perform(get(ENTITY_API_URL_ID, driver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(driver.getId().intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.totalTrips").value(DEFAULT_TOTAL_TRIPS))
            .andExpect(jsonPath("$.averageTripTime").value(DEFAULT_AVERAGE_TRIP_TIME.toString()))
            .andExpect(jsonPath("$.averageRating").value(DEFAULT_AVERAGE_RATING.doubleValue()))
            .andExpect(jsonPath("$.totalRating").value(DEFAULT_TOTAL_RATING.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDriver() throws Exception {
        // Get the driver
        restDriverMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDriver() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        int databaseSizeBeforeUpdate = driverRepository.findAll().size();

        // Update the driver
        Driver updatedDriver = driverRepository.findById(driver.getId()).get();
        // Disconnect from session so that the updates on updatedDriver are not directly saved in db
        em.detach(updatedDriver);
        updatedDriver
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .totalTrips(UPDATED_TOTAL_TRIPS)
            .averageTripTime(UPDATED_AVERAGE_TRIP_TIME)
            .averageRating(UPDATED_AVERAGE_RATING)
            .totalRating(UPDATED_TOTAL_RATING)
            .status(UPDATED_STATUS);
        DriverDTO driverDTO = driverMapper.toDto(updatedDriver);

        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driverDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testDriver.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testDriver.getTotalTrips()).isEqualTo(UPDATED_TOTAL_TRIPS);
        assertThat(testDriver.getAverageTripTime()).isEqualTo(UPDATED_AVERAGE_TRIP_TIME);
        assertThat(testDriver.getAverageRating()).isEqualTo(UPDATED_AVERAGE_RATING);
        assertThat(testDriver.getTotalRating()).isEqualTo(UPDATED_TOTAL_RATING);
        assertThat(testDriver.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driverDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDriverWithPatch() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        int databaseSizeBeforeUpdate = driverRepository.findAll().size();

        // Update the driver using partial update
        Driver partialUpdatedDriver = new Driver();
        partialUpdatedDriver.setId(driver.getId());

        partialUpdatedDriver
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .totalTrips(UPDATED_TOTAL_TRIPS)
            .averageTripTime(UPDATED_AVERAGE_TRIP_TIME)
            .averageRating(UPDATED_AVERAGE_RATING)
            .status(UPDATED_STATUS);

        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriver.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDriver))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testDriver.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testDriver.getTotalTrips()).isEqualTo(UPDATED_TOTAL_TRIPS);
        assertThat(testDriver.getAverageTripTime()).isEqualTo(UPDATED_AVERAGE_TRIP_TIME);
        assertThat(testDriver.getAverageRating()).isEqualTo(UPDATED_AVERAGE_RATING);
        assertThat(testDriver.getTotalRating()).isEqualTo(DEFAULT_TOTAL_RATING);
        assertThat(testDriver.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateDriverWithPatch() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        int databaseSizeBeforeUpdate = driverRepository.findAll().size();

        // Update the driver using partial update
        Driver partialUpdatedDriver = new Driver();
        partialUpdatedDriver.setId(driver.getId());

        partialUpdatedDriver
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .totalTrips(UPDATED_TOTAL_TRIPS)
            .averageTripTime(UPDATED_AVERAGE_TRIP_TIME)
            .averageRating(UPDATED_AVERAGE_RATING)
            .totalRating(UPDATED_TOTAL_RATING)
            .status(UPDATED_STATUS);

        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriver.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDriver))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testDriver.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testDriver.getTotalTrips()).isEqualTo(UPDATED_TOTAL_TRIPS);
        assertThat(testDriver.getAverageTripTime()).isEqualTo(UPDATED_AVERAGE_TRIP_TIME);
        assertThat(testDriver.getAverageRating()).isEqualTo(UPDATED_AVERAGE_RATING);
        assertThat(testDriver.getTotalRating()).isEqualTo(UPDATED_TOTAL_RATING);
        assertThat(testDriver.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, driverDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDriver() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        int databaseSizeBeforeDelete = driverRepository.findAll().size();

        // Delete the driver
        restDriverMockMvc
            .perform(delete(ENTITY_API_URL_ID, driver.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
