package asc.foods.user.web.rest;

import asc.foods.user.repository.UserProductRepository;
import asc.foods.user.service.UserProductService;
import asc.foods.user.service.dto.UserProductDTO;
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
 * REST controller for managing {@link asc.foods.user.domain.UserProduct}.
 */
@RestController
@RequestMapping("/api")
public class UserProductResource {

    private final Logger log = LoggerFactory.getLogger(UserProductResource.class);

    private static final String ENTITY_NAME = "userServiceUserProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserProductService userProductService;

    private final UserProductRepository userProductRepository;

    public UserProductResource(UserProductService userProductService, UserProductRepository userProductRepository) {
        this.userProductService = userProductService;
        this.userProductRepository = userProductRepository;
    }

    /**
     * {@code POST  /user-products} : Create a new userProduct.
     *
     * @param userProductDTO the userProductDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userProductDTO, or with status {@code 400 (Bad Request)} if the userProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-products")
    public ResponseEntity<UserProductDTO> createUserProduct(@RequestBody UserProductDTO userProductDTO) throws URISyntaxException {
        log.debug("REST request to save UserProduct : {}", userProductDTO);
        if (userProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new userProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserProductDTO result = userProductService.save(userProductDTO);
        return ResponseEntity
            .created(new URI("/api/user-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-products/:id} : Updates an existing userProduct.
     *
     * @param id the id of the userProductDTO to save.
     * @param userProductDTO the userProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userProductDTO,
     * or with status {@code 400 (Bad Request)} if the userProductDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-products/{id}")
    public ResponseEntity<UserProductDTO> updateUserProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserProductDTO userProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserProduct : {}, {}", id, userProductDTO);
        if (userProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserProductDTO result = userProductService.save(userProductDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-products/:id} : Partial updates given fields of an existing userProduct, field will ignore if it is null
     *
     * @param id the id of the userProductDTO to save.
     * @param userProductDTO the userProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userProductDTO,
     * or with status {@code 400 (Bad Request)} if the userProductDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userProductDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserProductDTO> partialUpdateUserProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserProductDTO userProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserProduct partially : {}, {}", id, userProductDTO);
        if (userProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserProductDTO> result = userProductService.partialUpdate(userProductDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userProductDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-products} : get all the userProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userProducts in body.
     */
    @GetMapping("/user-products")
    public List<UserProductDTO> getAllUserProducts() {
        log.debug("REST request to get all UserProducts");
        return userProductService.findAll();
    }

    /**
     * {@code GET  /user-products/:id} : get the "id" userProduct.
     *
     * @param id the id of the userProductDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userProductDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-products/{id}")
    public ResponseEntity<UserProductDTO> getUserProduct(@PathVariable Long id) {
        log.debug("REST request to get UserProduct : {}", id);
        Optional<UserProductDTO> userProductDTO = userProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userProductDTO);
    }

    /**
     * {@code DELETE  /user-products/:id} : delete the "id" userProduct.
     *
     * @param id the id of the userProductDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-products/{id}")
    public ResponseEntity<Void> deleteUserProduct(@PathVariable Long id) {
        log.debug("REST request to delete UserProduct : {}", id);
        userProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
