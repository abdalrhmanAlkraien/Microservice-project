package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.ItemType;
import asc.foods.order.repository.ItemTypeRepository;
import asc.foods.order.service.dto.ItemTypeDTO;
import asc.foods.order.service.mapper.ItemTypeMapper;
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
 * Integration tests for the {@link ItemTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemTypeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DECRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DECRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATION_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATION_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INCREASE = false;
    private static final Boolean UPDATED_INCREASE = true;

    private static final String ENTITY_API_URL = "/api/item-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemTypeMapper itemTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemTypeMockMvc;

    private ItemType itemType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemType createEntity(EntityManager em) {
        ItemType itemType = new ItemType()
            .title(DEFAULT_TITLE)
            .decription(DEFAULT_DECRIPTION)
            .imageUrl(DEFAULT_IMAGE_URL)
            .creationDate(DEFAULT_CREATION_DATE)
            .creationBy(DEFAULT_CREATION_BY)
            .increase(DEFAULT_INCREASE);
        return itemType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemType createUpdatedEntity(EntityManager em) {
        ItemType itemType = new ItemType()
            .title(UPDATED_TITLE)
            .decription(UPDATED_DECRIPTION)
            .imageUrl(UPDATED_IMAGE_URL)
            .creationDate(UPDATED_CREATION_DATE)
            .creationBy(UPDATED_CREATION_BY)
            .increase(UPDATED_INCREASE);
        return itemType;
    }

    @BeforeEach
    public void initTest() {
        itemType = createEntity(em);
    }

    @Test
    @Transactional
    void createItemType() throws Exception {
        int databaseSizeBeforeCreate = itemTypeRepository.findAll().size();
        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);
        restItemTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ItemType testItemType = itemTypeList.get(itemTypeList.size() - 1);
        assertThat(testItemType.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testItemType.getDecription()).isEqualTo(DEFAULT_DECRIPTION);
        assertThat(testItemType.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testItemType.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testItemType.getCreationBy()).isEqualTo(DEFAULT_CREATION_BY);
        assertThat(testItemType.getIncrease()).isEqualTo(DEFAULT_INCREASE);
    }

    @Test
    @Transactional
    void createItemTypeWithExistingId() throws Exception {
        // Create the ItemType with an existing ID
        itemType.setId(1L);
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        int databaseSizeBeforeCreate = itemTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItemTypes() throws Exception {
        // Initialize the database
        itemTypeRepository.saveAndFlush(itemType);

        // Get all the itemTypeList
        restItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].decription").value(hasItem(DEFAULT_DECRIPTION)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].creationBy").value(hasItem(DEFAULT_CREATION_BY)))
            .andExpect(jsonPath("$.[*].increase").value(hasItem(DEFAULT_INCREASE.booleanValue())));
    }

    @Test
    @Transactional
    void getItemType() throws Exception {
        // Initialize the database
        itemTypeRepository.saveAndFlush(itemType);

        // Get the itemType
        restItemTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, itemType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemType.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.decription").value(DEFAULT_DECRIPTION))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.creationBy").value(DEFAULT_CREATION_BY))
            .andExpect(jsonPath("$.increase").value(DEFAULT_INCREASE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingItemType() throws Exception {
        // Get the itemType
        restItemTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemType() throws Exception {
        // Initialize the database
        itemTypeRepository.saveAndFlush(itemType);

        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();

        // Update the itemType
        ItemType updatedItemType = itemTypeRepository.findById(itemType.getId()).get();
        // Disconnect from session so that the updates on updatedItemType are not directly saved in db
        em.detach(updatedItemType);
        updatedItemType
            .title(UPDATED_TITLE)
            .decription(UPDATED_DECRIPTION)
            .imageUrl(UPDATED_IMAGE_URL)
            .creationDate(UPDATED_CREATION_DATE)
            .creationBy(UPDATED_CREATION_BY)
            .increase(UPDATED_INCREASE);
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(updatedItemType);

        restItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeUpdate);
        ItemType testItemType = itemTypeList.get(itemTypeList.size() - 1);
        assertThat(testItemType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testItemType.getDecription()).isEqualTo(UPDATED_DECRIPTION);
        assertThat(testItemType.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testItemType.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testItemType.getCreationBy()).isEqualTo(UPDATED_CREATION_BY);
        assertThat(testItemType.getIncrease()).isEqualTo(UPDATED_INCREASE);
    }

    @Test
    @Transactional
    void putNonExistingItemType() throws Exception {
        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();
        itemType.setId(count.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemType() throws Exception {
        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();
        itemType.setId(count.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemType() throws Exception {
        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();
        itemType.setId(count.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemTypeWithPatch() throws Exception {
        // Initialize the database
        itemTypeRepository.saveAndFlush(itemType);

        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();

        // Update the itemType using partial update
        ItemType partialUpdatedItemType = new ItemType();
        partialUpdatedItemType.setId(itemType.getId());

        partialUpdatedItemType
            .title(UPDATED_TITLE)
            .decription(UPDATED_DECRIPTION)
            .imageUrl(UPDATED_IMAGE_URL)
            .creationDate(UPDATED_CREATION_DATE);

        restItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemType))
            )
            .andExpect(status().isOk());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeUpdate);
        ItemType testItemType = itemTypeList.get(itemTypeList.size() - 1);
        assertThat(testItemType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testItemType.getDecription()).isEqualTo(UPDATED_DECRIPTION);
        assertThat(testItemType.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testItemType.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testItemType.getCreationBy()).isEqualTo(DEFAULT_CREATION_BY);
        assertThat(testItemType.getIncrease()).isEqualTo(DEFAULT_INCREASE);
    }

    @Test
    @Transactional
    void fullUpdateItemTypeWithPatch() throws Exception {
        // Initialize the database
        itemTypeRepository.saveAndFlush(itemType);

        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();

        // Update the itemType using partial update
        ItemType partialUpdatedItemType = new ItemType();
        partialUpdatedItemType.setId(itemType.getId());

        partialUpdatedItemType
            .title(UPDATED_TITLE)
            .decription(UPDATED_DECRIPTION)
            .imageUrl(UPDATED_IMAGE_URL)
            .creationDate(UPDATED_CREATION_DATE)
            .creationBy(UPDATED_CREATION_BY)
            .increase(UPDATED_INCREASE);

        restItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemType))
            )
            .andExpect(status().isOk());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeUpdate);
        ItemType testItemType = itemTypeList.get(itemTypeList.size() - 1);
        assertThat(testItemType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testItemType.getDecription()).isEqualTo(UPDATED_DECRIPTION);
        assertThat(testItemType.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testItemType.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testItemType.getCreationBy()).isEqualTo(UPDATED_CREATION_BY);
        assertThat(testItemType.getIncrease()).isEqualTo(UPDATED_INCREASE);
    }

    @Test
    @Transactional
    void patchNonExistingItemType() throws Exception {
        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();
        itemType.setId(count.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemTypeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemType() throws Exception {
        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();
        itemType.setId(count.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemType() throws Exception {
        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();
        itemType.setId(count.incrementAndGet());

        // Create the ItemType
        ItemTypeDTO itemTypeDTO = itemTypeMapper.toDto(itemType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemType in the database
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemType() throws Exception {
        // Initialize the database
        itemTypeRepository.saveAndFlush(itemType);

        int databaseSizeBeforeDelete = itemTypeRepository.findAll().size();

        // Delete the itemType
        restItemTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        assertThat(itemTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
