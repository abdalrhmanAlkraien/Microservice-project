package asc.foods.user.web.rest;

import asc.foods.user.repository.ProductBranchRepository;
import asc.foods.user.service.ProductBranchService;
import asc.foods.user.service.dto.ProductBranchDTO;
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
 * REST controller for managing {@link asc.foods.user.domain.ProductBranch}.
 */
@RestController
@RequestMapping("/api")
public class ProductBranchResource {

    private final Logger log = LoggerFactory.getLogger(ProductBranchResource.class);

    private static final String ENTITY_NAME = "userServiceProductBranch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductBranchService productBranchService;

    private final ProductBranchRepository productBranchRepository;

    public ProductBranchResource(ProductBranchService productBranchService, ProductBranchRepository productBranchRepository) {
        this.productBranchService = productBranchService;
        this.productBranchRepository = productBranchRepository;
    }

    /**
     * {@code POST  /product-branches} : Create a new productBranch.
     *
     * @param productBranchDTO the productBranchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productBranchDTO, or with status {@code 400 (Bad Request)} if the productBranch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-branches")
    public ResponseEntity<ProductBranchDTO> createProductBranch(@RequestBody ProductBranchDTO productBranchDTO) throws URISyntaxException {
        log.debug("REST request to save ProductBranch : {}", productBranchDTO);
        if (productBranchDTO.getId() != null) {
            throw new BadRequestAlertException("A new productBranch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductBranchDTO result = productBranchService.save(productBranchDTO);
        return ResponseEntity
            .created(new URI("/api/product-branches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-branches/:id} : Updates an existing productBranch.
     *
     * @param id the id of the productBranchDTO to save.
     * @param productBranchDTO the productBranchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productBranchDTO,
     * or with status {@code 400 (Bad Request)} if the productBranchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productBranchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-branches/{id}")
    public ResponseEntity<ProductBranchDTO> updateProductBranch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductBranchDTO productBranchDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductBranch : {}, {}", id, productBranchDTO);
        if (productBranchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productBranchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productBranchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductBranchDTO result = productBranchService.save(productBranchDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productBranchDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-branches/:id} : Partial updates given fields of an existing productBranch, field will ignore if it is null
     *
     * @param id the id of the productBranchDTO to save.
     * @param productBranchDTO the productBranchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productBranchDTO,
     * or with status {@code 400 (Bad Request)} if the productBranchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productBranchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productBranchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-branches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductBranchDTO> partialUpdateProductBranch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductBranchDTO productBranchDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductBranch partially : {}, {}", id, productBranchDTO);
        if (productBranchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productBranchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productBranchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductBranchDTO> result = productBranchService.partialUpdate(productBranchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productBranchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-branches} : get all the productBranches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productBranches in body.
     */
    @GetMapping("/product-branches")
    public List<ProductBranchDTO> getAllProductBranches() {
        log.debug("REST request to get all ProductBranches");
        return productBranchService.findAll();
    }

    /**
     * {@code GET  /product-branches/:id} : get the "id" productBranch.
     *
     * @param id the id of the productBranchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productBranchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-branches/{id}")
    public ResponseEntity<ProductBranchDTO> getProductBranch(@PathVariable Long id) {
        log.debug("REST request to get ProductBranch : {}", id);
        Optional<ProductBranchDTO> productBranchDTO = productBranchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productBranchDTO);
    }

    /**
     * {@code DELETE  /product-branches/:id} : delete the "id" productBranch.
     *
     * @param id the id of the productBranchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-branches/{id}")
    public ResponseEntity<Void> deleteProductBranch(@PathVariable Long id) {
        log.debug("REST request to delete ProductBranch : {}", id);
        productBranchService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
