package asc.foods.user.web.rest;

import asc.foods.user.repository.PostMultimediaRepository;
import asc.foods.user.service.PostMultimediaService;
import asc.foods.user.service.dto.PostMultimediaDTO;
import asc.foods.user.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link asc.foods.user.domain.PostMultimedia}.
 */
@RestController
@RequestMapping("/api")
public class PostMultimediaResource {

    private final Logger log = LoggerFactory.getLogger(PostMultimediaResource.class);

    private static final String ENTITY_NAME = "userServicePostMultimedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostMultimediaService postMultimediaService;

    private final PostMultimediaRepository postMultimediaRepository;

    public PostMultimediaResource(PostMultimediaService postMultimediaService, PostMultimediaRepository postMultimediaRepository) {
        this.postMultimediaService = postMultimediaService;
        this.postMultimediaRepository = postMultimediaRepository;
    }

    /**
     * {@code POST  /post-multimedias} : Create a new postMultimedia.
     *
     * @param postMultimediaDTO the postMultimediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postMultimediaDTO, or with status {@code 400 (Bad Request)} if the postMultimedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/post-multimedias")
    public ResponseEntity<PostMultimediaDTO> createPostMultimedia(@RequestBody PostMultimediaDTO postMultimediaDTO)
        throws URISyntaxException {
        log.debug("REST request to save PostMultimedia : {}", postMultimediaDTO);
        if (postMultimediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new postMultimedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostMultimediaDTO result = postMultimediaService.save(postMultimediaDTO);
        return ResponseEntity
            .created(new URI("/api/post-multimedias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /post-multimedias/:id} : Updates an existing postMultimedia.
     *
     * @param id the id of the postMultimediaDTO to save.
     * @param postMultimediaDTO the postMultimediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postMultimediaDTO,
     * or with status {@code 400 (Bad Request)} if the postMultimediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postMultimediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/post-multimedias/{id}")
    public ResponseEntity<PostMultimediaDTO> updatePostMultimedia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PostMultimediaDTO postMultimediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PostMultimedia : {}, {}", id, postMultimediaDTO);
        if (postMultimediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postMultimediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postMultimediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PostMultimediaDTO result = postMultimediaService.save(postMultimediaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postMultimediaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /post-multimedias/:id} : Partial updates given fields of an existing postMultimedia, field will ignore if it is null
     *
     * @param id the id of the postMultimediaDTO to save.
     * @param postMultimediaDTO the postMultimediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postMultimediaDTO,
     * or with status {@code 400 (Bad Request)} if the postMultimediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the postMultimediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the postMultimediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/post-multimedias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostMultimediaDTO> partialUpdatePostMultimedia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PostMultimediaDTO postMultimediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostMultimedia partially : {}, {}", id, postMultimediaDTO);
        if (postMultimediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postMultimediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postMultimediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostMultimediaDTO> result = postMultimediaService.partialUpdate(postMultimediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postMultimediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /post-multimedias} : get all the postMultimedias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postMultimedias in body.
     */
    @GetMapping("/post-multimedias")
    public List<PostMultimediaDTO> getAllPostMultimedias() {
        log.debug("REST request to get all PostMultimedias");
        return postMultimediaService.findAll();
    }

    /**
     * {@code GET  /post-multimedias/:id} : get the "id" postMultimedia.
     *
     * @param id the id of the postMultimediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postMultimediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/post-multimedias/{id}")
    public ResponseEntity<PostMultimediaDTO> getPostMultimedia(@PathVariable Long id) {
        log.debug("REST request to get PostMultimedia : {}", id);
        Optional<PostMultimediaDTO> postMultimediaDTO = postMultimediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postMultimediaDTO);
    }

    /**
     * {@code DELETE  /post-multimedias/:id} : delete the "id" postMultimedia.
     *
     * @param id the id of the postMultimediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/post-multimedias/{id}")
    public ResponseEntity<Void> deletePostMultimedia(@PathVariable Long id) {
        log.debug("REST request to delete PostMultimedia : {}", id);
        postMultimediaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
