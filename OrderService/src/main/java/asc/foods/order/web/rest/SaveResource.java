package asc.foods.order.web.rest;

import asc.foods.order.repository.SaveRepository;
import asc.foods.order.service.SaveService;
import asc.foods.order.service.dto.SaveDTO;
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
 * REST controller for managing {@link asc.foods.order.domain.Save}.
 */
@RestController
@RequestMapping("/api")
public class SaveResource {

    private final Logger log = LoggerFactory.getLogger(SaveResource.class);

    private static final String ENTITY_NAME = "orderServiceSave";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SaveService saveService;

    private final SaveRepository saveRepository;

    public SaveResource(SaveService saveService, SaveRepository saveRepository) {
        this.saveService = saveService;
        this.saveRepository = saveRepository;
    }

    /**
     * {@code POST  /saves} : Create a new save.
     *
     * @param saveDTO the saveDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new saveDTO, or with status {@code 400 (Bad Request)} if the save has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/saves")
    public ResponseEntity<SaveDTO> createSave(@RequestBody SaveDTO saveDTO) throws URISyntaxException {
        log.debug("REST request to save Save : {}", saveDTO);
        if (saveDTO.getId() != null) {
            throw new BadRequestAlertException("A new save cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaveDTO result = saveService.save(saveDTO);
        return ResponseEntity
            .created(new URI("/api/saves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /saves/:id} : Updates an existing save.
     *
     * @param id the id of the saveDTO to save.
     * @param saveDTO the saveDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saveDTO,
     * or with status {@code 400 (Bad Request)} if the saveDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the saveDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/saves/{id}")
    public ResponseEntity<SaveDTO> updateSave(@PathVariable(value = "id", required = false) final Long id, @RequestBody SaveDTO saveDTO)
        throws URISyntaxException {
        log.debug("REST request to update Save : {}, {}", id, saveDTO);
        if (saveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saveDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SaveDTO result = saveService.save(saveDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saveDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /saves/:id} : Partial updates given fields of an existing save, field will ignore if it is null
     *
     * @param id the id of the saveDTO to save.
     * @param saveDTO the saveDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saveDTO,
     * or with status {@code 400 (Bad Request)} if the saveDTO is not valid,
     * or with status {@code 404 (Not Found)} if the saveDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the saveDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/saves/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SaveDTO> partialUpdateSave(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SaveDTO saveDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Save partially : {}, {}", id, saveDTO);
        if (saveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saveDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SaveDTO> result = saveService.partialUpdate(saveDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saveDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /saves} : get all the saves.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of saves in body.
     */
    @GetMapping("/saves")
    public List<SaveDTO> getAllSaves() {
        log.debug("REST request to get all Saves");
        return saveService.findAll();
    }

    /**
     * {@code GET  /saves/:id} : get the "id" save.
     *
     * @param id the id of the saveDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saveDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/saves/{id}")
    public ResponseEntity<SaveDTO> getSave(@PathVariable Long id) {
        log.debug("REST request to get Save : {}", id);
        Optional<SaveDTO> saveDTO = saveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saveDTO);
    }

    /**
     * {@code DELETE  /saves/:id} : delete the "id" save.
     *
     * @param id the id of the saveDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/saves/{id}")
    public ResponseEntity<Void> deleteSave(@PathVariable Long id) {
        log.debug("REST request to delete Save : {}", id);
        saveService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
