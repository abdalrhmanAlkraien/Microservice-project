package asc.foods.user.service;

import asc.foods.user.domain.PostMultimedia;
import asc.foods.user.repository.PostMultimediaRepository;
import asc.foods.user.service.dto.PostMultimediaDTO;
import asc.foods.user.service.mapper.PostMultimediaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PostMultimedia}.
 */
@Service
@Transactional
public class PostMultimediaService {

    private final Logger log = LoggerFactory.getLogger(PostMultimediaService.class);

    private final PostMultimediaRepository postMultimediaRepository;

    private final PostMultimediaMapper postMultimediaMapper;

    public PostMultimediaService(PostMultimediaRepository postMultimediaRepository, PostMultimediaMapper postMultimediaMapper) {
        this.postMultimediaRepository = postMultimediaRepository;
        this.postMultimediaMapper = postMultimediaMapper;
    }

    /**
     * Save a postMultimedia.
     *
     * @param postMultimediaDTO the entity to save.
     * @return the persisted entity.
     */
    public PostMultimediaDTO save(PostMultimediaDTO postMultimediaDTO) {
        log.debug("Request to save PostMultimedia : {}", postMultimediaDTO);
        PostMultimedia postMultimedia = postMultimediaMapper.toEntity(postMultimediaDTO);
        postMultimedia = postMultimediaRepository.save(postMultimedia);
        return postMultimediaMapper.toDto(postMultimedia);
    }

    /**
     * Partially update a postMultimedia.
     *
     * @param postMultimediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PostMultimediaDTO> partialUpdate(PostMultimediaDTO postMultimediaDTO) {
        log.debug("Request to partially update PostMultimedia : {}", postMultimediaDTO);

        return postMultimediaRepository
            .findById(postMultimediaDTO.getId())
            .map(existingPostMultimedia -> {
                postMultimediaMapper.partialUpdate(existingPostMultimedia, postMultimediaDTO);

                return existingPostMultimedia;
            })
            .map(postMultimediaRepository::save)
            .map(postMultimediaMapper::toDto);
    }

    /**
     * Get all the postMultimedias.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PostMultimediaDTO> findAll() {
        log.debug("Request to get all PostMultimedias");
        return postMultimediaRepository
            .findAll()
            .stream()
            .map(postMultimediaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one postMultimedia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PostMultimediaDTO> findOne(Long id) {
        log.debug("Request to get PostMultimedia : {}", id);
        return postMultimediaRepository.findById(id).map(postMultimediaMapper::toDto);
    }

    /**
     * Delete the postMultimedia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PostMultimedia : {}", id);
        postMultimediaRepository.deleteById(id);
    }
}
