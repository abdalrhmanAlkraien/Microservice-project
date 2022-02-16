package asc.foods.store.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.store.IntegrationTest;
import asc.foods.store.domain.UserAddress;
import asc.foods.store.domain.enumeration.AddressType;
import asc.foods.store.repository.UserAddressRepository;
import asc.foods.store.service.dto.UserAddressDTO;
import asc.foods.store.service.mapper.UserAddressMapper;
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
 * Integration tests for the {@link UserAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAddressResourceIT {

    private static final String DEFAULT_LOCATION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final String DEFAULT_AREA = "AAAAAAAAAA";
    private static final String UPDATED_AREA = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final Long DEFAULT_FLAT_NO = 1L;
    private static final Long UPDATED_FLAT_NO = 2L;

    private static final String DEFAULT_BUILDING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUILDING_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VILLA_NO = "AAAAAAAAAA";
    private static final String UPDATED_VILLA_NO = "BBBBBBBBBB";

    private static final AddressType DEFAULT_ADDRESS_TYPE = AddressType.WORK;
    private static final AddressType UPDATED_ADDRESS_TYPE = AddressType.HOME;

    private static final String ENTITY_API_URL = "/api/user-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAddressMockMvc;

    private UserAddress userAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAddress createEntity(EntityManager em) {
        UserAddress userAddress = new UserAddress()
            .locationDescription(DEFAULT_LOCATION_DESCRIPTION)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .area(DEFAULT_AREA)
            .street(DEFAULT_STREET)
            .flatNo(DEFAULT_FLAT_NO)
            .buildingName(DEFAULT_BUILDING_NAME)
            .villaNo(DEFAULT_VILLA_NO)
            .addressType(DEFAULT_ADDRESS_TYPE);
        return userAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAddress createUpdatedEntity(EntityManager em) {
        UserAddress userAddress = new UserAddress()
            .locationDescription(UPDATED_LOCATION_DESCRIPTION)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .area(UPDATED_AREA)
            .street(UPDATED_STREET)
            .flatNo(UPDATED_FLAT_NO)
            .buildingName(UPDATED_BUILDING_NAME)
            .villaNo(UPDATED_VILLA_NO)
            .addressType(UPDATED_ADDRESS_TYPE);
        return userAddress;
    }

    @BeforeEach
    public void initTest() {
        userAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAddress() throws Exception {
        int databaseSizeBeforeCreate = userAddressRepository.findAll().size();
        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);
        restUserAddressMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeCreate + 1);
        UserAddress testUserAddress = userAddressList.get(userAddressList.size() - 1);
        assertThat(testUserAddress.getLocationDescription()).isEqualTo(DEFAULT_LOCATION_DESCRIPTION);
        assertThat(testUserAddress.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testUserAddress.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testUserAddress.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testUserAddress.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testUserAddress.getFlatNo()).isEqualTo(DEFAULT_FLAT_NO);
        assertThat(testUserAddress.getBuildingName()).isEqualTo(DEFAULT_BUILDING_NAME);
        assertThat(testUserAddress.getVillaNo()).isEqualTo(DEFAULT_VILLA_NO);
        assertThat(testUserAddress.getAddressType()).isEqualTo(DEFAULT_ADDRESS_TYPE);
    }

    @Test
    @Transactional
    void createUserAddressWithExistingId() throws Exception {
        // Create the UserAddress with an existing ID
        userAddress.setId(1L);
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        int databaseSizeBeforeCreate = userAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAddressMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserAddresses() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].locationDescription").value(hasItem(DEFAULT_LOCATION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].flatNo").value(hasItem(DEFAULT_FLAT_NO.intValue())))
            .andExpect(jsonPath("$.[*].buildingName").value(hasItem(DEFAULT_BUILDING_NAME)))
            .andExpect(jsonPath("$.[*].villaNo").value(hasItem(DEFAULT_VILLA_NO)))
            .andExpect(jsonPath("$.[*].addressType").value(hasItem(DEFAULT_ADDRESS_TYPE.toString())));
    }

    @Test
    @Transactional
    void getUserAddress() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get the userAddress
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, userAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAddress.getId().intValue()))
            .andExpect(jsonPath("$.locationDescription").value(DEFAULT_LOCATION_DESCRIPTION))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.flatNo").value(DEFAULT_FLAT_NO.intValue()))
            .andExpect(jsonPath("$.buildingName").value(DEFAULT_BUILDING_NAME))
            .andExpect(jsonPath("$.villaNo").value(DEFAULT_VILLA_NO))
            .andExpect(jsonPath("$.addressType").value(DEFAULT_ADDRESS_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserAddress() throws Exception {
        // Get the userAddress
        restUserAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserAddress() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();

        // Update the userAddress
        UserAddress updatedUserAddress = userAddressRepository.findById(userAddress.getId()).get();
        // Disconnect from session so that the updates on updatedUserAddress are not directly saved in db
        em.detach(updatedUserAddress);
        updatedUserAddress
            .locationDescription(UPDATED_LOCATION_DESCRIPTION)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .area(UPDATED_AREA)
            .street(UPDATED_STREET)
            .flatNo(UPDATED_FLAT_NO)
            .buildingName(UPDATED_BUILDING_NAME)
            .villaNo(UPDATED_VILLA_NO)
            .addressType(UPDATED_ADDRESS_TYPE);
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(updatedUserAddress);

        restUserAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAddressDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
        UserAddress testUserAddress = userAddressList.get(userAddressList.size() - 1);
        assertThat(testUserAddress.getLocationDescription()).isEqualTo(UPDATED_LOCATION_DESCRIPTION);
        assertThat(testUserAddress.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testUserAddress.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testUserAddress.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testUserAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testUserAddress.getFlatNo()).isEqualTo(UPDATED_FLAT_NO);
        assertThat(testUserAddress.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testUserAddress.getVillaNo()).isEqualTo(UPDATED_VILLA_NO);
        assertThat(testUserAddress.getAddressType()).isEqualTo(UPDATED_ADDRESS_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(count.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAddressDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(count.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(count.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAddressWithPatch() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();

        // Update the userAddress using partial update
        UserAddress partialUpdatedUserAddress = new UserAddress();
        partialUpdatedUserAddress.setId(userAddress.getId());

        partialUpdatedUserAddress
            .locationDescription(UPDATED_LOCATION_DESCRIPTION)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .area(UPDATED_AREA)
            .flatNo(UPDATED_FLAT_NO)
            .buildingName(UPDATED_BUILDING_NAME)
            .villaNo(UPDATED_VILLA_NO);

        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAddress.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAddress))
            )
            .andExpect(status().isOk());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
        UserAddress testUserAddress = userAddressList.get(userAddressList.size() - 1);
        assertThat(testUserAddress.getLocationDescription()).isEqualTo(UPDATED_LOCATION_DESCRIPTION);
        assertThat(testUserAddress.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testUserAddress.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testUserAddress.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testUserAddress.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testUserAddress.getFlatNo()).isEqualTo(UPDATED_FLAT_NO);
        assertThat(testUserAddress.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testUserAddress.getVillaNo()).isEqualTo(UPDATED_VILLA_NO);
        assertThat(testUserAddress.getAddressType()).isEqualTo(DEFAULT_ADDRESS_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateUserAddressWithPatch() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();

        // Update the userAddress using partial update
        UserAddress partialUpdatedUserAddress = new UserAddress();
        partialUpdatedUserAddress.setId(userAddress.getId());

        partialUpdatedUserAddress
            .locationDescription(UPDATED_LOCATION_DESCRIPTION)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .area(UPDATED_AREA)
            .street(UPDATED_STREET)
            .flatNo(UPDATED_FLAT_NO)
            .buildingName(UPDATED_BUILDING_NAME)
            .villaNo(UPDATED_VILLA_NO)
            .addressType(UPDATED_ADDRESS_TYPE);

        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAddress.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAddress))
            )
            .andExpect(status().isOk());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
        UserAddress testUserAddress = userAddressList.get(userAddressList.size() - 1);
        assertThat(testUserAddress.getLocationDescription()).isEqualTo(UPDATED_LOCATION_DESCRIPTION);
        assertThat(testUserAddress.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testUserAddress.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testUserAddress.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testUserAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testUserAddress.getFlatNo()).isEqualTo(UPDATED_FLAT_NO);
        assertThat(testUserAddress.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testUserAddress.getVillaNo()).isEqualTo(UPDATED_VILLA_NO);
        assertThat(testUserAddress.getAddressType()).isEqualTo(UPDATED_ADDRESS_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(count.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAddressDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(count.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(count.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAddress() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        int databaseSizeBeforeDelete = userAddressRepository.findAll().size();

        // Delete the userAddress
        restUserAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAddress.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
