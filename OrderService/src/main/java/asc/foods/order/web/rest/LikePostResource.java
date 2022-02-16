package asc.foods.order.web.rest;

import asc.foods.order.repository.LikePostRepository;
import asc.foods.order.service.LikePostService;
import asc.foods.order.service.dto.LikePostDTO;
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
 * REST controller for managing {@link asc.foods.order.domain.LikePost}.
 */
@RestController
@RequestMapping("/api")
public class LikePostResource {

    private final Logger log = LoggerFactory.getLogger(LikePostResource.class);

    private static final String ENTITY_NAME = "orderServiceLikePost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikePostService likePostService;

    private final LikePostRepository likePostRepository;

    public LikePostResource(LikePostService likePostService, LikePostRepository likePostRepository) {
        this.likePostService = likePostService;
        this.likePostRepository = likePostRepository;
    }

    /**
     * {@code POST  /like-posts} : Create a new likePost.
     *
     * @param likePostDTO the likePostDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new likePostDTO, or with status {@code 400 (Bad Request)} if the likePost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/like-posts")
    public ResponseEntity<LikePostDTO> createLikePost(@RequestBody LikePostDTO likePostDTO) throws URISyntaxException {
        log.debug("REST request to save LikePost : {}", likePostDTO);
        if (likePostDTO.getId() != null) {
            throw new BadRequestAlertException("A new likePost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LikePostDTO result = likePostService.save(likePostDTO);
        return ResponseEntity
            .created(new URI("/api/like-posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /like-posts/:id} : Updates an existing likePost.
     *
     * @param id the id of the likePostDTO to save.
     * @param likePostDTO the likePostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likePostDTO,
     * or with status {@code 400 (Bad Request)} if the likePostDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the likePostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/like-posts/{id}")
    public ResponseEntity<LikePostDTO> updateLikePost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikePostDTO likePostDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LikePost : {}, {}", id, likePostDTO);
        if (likePostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likePostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likePostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LikePostDTO result = likePostService.save(likePostDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likePostDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /like-posts/:id} : Partial updates given fields of an existing likePost, field will ignore if it is null
     *
     * @param id the id of the likePostDTO to save.
     * @param likePostDTO the likePostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likePostDTO,
     * or with status {@code 400 (Bad Request)} if the likePostDTO is not valid,
     * or with status {@code 404 (Not Found)} if the likePostDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the likePostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/like-posts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LikePostDTO> partialUpdateLikePost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikePostDTO likePostDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LikePost partially : {}, {}", id, likePostDTO);
        if (likePostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likePostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likePostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LikePostDTO> result = likePostService.partialUpdate(likePostDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likePostDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /like-posts} : get all the likePosts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likePosts in body.
     */
    @GetMapping("/like-posts")
    public List<LikePostDTO> getAllLikePosts() {
        log.debug("REST request to get all LikePosts");
        return likePostService.findAll();
    }

    /**
     * {@code GET  /like-posts/:id} : get the "id" likePost.
     *
     * @param id the id of the likePostDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the likePostDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/like-posts/{id}")
    public ResponseEntity<LikePostDTO> getLikePost(@PathVariable Long id) {
        log.debug("REST request to get LikePost : {}", id);
        Optional<LikePostDTO> likePostDTO = likePostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(likePostDTO);
    }

    /**
     * {@code DELETE  /like-posts/:id} : delete the "id" likePost.
     *
     * @param id the id of the likePostDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/like-posts/{id}")
    public ResponseEntity<Void> deleteLikePost(@PathVariable Long id) {
        log.debug("REST request to delete LikePost : {}", id);
        likePostService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
