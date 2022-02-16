package asc.foods.store.web.rest;

import asc.foods.store.repository.ViwedStoryRepository;
import asc.foods.store.service.ViwedStoryService;
import asc.foods.store.service.dto.ViwedStoryDTO;
import asc.foods.store.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link asc.foods.store.domain.ViwedStory}.
 */
@RestController
@RequestMapping("/api")
public class ViwedStoryResource {

    private final Logger log = LoggerFactory.getLogger(ViwedStoryResource.class);

    private static final String ENTITY_NAME = "storeServiceViwedStory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViwedStoryService viwedStoryService;

    private final ViwedStoryRepository viwedStoryRepository;

    public ViwedStoryResource(ViwedStoryService viwedStoryService, ViwedStoryRepository viwedStoryRepository) {
        this.viwedStoryService = viwedStoryService;
        this.viwedStoryRepository = viwedStoryRepository;
    }

    /**
     * {@code POST  /viwed-stories} : Create a new viwedStory.
     *
     * @param viwedStoryDTO the viwedStoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new viwedStoryDTO, or with status {@code 400 (Bad Request)} if the viwedStory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/viwed-stories")
    public ResponseEntity<ViwedStoryDTO> createViwedStory(@RequestBody ViwedStoryDTO viwedStoryDTO) throws URISyntaxException {
        log.debug("REST request to save ViwedStory : {}", viwedStoryDTO);
        if (viwedStoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new viwedStory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ViwedStoryDTO result = viwedStoryService.save(viwedStoryDTO);
        return ResponseEntity
            .created(new URI("/api/viwed-stories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /viwed-stories/:id} : Updates an existing viwedStory.
     *
     * @param id the id of the viwedStoryDTO to save.
     * @param viwedStoryDTO the viwedStoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viwedStoryDTO,
     * or with status {@code 400 (Bad Request)} if the viwedStoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viwedStoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/viwed-stories/{id}")
    public ResponseEntity<ViwedStoryDTO> updateViwedStory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ViwedStoryDTO viwedStoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ViwedStory : {}, {}", id, viwedStoryDTO);
        if (viwedStoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viwedStoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viwedStoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ViwedStoryDTO result = viwedStoryService.save(viwedStoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viwedStoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /viwed-stories/:id} : Partial updates given fields of an existing viwedStory, field will ignore if it is null
     *
     * @param id the id of the viwedStoryDTO to save.
     * @param viwedStoryDTO the viwedStoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viwedStoryDTO,
     * or with status {@code 400 (Bad Request)} if the viwedStoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the viwedStoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the viwedStoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/viwed-stories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ViwedStoryDTO> partialUpdateViwedStory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ViwedStoryDTO viwedStoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ViwedStory partially : {}, {}", id, viwedStoryDTO);
        if (viwedStoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viwedStoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viwedStoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ViwedStoryDTO> result = viwedStoryService.partialUpdate(viwedStoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viwedStoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /viwed-stories} : get all the viwedStories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viwedStories in body.
     */
    @GetMapping("/viwed-stories")
    public List<ViwedStoryDTO> getAllViwedStories() {
        log.debug("REST request to get all ViwedStories");
        return viwedStoryService.findAll();
    }

    /**
     * {@code GET  /viwed-stories/:id} : get the "id" viwedStory.
     *
     * @param id the id of the viwedStoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the viwedStoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/viwed-stories/{id}")
    public ResponseEntity<ViwedStoryDTO> getViwedStory(@PathVariable Long id) {
        log.debug("REST request to get ViwedStory : {}", id);
        Optional<ViwedStoryDTO> viwedStoryDTO = viwedStoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(viwedStoryDTO);
    }

    /**
     * {@code DELETE  /viwed-stories/:id} : delete the "id" viwedStory.
     *
     * @param id the id of the viwedStoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/viwed-stories/{id}")
    public ResponseEntity<Void> deleteViwedStory(@PathVariable Long id) {
        log.debug("REST request to delete ViwedStory : {}", id);
        viwedStoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
