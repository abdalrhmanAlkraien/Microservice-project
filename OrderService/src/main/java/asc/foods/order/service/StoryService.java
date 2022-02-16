package asc.foods.order.service;

import asc.foods.order.domain.Story;
import asc.foods.order.repository.StoryRepository;
import asc.foods.order.service.dto.StoryDTO;
import asc.foods.order.service.mapper.StoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Story}.
 */
@Service
@Transactional
public class StoryService {

    private final Logger log = LoggerFactory.getLogger(StoryService.class);

    private final StoryRepository storyRepository;

    private final StoryMapper storyMapper;

    public StoryService(StoryRepository storyRepository, StoryMapper storyMapper) {
        this.storyRepository = storyRepository;
        this.storyMapper = storyMapper;
    }

    /**
     * Save a story.
     *
     * @param storyDTO the entity to save.
     * @return the persisted entity.
     */
    public StoryDTO save(StoryDTO storyDTO) {
        log.debug("Request to save Story : {}", storyDTO);
        Story story = storyMapper.toEntity(storyDTO);
        story = storyRepository.save(story);
        return storyMapper.toDto(story);
    }

    /**
     * Partially update a story.
     *
     * @param storyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StoryDTO> partialUpdate(StoryDTO storyDTO) {
        log.debug("Request to partially update Story : {}", storyDTO);

        return storyRepository
            .findById(storyDTO.getId())
            .map(existingStory -> {
                storyMapper.partialUpdate(existingStory, storyDTO);

                return existingStory;
            })
            .map(storyRepository::save)
            .map(storyMapper::toDto);
    }

    /**
     * Get all the stories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StoryDTO> findAll() {
        log.debug("Request to get all Stories");
        return storyRepository.findAll().stream().map(storyMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one story by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StoryDTO> findOne(Long id) {
        log.debug("Request to get Story : {}", id);
        return storyRepository.findById(id).map(storyMapper::toDto);
    }

    /**
     * Delete the story by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Story : {}", id);
        storyRepository.deleteById(id);
    }
}
