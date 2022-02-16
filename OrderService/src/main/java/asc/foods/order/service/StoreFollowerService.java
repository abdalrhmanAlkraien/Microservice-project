package asc.foods.order.service;

import asc.foods.order.domain.StoreFollower;
import asc.foods.order.repository.StoreFollowerRepository;
import asc.foods.order.service.dto.StoreFollowerDTO;
import asc.foods.order.service.mapper.StoreFollowerMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StoreFollower}.
 */
@Service
@Transactional
public class StoreFollowerService {

    private final Logger log = LoggerFactory.getLogger(StoreFollowerService.class);

    private final StoreFollowerRepository storeFollowerRepository;

    private final StoreFollowerMapper storeFollowerMapper;

    public StoreFollowerService(StoreFollowerRepository storeFollowerRepository, StoreFollowerMapper storeFollowerMapper) {
        this.storeFollowerRepository = storeFollowerRepository;
        this.storeFollowerMapper = storeFollowerMapper;
    }

    /**
     * Save a storeFollower.
     *
     * @param storeFollowerDTO the entity to save.
     * @return the persisted entity.
     */
    public StoreFollowerDTO save(StoreFollowerDTO storeFollowerDTO) {
        log.debug("Request to save StoreFollower : {}", storeFollowerDTO);
        StoreFollower storeFollower = storeFollowerMapper.toEntity(storeFollowerDTO);
        storeFollower = storeFollowerRepository.save(storeFollower);
        return storeFollowerMapper.toDto(storeFollower);
    }

    /**
     * Partially update a storeFollower.
     *
     * @param storeFollowerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StoreFollowerDTO> partialUpdate(StoreFollowerDTO storeFollowerDTO) {
        log.debug("Request to partially update StoreFollower : {}", storeFollowerDTO);

        return storeFollowerRepository
            .findById(storeFollowerDTO.getId())
            .map(existingStoreFollower -> {
                storeFollowerMapper.partialUpdate(existingStoreFollower, storeFollowerDTO);

                return existingStoreFollower;
            })
            .map(storeFollowerRepository::save)
            .map(storeFollowerMapper::toDto);
    }

    /**
     * Get all the storeFollowers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StoreFollowerDTO> findAll() {
        log.debug("Request to get all StoreFollowers");
        return storeFollowerRepository.findAll().stream().map(storeFollowerMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one storeFollower by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StoreFollowerDTO> findOne(Long id) {
        log.debug("Request to get StoreFollower : {}", id);
        return storeFollowerRepository.findById(id).map(storeFollowerMapper::toDto);
    }

    /**
     * Delete the storeFollower by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StoreFollower : {}", id);
        storeFollowerRepository.deleteById(id);
    }
}
