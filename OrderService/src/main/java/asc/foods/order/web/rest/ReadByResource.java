package asc.foods.order.web.rest;

import asc.foods.order.repository.ReadByRepository;
import asc.foods.order.service.ReadByService;
import asc.foods.order.service.dto.ReadByDTO;
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
 * REST controller for managing {@link asc.foods.order.domain.ReadBy}.
 */
@RestController
@RequestMapping("/api")
public class ReadByResource {

    private final Logger log = LoggerFactory.getLogger(ReadByResource.class);

    private static final String ENTITY_NAME = "orderServiceReadBy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReadByService readByService;

    private final ReadByRepository readByRepository;

    public ReadByResource(ReadByService readByService, ReadByRepository readByRepository) {
        this.readByService = readByService;
        this.readByRepository = readByRepository;
    }

    /**
     * {@code POST  /read-bies} : Create a new readBy.
     *
     * @param readByDTO the readByDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new readByDTO, or with status {@code 400 (Bad Request)} if the readBy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/read-bies")
    public ResponseEntity<ReadByDTO> createReadBy(@RequestBody ReadByDTO readByDTO) throws URISyntaxException {
        log.debug("REST request to save ReadBy : {}", readByDTO);
        if (readByDTO.getId() != null) {
            throw new BadRequestAlertException("A new readBy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReadByDTO result = readByService.save(readByDTO);
        return ResponseEntity
            .created(new URI("/api/read-bies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /read-bies/:id} : Updates an existing readBy.
     *
     * @param id the id of the readByDTO to save.
     * @param readByDTO the readByDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated readByDTO,
     * or with status {@code 400 (Bad Request)} if the readByDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the readByDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/read-bies/{id}")
    public ResponseEntity<ReadByDTO> updateReadBy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReadByDTO readByDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReadBy : {}, {}", id, readByDTO);
        if (readByDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, readByDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!readByRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReadByDTO result = readByService.save(readByDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, readByDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /read-bies/:id} : Partial updates given fields of an existing readBy, field will ignore if it is null
     *
     * @param id the id of the readByDTO to save.
     * @param readByDTO the readByDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated readByDTO,
     * or with status {@code 400 (Bad Request)} if the readByDTO is not valid,
     * or with status {@code 404 (Not Found)} if the readByDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the readByDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/read-bies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReadByDTO> partialUpdateReadBy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReadByDTO readByDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReadBy partially : {}, {}", id, readByDTO);
        if (readByDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, readByDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!readByRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReadByDTO> result = readByService.partialUpdate(readByDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, readByDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /read-bies} : get all the readBies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of readBies in body.
     */
    @GetMapping("/read-bies")
    public List<ReadByDTO> getAllReadBies() {
        log.debug("REST request to get all ReadBies");
        return readByService.findAll();
    }

    /**
     * {@code GET  /read-bies/:id} : get the "id" readBy.
     *
     * @param id the id of the readByDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the readByDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/read-bies/{id}")
    public ResponseEntity<ReadByDTO> getReadBy(@PathVariable Long id) {
        log.debug("REST request to get ReadBy : {}", id);
        Optional<ReadByDTO> readByDTO = readByService.findOne(id);
        return ResponseUtil.wrapOrNotFound(readByDTO);
    }

    /**
     * {@code DELETE  /read-bies/:id} : delete the "id" readBy.
     *
     * @param id the id of the readByDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/read-bies/{id}")
    public ResponseEntity<Void> deleteReadBy(@PathVariable Long id) {
        log.debug("REST request to delete ReadBy : {}", id);
        readByService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
