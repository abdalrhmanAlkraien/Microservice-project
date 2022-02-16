package asc.foods.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.user.IntegrationTest;
import asc.foods.user.domain.AscStore;
import asc.foods.user.domain.enumeration.PriceRange;
import asc.foods.user.repository.AscStoreRepository;
import asc.foods.user.service.AscStoreService;
import asc.foods.user.service.dto.AscStoreDTO;
import asc.foods.user.service.mapper.AscStoreMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link AscStoreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AscStoreResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AD_VIDEO_URL = "AAAAAAAAAA";
    private static final String UPDATED_AD_VIDEO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_STORE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_STORE_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMAGE_URL = "BBBBBBBBBB";

    private static final Double DEFAULT_AVERAGE_RATING = 1D;
    private static final Double UPDATED_AVERAGE_RATING = 2D;

    private static final Long DEFAULT_TOTAL_RATING = 1L;
    private static final Long UPDATED_TOTAL_RATING = 2L;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HAS_FAVORITE = false;
    private static final Boolean UPDATED_HAS_FAVORITE = true;

    private static final Long DEFAULT_MIN_ORDER = 1L;
    private static final Long UPDATED_MIN_ORDER = 2L;

    private static final Double DEFAULT_DELIVERY = 1D;
    private static final Double UPDATED_DELIVERY = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final PriceRange DEFAULT_PRICE_RANGE = PriceRange.NONE;
    private static final PriceRange UPDATED_PRICE_RANGE = PriceRange.CHEAP;

    private static final String ENTITY_API_URL = "/api/asc-stores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AscStoreRepository ascStoreRepository;

    @Mock
    private AscStoreRepository ascStoreRepositoryMock;

    @Autowired
    private AscStoreMapper ascStoreMapper;

    @Mock
    private AscStoreService ascStoreServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAscStoreMockMvc;

    private AscStore ascStore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AscStore createEntity(EntityManager em) {
        AscStore ascStore = new AscStore()
            .name(DEFAULT_NAME)
            .adVideoUrl(DEFAULT_AD_VIDEO_URL)
            .storeImageUrl(DEFAULT_STORE_IMAGE_URL)
            .coverImageUrl(DEFAULT_COVER_IMAGE_URL)
            .averageRating(DEFAULT_AVERAGE_RATING)
            .totalRating(DEFAULT_TOTAL_RATING)
            .creationDate(DEFAULT_CREATION_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .hasFavorite(DEFAULT_HAS_FAVORITE)
            .minOrder(DEFAULT_MIN_ORDER)
            .delivery(DEFAULT_DELIVERY)
            .description(DEFAULT_DESCRIPTION)
            .priceRange(DEFAULT_PRICE_RANGE);
        return ascStore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AscStore createUpdatedEntity(EntityManager em) {
        AscStore ascStore = new AscStore()
            .name(UPDATED_NAME)
            .adVideoUrl(UPDATED_AD_VIDEO_URL)
            .storeImageUrl(UPDATED_STORE_IMAGE_URL)
            .coverImageUrl(UPDATED_COVER_IMAGE_URL)
            .averageRating(UPDATED_AVERAGE_RATING)
            .totalRating(UPDATED_TOTAL_RATING)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .hasFavorite(UPDATED_HAS_FAVORITE)
            .minOrder(UPDATED_MIN_ORDER)
            .delivery(UPDATED_DELIVERY)
            .description(UPDATED_DESCRIPTION)
            .priceRange(UPDATED_PRICE_RANGE);
        return ascStore;
    }

    @BeforeEach
    public void initTest() {
        ascStore = createEntity(em);
    }

    @Test
    @Transactional
    void createAscStore() throws Exception {
        int databaseSizeBeforeCreate = ascStoreRepository.findAll().size();
        // Create the AscStore
        AscStoreDTO ascStoreDTO = ascStoreMapper.toDto(ascStore);
        restAscStoreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascStoreDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AscStore in the database
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeCreate + 1);
        AscStore testAscStore = ascStoreList.get(ascStoreList.size() - 1);
        assertThat(testAscStore.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAscStore.getAdVideoUrl()).isEqualTo(DEFAULT_AD_VIDEO_URL);
        assertThat(testAscStore.getStoreImageUrl()).isEqualTo(DEFAULT_STORE_IMAGE_URL);
        assertThat(testAscStore.getCoverImageUrl()).isEqualTo(DEFAULT_COVER_IMAGE_URL);
        assertThat(testAscStore.getAverageRating()).isEqualTo(DEFAULT_AVERAGE_RATING);
        assertThat(testAscStore.getTotalRating()).isEqualTo(DEFAULT_TOTAL_RATING);
        assertThat(testAscStore.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAscStore.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAscStore.getHasFavorite()).isEqualTo(DEFAULT_HAS_FAVORITE);
        assertThat(testAscStore.getMinOrder()).isEqualTo(DEFAULT_MIN_ORDER);
        assertThat(testAscStore.getDelivery()).isEqualTo(DEFAULT_DELIVERY);
        assertThat(testAscStore.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAscStore.getPriceRange()).isEqualTo(DEFAULT_PRICE_RANGE);
    }

    @Test
    @Transactional
    void createAscStoreWithExistingId() throws Exception {
        // Create the AscStore with an existing ID
        ascStore.setId(1L);
        AscStoreDTO ascStoreDTO = ascStoreMapper.toDto(ascStore);

        int databaseSizeBeforeCreate = ascStoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAscStoreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AscStore in the database
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAscStores() throws Exception {
        // Initialize the database
        ascStoreRepository.saveAndFlush(ascStore);

        // Get all the ascStoreList
        restAscStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ascStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].adVideoUrl").value(hasItem(DEFAULT_AD_VIDEO_URL)))
            .andExpect(jsonPath("$.[*].storeImageUrl").value(hasItem(DEFAULT_STORE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].coverImageUrl").value(hasItem(DEFAULT_COVER_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].averageRating").value(hasItem(DEFAULT_AVERAGE_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].totalRating").value(hasItem(DEFAULT_TOTAL_RATING.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].hasFavorite").value(hasItem(DEFAULT_HAS_FAVORITE.booleanValue())))
            .andExpect(jsonPath("$.[*].minOrder").value(hasItem(DEFAULT_MIN_ORDER.intValue())))
            .andExpect(jsonPath("$.[*].delivery").value(hasItem(DEFAULT_DELIVERY.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priceRange").value(hasItem(DEFAULT_PRICE_RANGE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAscStoresWithEagerRelationshipsIsEnabled() throws Exception {
        when(ascStoreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAscStoreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ascStoreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAscStoresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ascStoreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAscStoreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ascStoreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAscStore() throws Exception {
        // Initialize the database
        ascStoreRepository.saveAndFlush(ascStore);

        // Get the ascStore
        restAscStoreMockMvc
            .perform(get(ENTITY_API_URL_ID, ascStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ascStore.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.adVideoUrl").value(DEFAULT_AD_VIDEO_URL))
            .andExpect(jsonPath("$.storeImageUrl").value(DEFAULT_STORE_IMAGE_URL))
            .andExpect(jsonPath("$.coverImageUrl").value(DEFAULT_COVER_IMAGE_URL))
            .andExpect(jsonPath("$.averageRating").value(DEFAULT_AVERAGE_RATING.doubleValue()))
            .andExpect(jsonPath("$.totalRating").value(DEFAULT_TOTAL_RATING.intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.hasFavorite").value(DEFAULT_HAS_FAVORITE.booleanValue()))
            .andExpect(jsonPath("$.minOrder").value(DEFAULT_MIN_ORDER.intValue()))
            .andExpect(jsonPath("$.delivery").value(DEFAULT_DELIVERY.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.priceRange").value(DEFAULT_PRICE_RANGE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAscStore() throws Exception {
        // Get the ascStore
        restAscStoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAscStore() throws Exception {
        // Initialize the database
        ascStoreRepository.saveAndFlush(ascStore);

        int databaseSizeBeforeUpdate = ascStoreRepository.findAll().size();

        // Update the ascStore
        AscStore updatedAscStore = ascStoreRepository.findById(ascStore.getId()).get();
        // Disconnect from session so that the updates on updatedAscStore are not directly saved in db
        em.detach(updatedAscStore);
        updatedAscStore
            .name(UPDATED_NAME)
            .adVideoUrl(UPDATED_AD_VIDEO_URL)
            .storeImageUrl(UPDATED_STORE_IMAGE_URL)
            .coverImageUrl(UPDATED_COVER_IMAGE_URL)
            .averageRating(UPDATED_AVERAGE_RATING)
            .totalRating(UPDATED_TOTAL_RATING)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .hasFavorite(UPDATED_HAS_FAVORITE)
            .minOrder(UPDATED_MIN_ORDER)
            .delivery(UPDATED_DELIVERY)
            .description(UPDATED_DESCRIPTION)
            .priceRange(UPDATED_PRICE_RANGE);
        AscStoreDTO ascStoreDTO = ascStoreMapper.toDto(updatedAscStore);

        restAscStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ascStoreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascStoreDTO))
            )
            .andExpect(status().isOk());

        // Validate the AscStore in the database
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeUpdate);
        AscStore testAscStore = ascStoreList.get(ascStoreList.size() - 1);
        assertThat(testAscStore.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAscStore.getAdVideoUrl()).isEqualTo(UPDATED_AD_VIDEO_URL);
        assertThat(testAscStore.getStoreImageUrl()).isEqualTo(UPDATED_STORE_IMAGE_URL);
        assertThat(testAscStore.getCoverImageUrl()).isEqualTo(UPDATED_COVER_IMAGE_URL);
        assertThat(testAscStore.getAverageRating()).isEqualTo(UPDATED_AVERAGE_RATING);
        assertThat(testAscStore.getTotalRating()).isEqualTo(UPDATED_TOTAL_RATING);
        assertThat(testAscStore.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAscStore.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAscStore.getHasFavorite()).isEqualTo(UPDATED_HAS_FAVORITE);
        assertThat(testAscStore.getMinOrder()).isEqualTo(UPDATED_MIN_ORDER);
        assertThat(testAscStore.getDelivery()).isEqualTo(UPDATED_DELIVERY);
        assertThat(testAscStore.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAscStore.getPriceRange()).isEqualTo(UPDATED_PRICE_RANGE);
    }

    @Test
    @Transactional
    void putNonExistingAscStore() throws Exception {
        int databaseSizeBeforeUpdate = ascStoreRepository.findAll().size();
        ascStore.setId(count.incrementAndGet());

        // Create the AscStore
        AscStoreDTO ascStoreDTO = ascStoreMapper.toDto(ascStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAscStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ascStoreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AscStore in the database
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAscStore() throws Exception {
        int databaseSizeBeforeUpdate = ascStoreRepository.findAll().size();
        ascStore.setId(count.incrementAndGet());

        // Create the AscStore
        AscStoreDTO ascStoreDTO = ascStoreMapper.toDto(ascStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAscStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AscStore in the database
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAscStore() throws Exception {
        int databaseSizeBeforeUpdate = ascStoreRepository.findAll().size();
        ascStore.setId(count.incrementAndGet());

        // Create the AscStore
        AscStoreDTO ascStoreDTO = ascStoreMapper.toDto(ascStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAscStoreMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ascStoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AscStore in the database
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAscStoreWithPatch() throws Exception {
        // Initialize the database
        ascStoreRepository.saveAndFlush(ascStore);

        int databaseSizeBeforeUpdate = ascStoreRepository.findAll().size();

        // Update the ascStore using partial update
        AscStore partialUpdatedAscStore = new AscStore();
        partialUpdatedAscStore.setId(ascStore.getId());

        partialUpdatedAscStore
            .name(UPDATED_NAME)
            .adVideoUrl(UPDATED_AD_VIDEO_URL)
            .storeImageUrl(UPDATED_STORE_IMAGE_URL)
            .coverImageUrl(UPDATED_COVER_IMAGE_URL)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .hasFavorite(UPDATED_HAS_FAVORITE)
            .minOrder(UPDATED_MIN_ORDER);

        restAscStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAscStore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAscStore))
            )
            .andExpect(status().isOk());

        // Validate the AscStore in the database
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeUpdate);
        AscStore testAscStore = ascStoreList.get(ascStoreList.size() - 1);
        assertThat(testAscStore.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAscStore.getAdVideoUrl()).isEqualTo(UPDATED_AD_VIDEO_URL);
        assertThat(testAscStore.getStoreImageUrl()).isEqualTo(UPDATED_STORE_IMAGE_URL);
        assertThat(testAscStore.getCoverImageUrl()).isEqualTo(UPDATED_COVER_IMAGE_URL);
        assertThat(testAscStore.getAverageRating()).isEqualTo(DEFAULT_AVERAGE_RATING);
        assertThat(testAscStore.getTotalRating()).isEqualTo(DEFAULT_TOTAL_RATING);
        assertThat(testAscStore.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAscStore.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAscStore.getHasFavorite()).isEqualTo(UPDATED_HAS_FAVORITE);
        assertThat(testAscStore.getMinOrder()).isEqualTo(UPDATED_MIN_ORDER);
        assertThat(testAscStore.getDelivery()).isEqualTo(DEFAULT_DELIVERY);
        assertThat(testAscStore.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAscStore.getPriceRange()).isEqualTo(DEFAULT_PRICE_RANGE);
    }

    @Test
    @Transactional
    void fullUpdateAscStoreWithPatch() throws Exception {
        // Initialize the database
        ascStoreRepository.saveAndFlush(ascStore);

        int databaseSizeBeforeUpdate = ascStoreRepository.findAll().size();

        // Update the ascStore using partial update
        AscStore partialUpdatedAscStore = new AscStore();
        partialUpdatedAscStore.setId(ascStore.getId());

        partialUpdatedAscStore
            .name(UPDATED_NAME)
            .adVideoUrl(UPDATED_AD_VIDEO_URL)
            .storeImageUrl(UPDATED_STORE_IMAGE_URL)
            .coverImageUrl(UPDATED_COVER_IMAGE_URL)
            .averageRating(UPDATED_AVERAGE_RATING)
            .totalRating(UPDATED_TOTAL_RATING)
            .creationDate(UPDATED_CREATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .hasFavorite(UPDATED_HAS_FAVORITE)
            .minOrder(UPDATED_MIN_ORDER)
            .delivery(UPDATED_DELIVERY)
            .description(UPDATED_DESCRIPTION)
            .priceRange(UPDATED_PRICE_RANGE);

        restAscStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAscStore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAscStore))
            )
            .andExpect(status().isOk());

        // Validate the AscStore in the database
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeUpdate);
        AscStore testAscStore = ascStoreList.get(ascStoreList.size() - 1);
        assertThat(testAscStore.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAscStore.getAdVideoUrl()).isEqualTo(UPDATED_AD_VIDEO_URL);
        assertThat(testAscStore.getStoreImageUrl()).isEqualTo(UPDATED_STORE_IMAGE_URL);
        assertThat(testAscStore.getCoverImageUrl()).isEqualTo(UPDATED_COVER_IMAGE_URL);
        assertThat(testAscStore.getAverageRating()).isEqualTo(UPDATED_AVERAGE_RATING);
        assertThat(testAscStore.getTotalRating()).isEqualTo(UPDATED_TOTAL_RATING);
        assertThat(testAscStore.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAscStore.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAscStore.getHasFavorite()).isEqualTo(UPDATED_HAS_FAVORITE);
        assertThat(testAscStore.getMinOrder()).isEqualTo(UPDATED_MIN_ORDER);
        assertThat(testAscStore.getDelivery()).isEqualTo(UPDATED_DELIVERY);
        assertThat(testAscStore.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAscStore.getPriceRange()).isEqualTo(UPDATED_PRICE_RANGE);
    }

    @Test
    @Transactional
    void patchNonExistingAscStore() throws Exception {
        int databaseSizeBeforeUpdate = ascStoreRepository.findAll().size();
        ascStore.setId(count.incrementAndGet());

        // Create the AscStore
        AscStoreDTO ascStoreDTO = ascStoreMapper.toDto(ascStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAscStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ascStoreDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ascStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AscStore in the database
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAscStore() throws Exception {
        int databaseSizeBeforeUpdate = ascStoreRepository.findAll().size();
        ascStore.setId(count.incrementAndGet());

        // Create the AscStore
        AscStoreDTO ascStoreDTO = ascStoreMapper.toDto(ascStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAscStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ascStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AscStore in the database
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAscStore() throws Exception {
        int databaseSizeBeforeUpdate = ascStoreRepository.findAll().size();
        ascStore.setId(count.incrementAndGet());

        // Create the AscStore
        AscStoreDTO ascStoreDTO = ascStoreMapper.toDto(ascStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAscStoreMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ascStoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AscStore in the database
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAscStore() throws Exception {
        // Initialize the database
        ascStoreRepository.saveAndFlush(ascStore);

        int databaseSizeBeforeDelete = ascStoreRepository.findAll().size();

        // Delete the ascStore
        restAscStoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, ascStore.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AscStore> ascStoreList = ascStoreRepository.findAll();
        assertThat(ascStoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
