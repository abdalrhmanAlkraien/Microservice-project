package asc.foods.order.web.rest;

import asc.foods.order.repository.StoreFollowerRepository;
import asc.foods.order.service.StoreFollowerService;
import asc.foods.order.service.dto.StoreFollowerDTO;
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
 * REST controller for managing {@link asc.foods.order.domain.StoreFollower}.
 */
@RestController
@RequestMapping("/api")
public class StoreFollowerResource {

    private final Logger log = LoggerFactory.getLogger(StoreFollowerResource.class);

    private static final String ENTITY_NAME = "orderServiceStoreFollower";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoreFollowerService storeFollowerService;

    private final StoreFollowerRepository storeFollowerRepository;

    public StoreFollowerResource(StoreFollowerService storeFollowerService, StoreFollowerRepository storeFollowerRepository) {
        this.storeFollowerService = storeFollowerService;
        this.storeFollowerRepository = storeFollowerRepository;
    }

    /**
     * {@code POST  /store-followers} : Create a new storeFollower.
     *
     * @param storeFollowerDTO the storeFollowerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storeFollowerDTO, or with status {@code 400 (Bad Request)} if the storeFollower has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/store-followers")
    public ResponseEntity<StoreFollowerDTO> createStoreFollower(@RequestBody StoreFollowerDTO storeFollowerDTO) throws URISyntaxException {
        log.debug("REST request to save StoreFollower : {}", storeFollowerDTO);
        if (storeFollowerDTO.getId() != null) {
            throw new BadRequestAlertException("A new storeFollower cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoreFollowerDTO result = storeFollowerService.save(storeFollowerDTO);
        return ResponseEntity
            .created(new URI("/api/store-followers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /store-followers/:id} : Updates an existing storeFollower.
     *
     * @param id the id of the storeFollowerDTO to save.
     * @param storeFollowerDTO the storeFollowerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storeFollowerDTO,
     * or with status {@code 400 (Bad Request)} if the storeFollowerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storeFollowerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/store-followers/{id}")
    public ResponseEntity<StoreFollowerDTO> updateStoreFollower(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StoreFollowerDTO storeFollowerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StoreFollower : {}, {}", id, storeFollowerDTO);
        if (storeFollowerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storeFollowerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storeFollowerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StoreFollowerDTO result = storeFollowerService.save(storeFollowerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storeFollowerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /store-followers/:id} : Partial updates given fields of an existing storeFollower, field will ignore if it is null
     *
     * @param id the id of the storeFollowerDTO to save.
     * @param storeFollowerDTO the storeFollowerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storeFollowerDTO,
     * or with status {@code 400 (Bad Request)} if the storeFollowerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storeFollowerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storeFollowerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/store-followers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StoreFollowerDTO> partialUpdateStoreFollower(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StoreFollowerDTO storeFollowerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StoreFollower partially : {}, {}", id, storeFollowerDTO);
        if (storeFollowerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storeFollowerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storeFollowerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StoreFollowerDTO> result = storeFollowerService.partialUpdate(storeFollowerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storeFollowerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /store-followers} : get all the storeFollowers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storeFollowers in body.
     */
    @GetMapping("/store-followers")
    public List<StoreFollowerDTO> getAllStoreFollowers() {
        log.debug("REST request to get all StoreFollowers");
        return storeFollowerService.findAll();
    }

    /**
     * {@code GET  /store-followers/:id} : get the "id" storeFollower.
     *
     * @param id the id of the storeFollowerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storeFollowerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/store-followers/{id}")
    public ResponseEntity<StoreFollowerDTO> getStoreFollower(@PathVariable Long id) {
        log.debug("REST request to get StoreFollower : {}", id);
        Optional<StoreFollowerDTO> storeFollowerDTO = storeFollowerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storeFollowerDTO);
    }

    /**
     * {@code DELETE  /store-followers/:id} : delete the "id" storeFollower.
     *
     * @param id the id of the storeFollowerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/store-followers/{id}")
    public ResponseEntity<Void> deleteStoreFollower(@PathVariable Long id) {
        log.debug("REST request to delete StoreFollower : {}", id);
        storeFollowerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
