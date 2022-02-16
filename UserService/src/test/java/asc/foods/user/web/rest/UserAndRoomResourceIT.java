package asc.foods.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import asc.foods.user.IntegrationTest;
import asc.foods.user.domain.UserAndRoom;
import asc.foods.user.repository.UserAndRoomRepository;
import asc.foods.user.service.dto.UserAndRoomDTO;
import asc.foods.user.service.mapper.UserAndRoomMapper;
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
 * Integration tests for the {@link UserAndRoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAndRoomResourceIT {

    private static final String ENTITY_API_URL = "/api/user-and-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAndRoomRepository userAndRoomRepository;

    @Autowired
    private UserAndRoomMapper userAndRoomMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAndRoomMockMvc;

    private UserAndRoom userAndRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAndRoom createEntity(EntityManager em) {
        UserAndRoom userAndRoom = new UserAndRoom();
        return userAndRoom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAndRoom createUpdatedEntity(EntityManager em) {
        UserAndRoom userAndRoom = new UserAndRoom();
        return userAndRoom;
    }

    @BeforeEach
    public void initTest() {
        userAndRoom = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAndRoom() throws Exception {
        int databaseSizeBeforeCreate = userAndRoomRepository.findAll().size();
        // Create the UserAndRoom
        UserAndRoomDTO userAndRoomDTO = userAndRoomMapper.toDto(userAndRoom);
        restUserAndRoomMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAndRoomDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserAndRoom in the database
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeCreate + 1);
        UserAndRoom testUserAndRoom = userAndRoomList.get(userAndRoomList.size() - 1);
    }

    @Test
    @Transactional
    void createUserAndRoomWithExistingId() throws Exception {
        // Create the UserAndRoom with an existing ID
        userAndRoom.setId(1L);
        UserAndRoomDTO userAndRoomDTO = userAndRoomMapper.toDto(userAndRoom);

        int databaseSizeBeforeCreate = userAndRoomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAndRoomMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAndRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAndRoom in the database
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserAndRooms() throws Exception {
        // Initialize the database
        userAndRoomRepository.saveAndFlush(userAndRoom);

        // Get all the userAndRoomList
        restUserAndRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAndRoom.getId().intValue())));
    }

    @Test
    @Transactional
    void getUserAndRoom() throws Exception {
        // Initialize the database
        userAndRoomRepository.saveAndFlush(userAndRoom);

        // Get the userAndRoom
        restUserAndRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, userAndRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAndRoom.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserAndRoom() throws Exception {
        // Get the userAndRoom
        restUserAndRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserAndRoom() throws Exception {
        // Initialize the database
        userAndRoomRepository.saveAndFlush(userAndRoom);

        int databaseSizeBeforeUpdate = userAndRoomRepository.findAll().size();

        // Update the userAndRoom
        UserAndRoom updatedUserAndRoom = userAndRoomRepository.findById(userAndRoom.getId()).get();
        // Disconnect from session so that the updates on updatedUserAndRoom are not directly saved in db
        em.detach(updatedUserAndRoom);
        UserAndRoomDTO userAndRoomDTO = userAndRoomMapper.toDto(updatedUserAndRoom);

        restUserAndRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAndRoomDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAndRoomDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAndRoom in the database
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeUpdate);
        UserAndRoom testUserAndRoom = userAndRoomList.get(userAndRoomList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingUserAndRoom() throws Exception {
        int databaseSizeBeforeUpdate = userAndRoomRepository.findAll().size();
        userAndRoom.setId(count.incrementAndGet());

        // Create the UserAndRoom
        UserAndRoomDTO userAndRoomDTO = userAndRoomMapper.toDto(userAndRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAndRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAndRoomDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAndRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAndRoom in the database
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAndRoom() throws Exception {
        int databaseSizeBeforeUpdate = userAndRoomRepository.findAll().size();
        userAndRoom.setId(count.incrementAndGet());

        // Create the UserAndRoom
        UserAndRoomDTO userAndRoomDTO = userAndRoomMapper.toDto(userAndRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAndRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAndRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAndRoom in the database
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAndRoom() throws Exception {
        int databaseSizeBeforeUpdate = userAndRoomRepository.findAll().size();
        userAndRoom.setId(count.incrementAndGet());

        // Create the UserAndRoom
        UserAndRoomDTO userAndRoomDTO = userAndRoomMapper.toDto(userAndRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAndRoomMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAndRoomDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAndRoom in the database
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAndRoomWithPatch() throws Exception {
        // Initialize the database
        userAndRoomRepository.saveAndFlush(userAndRoom);

        int databaseSizeBeforeUpdate = userAndRoomRepository.findAll().size();

        // Update the userAndRoom using partial update
        UserAndRoom partialUpdatedUserAndRoom = new UserAndRoom();
        partialUpdatedUserAndRoom.setId(userAndRoom.getId());

        restUserAndRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAndRoom.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAndRoom))
            )
            .andExpect(status().isOk());

        // Validate the UserAndRoom in the database
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeUpdate);
        UserAndRoom testUserAndRoom = userAndRoomList.get(userAndRoomList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateUserAndRoomWithPatch() throws Exception {
        // Initialize the database
        userAndRoomRepository.saveAndFlush(userAndRoom);

        int databaseSizeBeforeUpdate = userAndRoomRepository.findAll().size();

        // Update the userAndRoom using partial update
        UserAndRoom partialUpdatedUserAndRoom = new UserAndRoom();
        partialUpdatedUserAndRoom.setId(userAndRoom.getId());

        restUserAndRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAndRoom.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAndRoom))
            )
            .andExpect(status().isOk());

        // Validate the UserAndRoom in the database
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeUpdate);
        UserAndRoom testUserAndRoom = userAndRoomList.get(userAndRoomList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingUserAndRoom() throws Exception {
        int databaseSizeBeforeUpdate = userAndRoomRepository.findAll().size();
        userAndRoom.setId(count.incrementAndGet());

        // Create the UserAndRoom
        UserAndRoomDTO userAndRoomDTO = userAndRoomMapper.toDto(userAndRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAndRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAndRoomDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAndRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAndRoom in the database
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAndRoom() throws Exception {
        int databaseSizeBeforeUpdate = userAndRoomRepository.findAll().size();
        userAndRoom.setId(count.incrementAndGet());

        // Create the UserAndRoom
        UserAndRoomDTO userAndRoomDTO = userAndRoomMapper.toDto(userAndRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAndRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAndRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAndRoom in the database
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAndRoom() throws Exception {
        int databaseSizeBeforeUpdate = userAndRoomRepository.findAll().size();
        userAndRoom.setId(count.incrementAndGet());

        // Create the UserAndRoom
        UserAndRoomDTO userAndRoomDTO = userAndRoomMapper.toDto(userAndRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAndRoomMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAndRoomDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAndRoom in the database
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAndRoom() throws Exception {
        // Initialize the database
        userAndRoomRepository.saveAndFlush(userAndRoom);

        int databaseSizeBeforeDelete = userAndRoomRepository.findAll().size();

        // Delete the userAndRoom
        restUserAndRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAndRoom.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAndRoom> userAndRoomList = userAndRoomRepository.findAll();
        assertThat(userAndRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
