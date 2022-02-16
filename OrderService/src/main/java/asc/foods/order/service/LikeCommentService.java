package asc.foods.order.service;

import asc.foods.order.domain.LikeComment;
import asc.foods.order.repository.LikeCommentRepository;
import asc.foods.order.service.dto.LikeCommentDTO;
import asc.foods.order.service.mapper.LikeCommentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LikeComment}.
 */
@Service
@Transactional
public class LikeCommentService {

    private final Logger log = LoggerFactory.getLogger(LikeCommentService.class);

    private final LikeCommentRepository likeCommentRepository;

    private final LikeCommentMapper likeCommentMapper;

    public LikeCommentService(LikeCommentRepository likeCommentRepository, LikeCommentMapper likeCommentMapper) {
        this.likeCommentRepository = likeCommentRepository;
        this.likeCommentMapper = likeCommentMapper;
    }

    /**
     * Save a likeComment.
     *
     * @param likeCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public LikeCommentDTO save(LikeCommentDTO likeCommentDTO) {
        log.debug("Request to save LikeComment : {}", likeCommentDTO);
        LikeComment likeComment = likeCommentMapper.toEntity(likeCommentDTO);
        likeComment = likeCommentRepository.save(likeComment);
        return likeCommentMapper.toDto(likeComment);
    }

    /**
     * Partially update a likeComment.
     *
     * @param likeCommentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LikeCommentDTO> partialUpdate(LikeCommentDTO likeCommentDTO) {
        log.debug("Request to partially update LikeComment : {}", likeCommentDTO);

        return likeCommentRepository
            .findById(likeCommentDTO.getId())
            .map(existingLikeComment -> {
                likeCommentMapper.partialUpdate(existingLikeComment, likeCommentDTO);

                return existingLikeComment;
            })
            .map(likeCommentRepository::save)
            .map(likeCommentMapper::toDto);
    }

    /**
     * Get all the likeComments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LikeCommentDTO> findAll() {
        log.debug("Request to get all LikeComments");
        return likeCommentRepository.findAll().stream().map(likeCommentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one likeComment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LikeCommentDTO> findOne(Long id) {
        log.debug("Request to get LikeComment : {}", id);
        return likeCommentRepository.findById(id).map(likeCommentMapper::toDto);
    }

    /**
     * Delete the likeComment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LikeComment : {}", id);
        likeCommentRepository.deleteById(id);
    }
}
