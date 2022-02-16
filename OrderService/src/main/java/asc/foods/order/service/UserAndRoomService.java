package asc.foods.order.service;

import asc.foods.order.domain.UserAndRoom;
import asc.foods.order.repository.UserAndRoomRepository;
import asc.foods.order.service.dto.UserAndRoomDTO;
import asc.foods.order.service.mapper.UserAndRoomMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserAndRoom}.
 */
@Service
@Transactional
public class UserAndRoomService {

    private final Logger log = LoggerFactory.getLogger(UserAndRoomService.class);

    private final UserAndRoomRepository userAndRoomRepository;

    private final UserAndRoomMapper userAndRoomMapper;

    public UserAndRoomService(UserAndRoomRepository userAndRoomRepository, UserAndRoomMapper userAndRoomMapper) {
        this.userAndRoomRepository = userAndRoomRepository;
        this.userAndRoomMapper = userAndRoomMapper;
    }

    /**
     * Save a userAndRoom.
     *
     * @param userAndRoomDTO the entity to save.
     * @return the persisted entity.
     */
    public UserAndRoomDTO save(UserAndRoomDTO userAndRoomDTO) {
        log.debug("Request to save UserAndRoom : {}", userAndRoomDTO);
        UserAndRoom userAndRoom = userAndRoomMapper.toEntity(userAndRoomDTO);
        userAndRoom = userAndRoomRepository.save(userAndRoom);
        return userAndRoomMapper.toDto(userAndRoom);
    }

    /**
     * Partially update a userAndRoom.
     *
     * @param userAndRoomDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserAndRoomDTO> partialUpdate(UserAndRoomDTO userAndRoomDTO) {
        log.debug("Request to partially update UserAndRoom : {}", userAndRoomDTO);

        return userAndRoomRepository
            .findById(userAndRoomDTO.getId())
            .map(existingUserAndRoom -> {
                userAndRoomMapper.partialUpdate(existingUserAndRoom, userAndRoomDTO);

                return existingUserAndRoom;
            })
            .map(userAndRoomRepository::save)
            .map(userAndRoomMapper::toDto);
    }

    /**
     * Get all the userAndRooms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserAndRoomDTO> findAll() {
        log.debug("Request to get all UserAndRooms");
        return userAndRoomRepository.findAll().stream().map(userAndRoomMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userAndRoom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserAndRoomDTO> findOne(Long id) {
        log.debug("Request to get UserAndRoom : {}", id);
        return userAndRoomRepository.findById(id).map(userAndRoomMapper::toDto);
    }

    /**
     * Delete the userAndRoom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserAndRoom : {}", id);
        userAndRoomRepository.deleteById(id);
    }
}
