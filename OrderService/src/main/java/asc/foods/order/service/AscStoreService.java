package asc.foods.order.service;

import asc.foods.order.domain.AscStore;
import asc.foods.order.repository.AscStoreRepository;
import asc.foods.order.service.dto.AscStoreDTO;
import asc.foods.order.service.mapper.AscStoreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AscStore}.
 */
@Service
@Transactional
public class AscStoreService {

    private final Logger log = LoggerFactory.getLogger(AscStoreService.class);

    private final AscStoreRepository ascStoreRepository;

    private final AscStoreMapper ascStoreMapper;

    public AscStoreService(AscStoreRepository ascStoreRepository, AscStoreMapper ascStoreMapper) {
        this.ascStoreRepository = ascStoreRepository;
        this.ascStoreMapper = ascStoreMapper;
    }

    /**
     * Save a ascStore.
     *
     * @param ascStoreDTO the entity to save.
     * @return the persisted entity.
     */
    public AscStoreDTO save(AscStoreDTO ascStoreDTO) {
        log.debug("Request to save AscStore : {}", ascStoreDTO);
        AscStore ascStore = ascStoreMapper.toEntity(ascStoreDTO);
        ascStore = ascStoreRepository.save(ascStore);
        return ascStoreMapper.toDto(ascStore);
    }

    /**
     * Partially update a ascStore.
     *
     * @param ascStoreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AscStoreDTO> partialUpdate(AscStoreDTO ascStoreDTO) {
        log.debug("Request to partially update AscStore : {}", ascStoreDTO);

        return ascStoreRepository
            .findById(ascStoreDTO.getId())
            .map(existingAscStore -> {
                ascStoreMapper.partialUpdate(existingAscStore, ascStoreDTO);

                return existingAscStore;
            })
            .map(ascStoreRepository::save)
            .map(ascStoreMapper::toDto);
    }

    /**
     * Get all the ascStores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AscStoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AscStores");
        return ascStoreRepository.findAll(pageable).map(ascStoreMapper::toDto);
    }

    /**
     * Get all the ascStores with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AscStoreDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ascStoreRepository.findAllWithEagerRelationships(pageable).map(ascStoreMapper::toDto);
    }

    /**
     * Get one ascStore by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AscStoreDTO> findOne(Long id) {
        log.debug("Request to get AscStore : {}", id);
        return ascStoreRepository.findOneWithEagerRelationships(id).map(ascStoreMapper::toDto);
    }

    /**
     * Delete the ascStore by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AscStore : {}", id);
        ascStoreRepository.deleteById(id);
    }
}
