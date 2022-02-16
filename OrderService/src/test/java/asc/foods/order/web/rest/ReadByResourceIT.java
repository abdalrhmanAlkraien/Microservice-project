package asc.foods.order.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.order.IntegrationTest;
import asc.foods.order.domain.ReadBy;
import asc.foods.order.repository.ReadByRepository;
import asc.foods.order.service.dto.ReadByDTO;
import asc.foods.order.service.mapper.ReadByMapper;
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
 * Integration tests for the {@link ReadByResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReadByResourceIT {

    private static final String ENTITY_API_URL = "/api/read-bies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReadByRepository readByRepository;

    @Autowired
    private ReadByMapper readByMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReadByMockMvc;

    private ReadBy readBy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReadBy createEntity(EntityManager em) {
        ReadBy readBy = new ReadBy();
        return readBy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReadBy createUpdatedEntity(EntityManager em) {
        ReadBy readBy = new ReadBy();
        return readBy;
    }

    @BeforeEach
    public void initTest() {
        readBy = createEntity(em);
    }

    @Test
    @Transactional
    void createReadBy() throws Exception {
        int databaseSizeBeforeCreate = readByRepository.findAll().size();
        // Create the ReadBy
        ReadByDTO readByDTO = readByMapper.toDto(readBy);
        restReadByMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(readByDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ReadBy in the database
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeCreate + 1);
        ReadBy testReadBy = readByList.get(readByList.size() - 1);
    }

    @Test
    @Transactional
    void createReadByWithExistingId() throws Exception {
        // Create the ReadBy with an existing ID
        readBy.setId(1L);
        ReadByDTO readByDTO = readByMapper.toDto(readBy);

        int databaseSizeBeforeCreate = readByRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReadByMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(readByDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReadBy in the database
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReadBies() throws Exception {
        // Initialize the database
        readByRepository.saveAndFlush(readBy);

        // Get all the readByList
        restReadByMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(readBy.getId().intValue())));
    }

    @Test
    @Transactional
    void getReadBy() throws Exception {
        // Initialize the database
        readByRepository.saveAndFlush(readBy);

        // Get the readBy
        restReadByMockMvc
            .perform(get(ENTITY_API_URL_ID, readBy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(readBy.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingReadBy() throws Exception {
        // Get the readBy
        restReadByMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReadBy() throws Exception {
        // Initialize the database
        readByRepository.saveAndFlush(readBy);

        int databaseSizeBeforeUpdate = readByRepository.findAll().size();

        // Update the readBy
        ReadBy updatedReadBy = readByRepository.findById(readBy.getId()).get();
        // Disconnect from session so that the updates on updatedReadBy are not directly saved in db
        em.detach(updatedReadBy);
        ReadByDTO readByDTO = readByMapper.toDto(updatedReadBy);

        restReadByMockMvc
            .perform(
                put(ENTITY_API_URL_ID, readByDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(readByDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReadBy in the database
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeUpdate);
        ReadBy testReadBy = readByList.get(readByList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingReadBy() throws Exception {
        int databaseSizeBeforeUpdate = readByRepository.findAll().size();
        readBy.setId(count.incrementAndGet());

        // Create the ReadBy
        ReadByDTO readByDTO = readByMapper.toDto(readBy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReadByMockMvc
            .perform(
                put(ENTITY_API_URL_ID, readByDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(readByDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReadBy in the database
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReadBy() throws Exception {
        int databaseSizeBeforeUpdate = readByRepository.findAll().size();
        readBy.setId(count.incrementAndGet());

        // Create the ReadBy
        ReadByDTO readByDTO = readByMapper.toDto(readBy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReadByMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(readByDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReadBy in the database
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReadBy() throws Exception {
        int databaseSizeBeforeUpdate = readByRepository.findAll().size();
        readBy.setId(count.incrementAndGet());

        // Create the ReadBy
        ReadByDTO readByDTO = readByMapper.toDto(readBy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReadByMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(readByDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReadBy in the database
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReadByWithPatch() throws Exception {
        // Initialize the database
        readByRepository.saveAndFlush(readBy);

        int databaseSizeBeforeUpdate = readByRepository.findAll().size();

        // Update the readBy using partial update
        ReadBy partialUpdatedReadBy = new ReadBy();
        partialUpdatedReadBy.setId(readBy.getId());

        restReadByMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReadBy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReadBy))
            )
            .andExpect(status().isOk());

        // Validate the ReadBy in the database
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeUpdate);
        ReadBy testReadBy = readByList.get(readByList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateReadByWithPatch() throws Exception {
        // Initialize the database
        readByRepository.saveAndFlush(readBy);

        int databaseSizeBeforeUpdate = readByRepository.findAll().size();

        // Update the readBy using partial update
        ReadBy partialUpdatedReadBy = new ReadBy();
        partialUpdatedReadBy.setId(readBy.getId());

        restReadByMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReadBy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReadBy))
            )
            .andExpect(status().isOk());

        // Validate the ReadBy in the database
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeUpdate);
        ReadBy testReadBy = readByList.get(readByList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingReadBy() throws Exception {
        int databaseSizeBeforeUpdate = readByRepository.findAll().size();
        readBy.setId(count.incrementAndGet());

        // Create the ReadBy
        ReadByDTO readByDTO = readByMapper.toDto(readBy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReadByMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, readByDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(readByDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReadBy in the database
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReadBy() throws Exception {
        int databaseSizeBeforeUpdate = readByRepository.findAll().size();
        readBy.setId(count.incrementAndGet());

        // Create the ReadBy
        ReadByDTO readByDTO = readByMapper.toDto(readBy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReadByMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(readByDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReadBy in the database
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReadBy() throws Exception {
        int databaseSizeBeforeUpdate = readByRepository.findAll().size();
        readBy.setId(count.incrementAndGet());

        // Create the ReadBy
        ReadByDTO readByDTO = readByMapper.toDto(readBy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReadByMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(readByDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReadBy in the database
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReadBy() throws Exception {
        // Initialize the database
        readByRepository.saveAndFlush(readBy);

        int databaseSizeBeforeDelete = readByRepository.findAll().size();

        // Delete the readBy
        restReadByMockMvc
            .perform(delete(ENTITY_API_URL_ID, readBy.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReadBy> readByList = readByRepository.findAll();
        assertThat(readByList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
