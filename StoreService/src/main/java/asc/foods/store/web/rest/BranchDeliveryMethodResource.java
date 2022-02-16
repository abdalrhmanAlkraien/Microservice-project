package asc.foods.store.web.rest;

import asc.foods.store.repository.BranchDeliveryMethodRepository;
import asc.foods.store.service.BranchDeliveryMethodService;
import asc.foods.store.service.dto.BranchDeliveryMethodDTO;
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
 * REST controller for managing {@link asc.foods.store.domain.BranchDeliveryMethod}.
 */
@RestController
@RequestMapping("/api")
public class BranchDeliveryMethodResource {

    private final Logger log = LoggerFactory.getLogger(BranchDeliveryMethodResource.class);

    private static final String ENTITY_NAME = "storeServiceBranchDeliveryMethod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchDeliveryMethodService branchDeliveryMethodService;

    private final BranchDeliveryMethodRepository branchDeliveryMethodRepository;

    public BranchDeliveryMethodResource(
        BranchDeliveryMethodService branchDeliveryMethodService,
        BranchDeliveryMethodRepository branchDeliveryMethodRepository
    ) {
        this.branchDeliveryMethodService = branchDeliveryMethodService;
        this.branchDeliveryMethodRepository = branchDeliveryMethodRepository;
    }

    /**
     * {@code POST  /branch-delivery-methods} : Create a new branchDeliveryMethod.
     *
     * @param branchDeliveryMethodDTO the branchDeliveryMethodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchDeliveryMethodDTO, or with status {@code 400 (Bad Request)} if the branchDeliveryMethod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branch-delivery-methods")
    public ResponseEntity<BranchDeliveryMethodDTO> createBranchDeliveryMethod(@RequestBody BranchDeliveryMethodDTO branchDeliveryMethodDTO)
        throws URISyntaxException {
        log.debug("REST request to save BranchDeliveryMethod : {}", branchDeliveryMethodDTO);
        if (branchDeliveryMethodDTO.getId() != null) {
            throw new BadRequestAlertException("A new branchDeliveryMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BranchDeliveryMethodDTO result = branchDeliveryMethodService.save(branchDeliveryMethodDTO);
        return ResponseEntity
            .created(new URI("/api/branch-delivery-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branch-delivery-methods/:id} : Updates an existing branchDeliveryMethod.
     *
     * @param id the id of the branchDeliveryMethodDTO to save.
     * @param branchDeliveryMethodDTO the branchDeliveryMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchDeliveryMethodDTO,
     * or with status {@code 400 (Bad Request)} if the branchDeliveryMethodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchDeliveryMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branch-delivery-methods/{id}")
    public ResponseEntity<BranchDeliveryMethodDTO> updateBranchDeliveryMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BranchDeliveryMethodDTO branchDeliveryMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BranchDeliveryMethod : {}, {}", id, branchDeliveryMethodDTO);
        if (branchDeliveryMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchDeliveryMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchDeliveryMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BranchDeliveryMethodDTO result = branchDeliveryMethodService.save(branchDeliveryMethodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, branchDeliveryMethodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /branch-delivery-methods/:id} : Partial updates given fields of an existing branchDeliveryMethod, field will ignore if it is null
     *
     * @param id the id of the branchDeliveryMethodDTO to save.
     * @param branchDeliveryMethodDTO the branchDeliveryMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchDeliveryMethodDTO,
     * or with status {@code 400 (Bad Request)} if the branchDeliveryMethodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the branchDeliveryMethodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the branchDeliveryMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/branch-delivery-methods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BranchDeliveryMethodDTO> partialUpdateBranchDeliveryMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BranchDeliveryMethodDTO branchDeliveryMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BranchDeliveryMethod partially : {}, {}", id, branchDeliveryMethodDTO);
        if (branchDeliveryMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchDeliveryMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchDeliveryMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BranchDeliveryMethodDTO> result = branchDeliveryMethodService.partialUpdate(branchDeliveryMethodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, branchDeliveryMethodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /branch-delivery-methods} : get all the branchDeliveryMethods.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchDeliveryMethods in body.
     */
    @GetMapping("/branch-delivery-methods")
    public List<BranchDeliveryMethodDTO> getAllBranchDeliveryMethods() {
        log.debug("REST request to get all BranchDeliveryMethods");
        return branchDeliveryMethodService.findAll();
    }

    /**
     * {@code GET  /branch-delivery-methods/:id} : get the "id" branchDeliveryMethod.
     *
     * @param id the id of the branchDeliveryMethodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchDeliveryMethodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch-delivery-methods/{id}")
    public ResponseEntity<BranchDeliveryMethodDTO> getBranchDeliveryMethod(@PathVariable Long id) {
        log.debug("REST request to get BranchDeliveryMethod : {}", id);
        Optional<BranchDeliveryMethodDTO> branchDeliveryMethodDTO = branchDeliveryMethodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branchDeliveryMethodDTO);
    }

    /**
     * {@code DELETE  /branch-delivery-methods/:id} : delete the "id" branchDeliveryMethod.
     *
     * @param id the id of the branchDeliveryMethodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branch-delivery-methods/{id}")
    public ResponseEntity<Void> deleteBranchDeliveryMethod(@PathVariable Long id) {
        log.debug("REST request to delete BranchDeliveryMethod : {}", id);
        branchDeliveryMethodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
