package asc.foods.user.web.rest;

import asc.foods.user.repository.AscStoreRepository;
import asc.foods.user.service.AscStoreService;
import asc.foods.user.service.dto.AscStoreDTO;
import asc.foods.user.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link asc.foods.user.domain.AscStore}.
 */
@RestController
@RequestMapping("/api")
public class AscStoreResource {

    private final Logger log = LoggerFactory.getLogger(AscStoreResource.class);

    private static final String ENTITY_NAME = "userServiceAscStore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AscStoreService ascStoreService;

    private final AscStoreRepository ascStoreRepository;

    public AscStoreResource(AscStoreService ascStoreService, AscStoreRepository ascStoreRepository) {
        this.ascStoreService = ascStoreService;
        this.ascStoreRepository = ascStoreRepository;
    }

    /**
     * {@code POST  /asc-stores} : Create a new ascStore.
     *
     * @param ascStoreDTO the ascStoreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ascStoreDTO, or with status {@code 400 (Bad Request)} if the ascStore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asc-stores")
    public ResponseEntity<AscStoreDTO> createAscStore(@RequestBody AscStoreDTO ascStoreDTO) throws URISyntaxException {
        log.debug("REST request to save AscStore : {}", ascStoreDTO);
        if (ascStoreDTO.getId() != null) {
            throw new BadRequestAlertException("A new ascStore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AscStoreDTO result = ascStoreService.save(ascStoreDTO);
        return ResponseEntity
            .created(new URI("/api/asc-stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asc-stores/:id} : Updates an existing ascStore.
     *
     * @param id the id of the ascStoreDTO to save.
     * @param ascStoreDTO the ascStoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ascStoreDTO,
     * or with status {@code 400 (Bad Request)} if the ascStoreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ascStoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asc-stores/{id}")
    public ResponseEntity<AscStoreDTO> updateAscStore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AscStoreDTO ascStoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AscStore : {}, {}", id, ascStoreDTO);
        if (ascStoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ascStoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ascStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AscStoreDTO result = ascStoreService.save(ascStoreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ascStoreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asc-stores/:id} : Partial updates given fields of an existing ascStore, field will ignore if it is null
     *
     * @param id the id of the ascStoreDTO to save.
     * @param ascStoreDTO the ascStoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ascStoreDTO,
     * or with status {@code 400 (Bad Request)} if the ascStoreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ascStoreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ascStoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asc-stores/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AscStoreDTO> partialUpdateAscStore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AscStoreDTO ascStoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AscStore partially : {}, {}", id, ascStoreDTO);
        if (ascStoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ascStoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ascStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AscStoreDTO> result = ascStoreService.partialUpdate(ascStoreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ascStoreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asc-stores} : get all the ascStores.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ascStores in body.
     */
    @GetMapping("/asc-stores")
    public ResponseEntity<List<AscStoreDTO>> getAllAscStores(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of AscStores");
        Page<AscStoreDTO> page;
        if (eagerload) {
            page = ascStoreService.findAllWithEagerRelationships(pageable);
        } else {
            page = ascStoreService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asc-stores/:id} : get the "id" ascStore.
     *
     * @param id the id of the ascStoreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ascStoreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asc-stores/{id}")
    public ResponseEntity<AscStoreDTO> getAscStore(@PathVariable Long id) {
        log.debug("REST request to get AscStore : {}", id);
        Optional<AscStoreDTO> ascStoreDTO = ascStoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ascStoreDTO);
    }

    /**
     * {@code DELETE  /asc-stores/:id} : delete the "id" ascStore.
     *
     * @param id the id of the ascStoreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asc-stores/{id}")
    public ResponseEntity<Void> deleteAscStore(@PathVariable Long id) {
        log.debug("REST request to delete AscStore : {}", id);
        ascStoreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
