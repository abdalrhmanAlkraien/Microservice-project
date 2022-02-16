package asc.foods.store.web.rest;

import asc.foods.store.repository.FoodGenreRepository;
import asc.foods.store.service.FoodGenreService;
import asc.foods.store.service.dto.FoodGenreDTO;
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
 * REST controller for managing {@link asc.foods.store.domain.FoodGenre}.
 */
@RestController
@RequestMapping("/api")
public class FoodGenreResource {

    private final Logger log = LoggerFactory.getLogger(FoodGenreResource.class);

    private static final String ENTITY_NAME = "storeServiceFoodGenre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FoodGenreService foodGenreService;

    private final FoodGenreRepository foodGenreRepository;

    public FoodGenreResource(FoodGenreService foodGenreService, FoodGenreRepository foodGenreRepository) {
        this.foodGenreService = foodGenreService;
        this.foodGenreRepository = foodGenreRepository;
    }

    /**
     * {@code POST  /food-genres} : Create a new foodGenre.
     *
     * @param foodGenreDTO the foodGenreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new foodGenreDTO, or with status {@code 400 (Bad Request)} if the foodGenre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/food-genres")
    public ResponseEntity<FoodGenreDTO> createFoodGenre(@RequestBody FoodGenreDTO foodGenreDTO) throws URISyntaxException {
        log.debug("REST request to save FoodGenre : {}", foodGenreDTO);
        if (foodGenreDTO.getId() != null) {
            throw new BadRequestAlertException("A new foodGenre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FoodGenreDTO result = foodGenreService.save(foodGenreDTO);
        return ResponseEntity
            .created(new URI("/api/food-genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /food-genres/:id} : Updates an existing foodGenre.
     *
     * @param id the id of the foodGenreDTO to save.
     * @param foodGenreDTO the foodGenreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodGenreDTO,
     * or with status {@code 400 (Bad Request)} if the foodGenreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the foodGenreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/food-genres/{id}")
    public ResponseEntity<FoodGenreDTO> updateFoodGenre(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FoodGenreDTO foodGenreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FoodGenre : {}, {}", id, foodGenreDTO);
        if (foodGenreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodGenreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodGenreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FoodGenreDTO result = foodGenreService.save(foodGenreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodGenreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /food-genres/:id} : Partial updates given fields of an existing foodGenre, field will ignore if it is null
     *
     * @param id the id of the foodGenreDTO to save.
     * @param foodGenreDTO the foodGenreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodGenreDTO,
     * or with status {@code 400 (Bad Request)} if the foodGenreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the foodGenreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the foodGenreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/food-genres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FoodGenreDTO> partialUpdateFoodGenre(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FoodGenreDTO foodGenreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FoodGenre partially : {}, {}", id, foodGenreDTO);
        if (foodGenreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodGenreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodGenreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FoodGenreDTO> result = foodGenreService.partialUpdate(foodGenreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodGenreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /food-genres} : get all the foodGenres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of foodGenres in body.
     */
    @GetMapping("/food-genres")
    public List<FoodGenreDTO> getAllFoodGenres() {
        log.debug("REST request to get all FoodGenres");
        return foodGenreService.findAll();
    }

    /**
     * {@code GET  /food-genres/:id} : get the "id" foodGenre.
     *
     * @param id the id of the foodGenreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the foodGenreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/food-genres/{id}")
    public ResponseEntity<FoodGenreDTO> getFoodGenre(@PathVariable Long id) {
        log.debug("REST request to get FoodGenre : {}", id);
        Optional<FoodGenreDTO> foodGenreDTO = foodGenreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(foodGenreDTO);
    }

    /**
     * {@code DELETE  /food-genres/:id} : delete the "id" foodGenre.
     *
     * @param id the id of the foodGenreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/food-genres/{id}")
    public ResponseEntity<Void> deleteFoodGenre(@PathVariable Long id) {
        log.debug("REST request to delete FoodGenre : {}", id);
        foodGenreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
