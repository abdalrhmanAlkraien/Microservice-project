package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.AppUser;
import asc.foods.order.domain.enumeration.Gender;
import asc.foods.order.domain.enumeration.UserStatus;
import asc.foods.order.domain.enumeration.UserType;
import asc.foods.order.repository.AppUserRepository;
import asc.foods.order.service.AppUserService;
import asc.foods.order.service.dto.AppUserDTO;
import asc.foods.order.service.mapper.AppUserMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AppUserResourceIT {

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_LANG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_LANG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final UserStatus DEFAULT_USER_STATUS = UserStatus.ONLINE;
    private static final UserStatus UPDATED_USER_STATUS = UserStatus.OFFLINE;

    private static final String DEFAULT_COVER_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_COVER_PHOTO = "BBBBBBBBBB";

    private static final UserType DEFAULT_USER_TYPE = UserType.REVIEWER;
    private static final UserType UPDATED_USER_TYPE = UserType.NORMAL;

    private static final String DEFAULT_DOB = "AAAAAAAAAA";
    private static final String UPDATED_DOB = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Boolean DEFAULT_IS_HIRING = false;
    private static final Boolean UPDATED_IS_HIRING = true;

    private static final Boolean DEFAULT_ENABLE_MESSAGES = false;
    private static final Boolean UPDATED_ENABLE_MESSAGES = true;

    private static final Boolean DEFAULT_ENABLE_NOTIFICATIONS = false;
    private static final Boolean UPDATED_ENABLE_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_ENABLE_OFFERS_NOTIFICATIONS = false;
    private static final Boolean UPDATED_ENABLE_OFFERS_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_IS_DARK = false;
    private static final Boolean UPDATED_IS_DARK = true;

    private static final String ENTITY_API_URL = "/api/app-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserRepository appUserRepositoryMock;

    @Autowired
    private AppUserMapper appUserMapper;

    @Mock
    private AppUserService appUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppUserMockMvc;

    private AppUser appUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createEntity(EntityManager em) {
        AppUser appUser = new AppUser()
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .login(DEFAULT_LOGIN)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .activated(DEFAULT_ACTIVATED)
            .langKey(DEFAULT_LANG_KEY)
            .imageUrl(DEFAULT_IMAGE_URL)
            .userStatus(DEFAULT_USER_STATUS)
            .coverPhoto(DEFAULT_COVER_PHOTO)
            .userType(DEFAULT_USER_TYPE)
            .dob(DEFAULT_DOB)
            .gender(DEFAULT_GENDER)
            .isHiring(DEFAULT_IS_HIRING)
            .enableMessages(DEFAULT_ENABLE_MESSAGES)
            .enableNotifications(DEFAULT_ENABLE_NOTIFICATIONS)
            .enableOffersNotifications(DEFAULT_ENABLE_OFFERS_NOTIFICATIONS)
            .isDark(DEFAULT_IS_DARK);
        return appUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createUpdatedEntity(EntityManager em) {
        AppUser appUser = new AppUser()
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .imageUrl(UPDATED_IMAGE_URL)
            .userStatus(UPDATED_USER_STATUS)
            .coverPhoto(UPDATED_COVER_PHOTO)
            .userType(UPDATED_USER_TYPE)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .isHiring(UPDATED_IS_HIRING)
            .enableMessages(UPDATED_ENABLE_MESSAGES)
            .enableNotifications(UPDATED_ENABLE_NOTIFICATIONS)
            .enableOffersNotifications(UPDATED_ENABLE_OFFERS_NOTIFICATIONS)
            .isDark(UPDATED_IS_DARK);
        return appUser;
    }

    @BeforeEach
    public void initTest() {
        appUser = createEntity(em);
    }

    @Test
    @Transactional
    void createAppUser() throws Exception {
        int databaseSizeBeforeCreate = appUserRepository.findAll().size();
        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);
        restAppUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate + 1);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testAppUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testAppUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAppUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAppUser.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testAppUser.getLangKey()).isEqualTo(DEFAULT_LANG_KEY);
        assertThat(testAppUser.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testAppUser.getUserStatus()).isEqualTo(DEFAULT_USER_STATUS);
        assertThat(testAppUser.getCoverPhoto()).isEqualTo(DEFAULT_COVER_PHOTO);
        assertThat(testAppUser.getUserType()).isEqualTo(DEFAULT_USER_TYPE);
        assertThat(testAppUser.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testAppUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAppUser.getIsHiring()).isEqualTo(DEFAULT_IS_HIRING);
        assertThat(testAppUser.getEnableMessages()).isEqualTo(DEFAULT_ENABLE_MESSAGES);
        assertThat(testAppUser.getEnableNotifications()).isEqualTo(DEFAULT_ENABLE_NOTIFICATIONS);
        assertThat(testAppUser.getEnableOffersNotifications()).isEqualTo(DEFAULT_ENABLE_OFFERS_NOTIFICATIONS);
        assertThat(testAppUser.getIsDark()).isEqualTo(DEFAULT_IS_DARK);
    }

    @Test
    @Transactional
    void createAppUserWithExistingId() throws Exception {
        // Create the AppUser with an existing ID
        appUser.setId("existing_id");
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        int databaseSizeBeforeCreate = appUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppUsers() throws Exception {
        // Initialize the database
        appUser.setId(UUID.randomUUID().toString());
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANG_KEY)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].userStatus").value(hasItem(DEFAULT_USER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].coverPhoto").value(hasItem(DEFAULT_COVER_PHOTO)))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].isHiring").value(hasItem(DEFAULT_IS_HIRING.booleanValue())))
            .andExpect(jsonPath("$.[*].enableMessages").value(hasItem(DEFAULT_ENABLE_MESSAGES.booleanValue())))
            .andExpect(jsonPath("$.[*].enableNotifications").value(hasItem(DEFAULT_ENABLE_NOTIFICATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].enableOffersNotifications").value(hasItem(DEFAULT_ENABLE_OFFERS_NOTIFICATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].isDark").value(hasItem(DEFAULT_IS_DARK.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(appUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAppUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(appUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(appUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAppUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(appUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAppUser() throws Exception {
        // Initialize the database
        appUser.setId(UUID.randomUUID().toString());
        appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL_ID, appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appUser.getId()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANG_KEY))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.userStatus").value(DEFAULT_USER_STATUS.toString()))
            .andExpect(jsonPath("$.coverPhoto").value(DEFAULT_COVER_PHOTO))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE.toString()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.isHiring").value(DEFAULT_IS_HIRING.booleanValue()))
            .andExpect(jsonPath("$.enableMessages").value(DEFAULT_ENABLE_MESSAGES.booleanValue()))
            .andExpect(jsonPath("$.enableNotifications").value(DEFAULT_ENABLE_NOTIFICATIONS.booleanValue()))
            .andExpect(jsonPath("$.enableOffersNotifications").value(DEFAULT_ENABLE_OFFERS_NOTIFICATIONS.booleanValue()))
            .andExpect(jsonPath("$.isDark").value(DEFAULT_IS_DARK.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAppUser() throws Exception {
        // Get the appUser
        restAppUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppUser() throws Exception {
        // Initialize the database
        appUser.setId(UUID.randomUUID().toString());
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser
        AppUser updatedAppUser = appUserRepository.findById(appUser.getId()).get();
        // Disconnect from session so that the updates on updatedAppUser are not directly saved in db
        em.detach(updatedAppUser);
        updatedAppUser
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .imageUrl(UPDATED_IMAGE_URL)
            .userStatus(UPDATED_USER_STATUS)
            .coverPhoto(UPDATED_COVER_PHOTO)
            .userType(UPDATED_USER_TYPE)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .isHiring(UPDATED_IS_HIRING)
            .enableMessages(UPDATED_ENABLE_MESSAGES)
            .enableNotifications(UPDATED_ENABLE_NOTIFICATIONS)
            .enableOffersNotifications(UPDATED_ENABLE_OFFERS_NOTIFICATIONS)
            .isDark(UPDATED_IS_DARK);
        AppUserDTO appUserDTO = appUserMapper.toDto(updatedAppUser);

        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testAppUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testAppUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAppUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAppUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testAppUser.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testAppUser.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testAppUser.getUserStatus()).isEqualTo(UPDATED_USER_STATUS);
        assertThat(testAppUser.getCoverPhoto()).isEqualTo(UPDATED_COVER_PHOTO);
        assertThat(testAppUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testAppUser.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testAppUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAppUser.getIsHiring()).isEqualTo(UPDATED_IS_HIRING);
        assertThat(testAppUser.getEnableMessages()).isEqualTo(UPDATED_ENABLE_MESSAGES);
        assertThat(testAppUser.getEnableNotifications()).isEqualTo(UPDATED_ENABLE_NOTIFICATIONS);
        assertThat(testAppUser.getEnableOffersNotifications()).isEqualTo(UPDATED_ENABLE_OFFERS_NOTIFICATIONS);
        assertThat(testAppUser.getIsDark()).isEqualTo(UPDATED_IS_DARK);
    }

    @Test
    @Transactional
    void putNonExistingAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        appUser.setId(UUID.randomUUID().toString());
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .login(UPDATED_LOGIN)
            .email(UPDATED_EMAIL)
            .activated(UPDATED_ACTIVATED)
            .userStatus(UPDATED_USER_STATUS)
            .coverPhoto(UPDATED_COVER_PHOTO)
            .userType(UPDATED_USER_TYPE)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .enableNotifications(UPDATED_ENABLE_NOTIFICATIONS);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testAppUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testAppUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAppUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAppUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testAppUser.getLangKey()).isEqualTo(DEFAULT_LANG_KEY);
        assertThat(testAppUser.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testAppUser.getUserStatus()).isEqualTo(UPDATED_USER_STATUS);
        assertThat(testAppUser.getCoverPhoto()).isEqualTo(UPDATED_COVER_PHOTO);
        assertThat(testAppUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testAppUser.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testAppUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAppUser.getIsHiring()).isEqualTo(DEFAULT_IS_HIRING);
        assertThat(testAppUser.getEnableMessages()).isEqualTo(DEFAULT_ENABLE_MESSAGES);
        assertThat(testAppUser.getEnableNotifications()).isEqualTo(UPDATED_ENABLE_NOTIFICATIONS);
        assertThat(testAppUser.getEnableOffersNotifications()).isEqualTo(DEFAULT_ENABLE_OFFERS_NOTIFICATIONS);
        assertThat(testAppUser.getIsDark()).isEqualTo(DEFAULT_IS_DARK);
    }

    @Test
    @Transactional
    void fullUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        appUser.setId(UUID.randomUUID().toString());
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .imageUrl(UPDATED_IMAGE_URL)
            .userStatus(UPDATED_USER_STATUS)
            .coverPhoto(UPDATED_COVER_PHOTO)
            .userType(UPDATED_USER_TYPE)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .isHiring(UPDATED_IS_HIRING)
            .enableMessages(UPDATED_ENABLE_MESSAGES)
            .enableNotifications(UPDATED_ENABLE_NOTIFICATIONS)
            .enableOffersNotifications(UPDATED_ENABLE_OFFERS_NOTIFICATIONS)
            .isDark(UPDATED_IS_DARK);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testAppUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testAppUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAppUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAppUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testAppUser.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testAppUser.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testAppUser.getUserStatus()).isEqualTo(UPDATED_USER_STATUS);
        assertThat(testAppUser.getCoverPhoto()).isEqualTo(UPDATED_COVER_PHOTO);
        assertThat(testAppUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testAppUser.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testAppUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAppUser.getIsHiring()).isEqualTo(UPDATED_IS_HIRING);
        assertThat(testAppUser.getEnableMessages()).isEqualTo(UPDATED_ENABLE_MESSAGES);
        assertThat(testAppUser.getEnableNotifications()).isEqualTo(UPDATED_ENABLE_NOTIFICATIONS);
        assertThat(testAppUser.getEnableOffersNotifications()).isEqualTo(UPDATED_ENABLE_OFFERS_NOTIFICATIONS);
        assertThat(testAppUser.getIsDark()).isEqualTo(UPDATED_IS_DARK);
    }

    @Test
    @Transactional
    void patchNonExistingAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appUserDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppUser() throws Exception {
        // Initialize the database
        appUser.setId(UUID.randomUUID().toString());
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeDelete = appUserRepository.findAll().size();

        // Delete the appUser
        restAppUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, appUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
