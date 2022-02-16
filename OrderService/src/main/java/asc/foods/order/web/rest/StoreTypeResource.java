package asc.foods.order.web.rest;

import asc.foods.order.repository.StoreTypeRepository;
import asc.foods.order.service.StoreTypeService;
import asc.foods.order.service.dto.StoreTypeDTO;
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
 * REST controller for managing {@link asc.foods.order.domain.StoreType}.
 */
@RestController
@RequestMapping("/api")
public class StoreTypeResource {

    private final Logger log = LoggerFactory.getLogger(StoreTypeResource.class);

    private static final String ENTITY_NAME = "orderServiceStoreType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoreTypeService storeTypeService;

    private final StoreTypeRepository storeTypeRepository;

    public StoreTypeResource(StoreTypeService storeTypeService, StoreTypeRepository storeTypeRepository) {
        this.storeTypeService = storeTypeService;
        this.storeTypeRepository = storeTypeRepository;
    }

    /**
     * {@code POST  /store-types} : Create a new storeType.
     *
     * @param storeTypeDTO the storeTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storeTypeDTO, or with status {@code 400 (Bad Request)} if the storeType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/store-types")
    public ResponseEntity<StoreTypeDTO> createStoreType(@RequestBody StoreTypeDTO storeTypeDTO) throws URISyntaxException {
        log.debug("REST request to save StoreType : {}", storeTypeDTO);
        if (storeTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new storeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoreTypeDTO result = storeTypeService.save(storeTypeDTO);
        return ResponseEntity
            .created(new URI("/api/store-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /store-types/:id} : Updates an existing storeType.
     *
     * @param id the id of the storeTypeDTO to save.
     * @param storeTypeDTO the storeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the storeTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/store-types/{id}")
    public ResponseEntity<StoreTypeDTO> updateStoreType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StoreTypeDTO storeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StoreType : {}, {}", id, storeTypeDTO);
        if (storeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StoreTypeDTO result = storeTypeService.save(storeTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storeTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /store-types/:id} : Partial updates given fields of an existing storeType, field will ignore if it is null
     *
     * @param id the id of the storeTypeDTO to save.
     * @param storeTypeDTO the storeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the storeTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storeTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/store-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StoreTypeDTO> partialUpdateStoreType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StoreTypeDTO storeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StoreType partially : {}, {}", id, storeTypeDTO);
        if (storeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StoreTypeDTO> result = storeTypeService.partialUpdate(storeTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storeTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /store-types} : get all the storeTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storeTypes in body.
     */
    @GetMapping("/store-types")
    public List<StoreTypeDTO> getAllStoreTypes() {
        log.debug("REST request to get all StoreTypes");
        return storeTypeService.findAll();
    }

    /**
     * {@code GET  /store-types/:id} : get the "id" storeType.
     *
     * @param id the id of the storeTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storeTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/store-types/{id}")
    public ResponseEntity<StoreTypeDTO> getStoreType(@PathVariable Long id) {
        log.debug("REST request to get StoreType : {}", id);
        Optional<StoreTypeDTO> storeTypeDTO = storeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storeTypeDTO);
    }

    /**
     * {@code DELETE  /store-types/:id} : delete the "id" storeType.
     *
     * @param id the id of the storeTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/store-types/{id}")
    public ResponseEntity<Void> deleteStoreType(@PathVariable Long id) {
        log.debug("REST request to delete StoreType : {}", id);
        storeTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
