package asc.foods.user.service;

import asc.foods.user.domain.LikePost;
import asc.foods.user.repository.LikePostRepository;
import asc.foods.user.service.dto.LikePostDTO;
import asc.foods.user.service.mapper.LikePostMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LikePost}.
 */
@Service
@Transactional
public class LikePostService {

    private final Logger log = LoggerFactory.getLogger(LikePostService.class);

    private final LikePostRepository likePostRepository;

    private final LikePostMapper likePostMapper;

    public LikePostService(LikePostRepository likePostRepository, LikePostMapper likePostMapper) {
        this.likePostRepository = likePostRepository;
        this.likePostMapper = likePostMapper;
    }

    /**
     * Save a likePost.
     *
     * @param likePostDTO the entity to save.
     * @return the persisted entity.
     */
    public LikePostDTO save(LikePostDTO likePostDTO) {
        log.debug("Request to save LikePost : {}", likePostDTO);
        LikePost likePost = likePostMapper.toEntity(likePostDTO);
        likePost = likePostRepository.save(likePost);
        return likePostMapper.toDto(likePost);
    }

    /**
     * Partially update a likePost.
     *
     * @param likePostDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LikePostDTO> partialUpdate(LikePostDTO likePostDTO) {
        log.debug("Request to partially update LikePost : {}", likePostDTO);

        return likePostRepository
            .findById(likePostDTO.getId())
            .map(existingLikePost -> {
                likePostMapper.partialUpdate(existingLikePost, likePostDTO);

                return existingLikePost;
            })
            .map(likePostRepository::save)
            .map(likePostMapper::toDto);
    }

    /**
     * Get all the likePosts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LikePostDTO> findAll() {
        log.debug("Request to get all LikePosts");
        return likePostRepository.findAll().stream().map(likePostMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one likePost by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LikePostDTO> findOne(Long id) {
        log.debug("Request to get LikePost : {}", id);
        return likePostRepository.findById(id).map(likePostMapper::toDto);
    }

    /**
     * Delete the likePost by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LikePost : {}", id);
        likePostRepository.deleteById(id);
    }
}
