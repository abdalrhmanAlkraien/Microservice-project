package asc.foods.user.service;

import asc.foods.user.domain.ViwedStory;
import asc.foods.user.repository.ViwedStoryRepository;
import asc.foods.user.service.dto.ViwedStoryDTO;
import asc.foods.user.service.mapper.ViwedStoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ViwedStory}.
 */
@Service
@Transactional
public class ViwedStoryService {

    private final Logger log = LoggerFactory.getLogger(ViwedStoryService.class);

    private final ViwedStoryRepository viwedStoryRepository;

    private final ViwedStoryMapper viwedStoryMapper;

    public ViwedStoryService(ViwedStoryRepository viwedStoryRepository, ViwedStoryMapper viwedStoryMapper) {
        this.viwedStoryRepository = viwedStoryRepository;
        this.viwedStoryMapper = viwedStoryMapper;
    }

    /**
     * Save a viwedStory.
     *
     * @param viwedStoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ViwedStoryDTO save(ViwedStoryDTO viwedStoryDTO) {
        log.debug("Request to save ViwedStory : {}", viwedStoryDTO);
        ViwedStory viwedStory = viwedStoryMapper.toEntity(viwedStoryDTO);
        viwedStory = viwedStoryRepository.save(viwedStory);
        return viwedStoryMapper.toDto(viwedStory);
    }

    /**
     * Partially update a viwedStory.
     *
     * @param viwedStoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ViwedStoryDTO> partialUpdate(ViwedStoryDTO viwedStoryDTO) {
        log.debug("Request to partially update ViwedStory : {}", viwedStoryDTO);

        return viwedStoryRepository
            .findById(viwedStoryDTO.getId())
            .map(existingViwedStory -> {
                viwedStoryMapper.partialUpdate(existingViwedStory, viwedStoryDTO);

                return existingViwedStory;
            })
            .map(viwedStoryRepository::save)
            .map(viwedStoryMapper::toDto);
    }

    /**
     * Get all the viwedStories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ViwedStoryDTO> findAll() {
        log.debug("Request to get all ViwedStories");
        return viwedStoryRepository.findAll().stream().map(viwedStoryMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one viwedStory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ViwedStoryDTO> findOne(Long id) {
        log.debug("Request to get ViwedStory : {}", id);
        return viwedStoryRepository.findById(id).map(viwedStoryMapper::toDto);
    }

    /**
     * Delete the viwedStory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ViwedStory : {}", id);
        viwedStoryRepository.deleteById(id);
    }
}
