package asc.foods.order.web.rest;

import asc.foods.order.repository.MealCustmizeRepository;
import asc.foods.order.service.MealCustmizeService;
import asc.foods.order.service.dto.MealCustmizeDTO;
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
 * REST controller for managing {@link asc.foods.order.domain.MealCustmize}.
 */
@RestController
@RequestMapping("/api")
public class MealCustmizeResource {

    private final Logger log = LoggerFactory.getLogger(MealCustmizeResource.class);

    private static final String ENTITY_NAME = "orderServiceMealCustmize";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MealCustmizeService mealCustmizeService;

    private final MealCustmizeRepository mealCustmizeRepository;

    public MealCustmizeResource(MealCustmizeService mealCustmizeService, MealCustmizeRepository mealCustmizeRepository) {
        this.mealCustmizeService = mealCustmizeService;
        this.mealCustmizeRepository = mealCustmizeRepository;
    }

    /**
     * {@code POST  /meal-custmizes} : Create a new mealCustmize.
     *
     * @param mealCustmizeDTO the mealCustmizeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mealCustmizeDTO, or with status {@code 400 (Bad Request)} if the mealCustmize has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meal-custmizes")
    public ResponseEntity<MealCustmizeDTO> createMealCustmize(@RequestBody MealCustmizeDTO mealCustmizeDTO) throws URISyntaxException {
        log.debug("REST request to save MealCustmize : {}", mealCustmizeDTO);
        if (mealCustmizeDTO.getId() != null) {
            throw new BadRequestAlertException("A new mealCustmize cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MealCustmizeDTO result = mealCustmizeService.save(mealCustmizeDTO);
        return ResponseEntity
            .created(new URI("/api/meal-custmizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meal-custmizes/:id} : Updates an existing mealCustmize.
     *
     * @param id the id of the mealCustmizeDTO to save.
     * @param mealCustmizeDTO the mealCustmizeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mealCustmizeDTO,
     * or with status {@code 400 (Bad Request)} if the mealCustmizeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mealCustmizeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meal-custmizes/{id}")
    public ResponseEntity<MealCustmizeDTO> updateMealCustmize(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MealCustmizeDTO mealCustmizeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MealCustmize : {}, {}", id, mealCustmizeDTO);
        if (mealCustmizeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mealCustmizeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mealCustmizeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MealCustmizeDTO result = mealCustmizeService.save(mealCustmizeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mealCustmizeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /meal-custmizes/:id} : Partial updates given fields of an existing mealCustmize, field will ignore if it is null
     *
     * @param id the id of the mealCustmizeDTO to save.
     * @param mealCustmizeDTO the mealCustmizeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mealCustmizeDTO,
     * or with status {@code 400 (Bad Request)} if the mealCustmizeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mealCustmizeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mealCustmizeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/meal-custmizes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MealCustmizeDTO> partialUpdateMealCustmize(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MealCustmizeDTO mealCustmizeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MealCustmize partially : {}, {}", id, mealCustmizeDTO);
        if (mealCustmizeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mealCustmizeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mealCustmizeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MealCustmizeDTO> result = mealCustmizeService.partialUpdate(mealCustmizeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mealCustmizeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meal-custmizes} : get all the mealCustmizes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mealCustmizes in body.
     */
    @GetMapping("/meal-custmizes")
    public List<MealCustmizeDTO> getAllMealCustmizes() {
        log.debug("REST request to get all MealCustmizes");
        return mealCustmizeService.findAll();
    }

    /**
     * {@code GET  /meal-custmizes/:id} : get the "id" mealCustmize.
     *
     * @param id the id of the mealCustmizeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mealCustmizeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meal-custmizes/{id}")
    public ResponseEntity<MealCustmizeDTO> getMealCustmize(@PathVariable Long id) {
        log.debug("REST request to get MealCustmize : {}", id);
        Optional<MealCustmizeDTO> mealCustmizeDTO = mealCustmizeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mealCustmizeDTO);
    }

    /**
     * {@code DELETE  /meal-custmizes/:id} : delete the "id" mealCustmize.
     *
     * @param id the id of the mealCustmizeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meal-custmizes/{id}")
    public ResponseEntity<Void> deleteMealCustmize(@PathVariable Long id) {
        log.debug("REST request to delete MealCustmize : {}", id);
        mealCustmizeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
