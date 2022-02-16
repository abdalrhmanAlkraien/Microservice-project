package asc.foods.user.web.rest;

import asc.foods.user.repository.StoryRepository;
import asc.foods.user.service.StoryService;
import asc.foods.user.service.dto.StoryDTO;
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
 * REST controller for managing {@link asc.foods.user.domain.Story}.
 */
@RestController
@RequestMapping("/api")
public class StoryResource {

    private final Logger log = LoggerFactory.getLogger(StoryResource.class);

    private static final String ENTITY_NAME = "userServiceStory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoryService storyService;

    private final StoryRepository storyRepository;

    public StoryResource(StoryService storyService, StoryRepository storyRepository) {
        this.storyService = storyService;
        this.storyRepository = storyRepository;
    }

    /**
     * {@code POST  /stories} : Create a new story.
     *
     * @param storyDTO the storyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storyDTO, or with status {@code 400 (Bad Request)} if the story has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stories")
    public ResponseEntity<StoryDTO> createStory(@RequestBody StoryDTO storyDTO) throws URISyntaxException {
        log.debug("REST request to save Story : {}", storyDTO);
        if (storyDTO.getId() != null) {
            throw new BadRequestAlertException("A new story cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoryDTO result = storyService.save(storyDTO);
        return ResponseEntity
            .created(new URI("/api/stories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stories/:id} : Updates an existing story.
     *
     * @param id the id of the storyDTO to save.
     * @param storyDTO the storyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storyDTO,
     * or with status {@code 400 (Bad Request)} if the storyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stories/{id}")
    public ResponseEntity<StoryDTO> updateStory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StoryDTO storyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Story : {}, {}", id, storyDTO);
        if (storyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StoryDTO result = storyService.save(storyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stories/:id} : Partial updates given fields of an existing story, field will ignore if it is null
     *
     * @param id the id of the storyDTO to save.
     * @param storyDTO the storyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storyDTO,
     * or with status {@code 400 (Bad Request)} if the storyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/stories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StoryDTO> partialUpdateStory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StoryDTO storyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Story partially : {}, {}", id, storyDTO);
        if (storyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StoryDTO> result = storyService.partialUpdate(storyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stories} : get all the stories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stories in body.
     */
    @GetMapping("/stories")
    public List<StoryDTO> getAllStories() {
        log.debug("REST request to get all Stories");
        return storyService.findAll();
    }

    /**
     * {@code GET  /stories/:id} : get the "id" story.
     *
     * @param id the id of the storyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stories/{id}")
    public ResponseEntity<StoryDTO> getStory(@PathVariable Long id) {
        log.debug("REST request to get Story : {}", id);
        Optional<StoryDTO> storyDTO = storyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storyDTO);
    }

    /**
     * {@code DELETE  /stories/:id} : delete the "id" story.
     *
     * @param id the id of the storyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stories/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        log.debug("REST request to delete Story : {}", id);
        storyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
