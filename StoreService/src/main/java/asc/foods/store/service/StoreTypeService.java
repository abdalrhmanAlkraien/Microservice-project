package asc.foods.store.service;

import asc.foods.store.domain.StoreType;
import asc.foods.store.repository.StoreTypeRepository;
import asc.foods.store.service.dto.StoreTypeDTO;
import asc.foods.store.service.mapper.StoreTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StoreType}.
 */
@Service
@Transactional
public class StoreTypeService {

    private final Logger log = LoggerFactory.getLogger(StoreTypeService.class);

    private final StoreTypeRepository storeTypeRepository;

    private final StoreTypeMapper storeTypeMapper;

    public StoreTypeService(StoreTypeRepository storeTypeRepository, StoreTypeMapper storeTypeMapper) {
        this.storeTypeRepository = storeTypeRepository;
        this.storeTypeMapper = storeTypeMapper;
    }

    /**
     * Save a storeType.
     *
     * @param storeTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public StoreTypeDTO save(StoreTypeDTO storeTypeDTO) {
        log.debug("Request to save StoreType : {}", storeTypeDTO);
        StoreType storeType = storeTypeMapper.toEntity(storeTypeDTO);
        storeType = storeTypeRepository.save(storeType);
        return storeTypeMapper.toDto(storeType);
    }

    /**
     * Partially update a storeType.
     *
     * @param storeTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StoreTypeDTO> partialUpdate(StoreTypeDTO storeTypeDTO) {
        log.debug("Request to partially update StoreType : {}", storeTypeDTO);

        return storeTypeRepository
            .findById(storeTypeDTO.getId())
            .map(existingStoreType -> {
                storeTypeMapper.partialUpdate(existingStoreType, storeTypeDTO);

                return existingStoreType;
            })
            .map(storeTypeRepository::save)
            .map(storeTypeMapper::toDto);
    }

    /**
     * Get all the storeTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StoreTypeDTO> findAll() {
        log.debug("Request to get all StoreTypes");
        return storeTypeRepository.findAll().stream().map(storeTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one storeType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StoreTypeDTO> findOne(Long id) {
        log.debug("Request to get StoreType : {}", id);
        return storeTypeRepository.findById(id).map(storeTypeMapper::toDto);
    }

    /**
     * Delete the storeType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StoreType : {}", id);
        storeTypeRepository.deleteById(id);
    }
}
