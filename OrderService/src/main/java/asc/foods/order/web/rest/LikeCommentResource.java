package asc.foods.order.web.rest;

import asc.foods.order.repository.LikeCommentRepository;
import asc.foods.order.service.LikeCommentService;
import asc.foods.order.service.dto.LikeCommentDTO;
import asc.foods.order.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link asc.foods.order.domain.LikeComment}.
 */
@RestController
@RequestMapping("/api")
public class LikeCommentResource {

    private final Logger log = LoggerFactory.getLogger(LikeCommentResource.class);

    private static final String ENTITY_NAME = "orderServiceLikeComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikeCommentService likeCommentService;

    private final LikeCommentRepository likeCommentRepository;

    public LikeCommentResource(LikeCommentService likeCommentService, LikeCommentRepository likeCommentRepository) {
        this.likeCommentService = likeCommentService;
        this.likeCommentRepository = likeCommentRepository;
    }

    /**
     * {@code POST  /like-comments} : Create a new likeComment.
     *
     * @param likeCommentDTO the likeCommentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new likeCommentDTO, or with status {@code 400 (Bad Request)} if the likeComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/like-comments")
    public ResponseEntity<LikeCommentDTO> createLikeComment(@RequestBody LikeCommentDTO likeCommentDTO) throws URISyntaxException {
        log.debug("REST request to save LikeComment : {}", likeCommentDTO);
        if (likeCommentDTO.getId() != null) {
            throw new BadRequestAlertException("A new likeComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LikeCommentDTO result = likeCommentService.save(likeCommentDTO);
        return ResponseEntity
            .created(new URI("/api/like-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /like-comments/:id} : Updates an existing likeComment.
     *
     * @param id the id of the likeCommentDTO to save.
     * @param likeCommentDTO the likeCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeCommentDTO,
     * or with status {@code 400 (Bad Request)} if the likeCommentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the likeCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/like-comments/{id}")
    public ResponseEntity<LikeCommentDTO> updateLikeComment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikeCommentDTO likeCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LikeComment : {}, {}", id, likeCommentDTO);
        if (likeCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LikeCommentDTO result = likeCommentService.save(likeCommentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeCommentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /like-comments/:id} : Partial updates given fields of an existing likeComment, field will ignore if it is null
     *
     * @param id the id of the likeCommentDTO to save.
     * @param likeCommentDTO the likeCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeCommentDTO,
     * or with status {@code 400 (Bad Request)} if the likeCommentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the likeCommentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the likeCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/like-comments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LikeCommentDTO> partialUpdateLikeComment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikeCommentDTO likeCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LikeComment partially : {}, {}", id, likeCommentDTO);
        if (likeCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LikeCommentDTO> result = likeCommentService.partialUpdate(likeCommentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeCommentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /like-comments} : get all the likeComments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likeComments in body.
     */
    @GetMapping("/like-comments")
    public List<LikeCommentDTO> getAllLikeComments() {
        log.debug("REST request to get all LikeComments");
        return likeCommentService.findAll();
    }

    /**
     * {@code GET  /like-comments/:id} : get the "id" likeComment.
     *
     * @param id the id of the likeCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the likeCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/like-comments/{id}")
    public ResponseEntity<LikeCommentDTO> getLikeComment(@PathVariable Long id) {
        log.debug("REST request to get LikeComment : {}", id);
        Optional<LikeCommentDTO> likeCommentDTO = likeCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(likeCommentDTO);
    }

    /**
     * {@code DELETE  /like-comments/:id} : delete the "id" likeComment.
     *
     * @param id the id of the likeCommentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/like-comments/{id}")
    public ResponseEntity<Void> deleteLikeComment(@PathVariable Long id) {
        log.debug("REST request to delete LikeComment : {}", id);
        likeCommentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
