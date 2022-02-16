package asc.foods.order.service;

import asc.foods.order.domain.ItemType;
import asc.foods.order.repository.ItemTypeRepository;
import asc.foods.order.service.dto.ItemTypeDTO;
import asc.foods.order.service.mapper.ItemTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemType}.
 */
@Service
@Transactional
public class ItemTypeService {

    private final Logger log = LoggerFactory.getLogger(ItemTypeService.class);

    private final ItemTypeRepository itemTypeRepository;

    private final ItemTypeMapper itemTypeMapper;

    public ItemTypeService(ItemTypeRepository itemTypeRepository, ItemTypeMapper itemTypeMapper) {
        this.itemTypeRepository = itemTypeRepository;
        this.itemTypeMapper = itemTypeMapper;
    }

    /**
     * Save a itemType.
     *
     * @param itemTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemTypeDTO save(ItemTypeDTO itemTypeDTO) {
        log.debug("Request to save ItemType : {}", itemTypeDTO);
        ItemType itemType = itemTypeMapper.toEntity(itemTypeDTO);
        itemType = itemTypeRepository.save(itemType);
        return itemTypeMapper.toDto(itemType);
    }

    /**
     * Partially update a itemType.
     *
     * @param itemTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ItemTypeDTO> partialUpdate(ItemTypeDTO itemTypeDTO) {
        log.debug("Request to partially update ItemType : {}", itemTypeDTO);

        return itemTypeRepository
            .findById(itemTypeDTO.getId())
            .map(existingItemType -> {
                itemTypeMapper.partialUpdate(existingItemType, itemTypeDTO);

                return existingItemType;
            })
            .map(itemTypeRepository::save)
            .map(itemTypeMapper::toDto);
    }

    /**
     * Get all the itemTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ItemTypeDTO> findAll() {
        log.debug("Request to get all ItemTypes");
        return itemTypeRepository.findAll().stream().map(itemTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one itemType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemTypeDTO> findOne(Long id) {
        log.debug("Request to get ItemType : {}", id);
        return itemTypeRepository.findById(id).map(itemTypeMapper::toDto);
    }

    /**
     * Delete the itemType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ItemType : {}", id);
        itemTypeRepository.deleteById(id);
    }
}
