package asc.foods.store.web.rest;

import asc.foods.store.repository.ProductOptionRepository;
import asc.foods.store.service.ProductOptionService;
import asc.foods.store.service.dto.ProductOptionDTO;
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
 * REST controller for managing {@link asc.foods.store.domain.ProductOption}.
 */
@RestController
@RequestMapping("/api")
public class ProductOptionResource {

    private final Logger log = LoggerFactory.getLogger(ProductOptionResource.class);

    private static final String ENTITY_NAME = "storeServiceProductOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductOptionService productOptionService;

    private final ProductOptionRepository productOptionRepository;

    public ProductOptionResource(ProductOptionService productOptionService, ProductOptionRepository productOptionRepository) {
        this.productOptionService = productOptionService;
        this.productOptionRepository = productOptionRepository;
    }

    /**
     * {@code POST  /product-options} : Create a new productOption.
     *
     * @param productOptionDTO the productOptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productOptionDTO, or with status {@code 400 (Bad Request)} if the productOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-options")
    public ResponseEntity<ProductOptionDTO> createProductOption(@RequestBody ProductOptionDTO productOptionDTO) throws URISyntaxException {
        log.debug("REST request to save ProductOption : {}", productOptionDTO);
        if (productOptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new productOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductOptionDTO result = productOptionService.save(productOptionDTO);
        return ResponseEntity
            .created(new URI("/api/product-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-options/:id} : Updates an existing productOption.
     *
     * @param id the id of the productOptionDTO to save.
     * @param productOptionDTO the productOptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOptionDTO,
     * or with status {@code 400 (Bad Request)} if the productOptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productOptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-options/{id}")
    public ResponseEntity<ProductOptionDTO> updateProductOption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductOptionDTO productOptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductOption : {}, {}", id, productOptionDTO);
        if (productOptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productOptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductOptionDTO result = productOptionService.save(productOptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productOptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-options/:id} : Partial updates given fields of an existing productOption, field will ignore if it is null
     *
     * @param id the id of the productOptionDTO to save.
     * @param productOptionDTO the productOptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOptionDTO,
     * or with status {@code 400 (Bad Request)} if the productOptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productOptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productOptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-options/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductOptionDTO> partialUpdateProductOption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductOptionDTO productOptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductOption partially : {}, {}", id, productOptionDTO);
        if (productOptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productOptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductOptionDTO> result = productOptionService.partialUpdate(productOptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productOptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-options} : get all the productOptions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productOptions in body.
     */
    @GetMapping("/product-options")
    public List<ProductOptionDTO> getAllProductOptions() {
        log.debug("REST request to get all ProductOptions");
        return productOptionService.findAll();
    }

    /**
     * {@code GET  /product-options/:id} : get the "id" productOption.
     *
     * @param id the id of the productOptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productOptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-options/{id}")
    public ResponseEntity<ProductOptionDTO> getProductOption(@PathVariable Long id) {
        log.debug("REST request to get ProductOption : {}", id);
        Optional<ProductOptionDTO> productOptionDTO = productOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productOptionDTO);
    }

    /**
     * {@code DELETE  /product-options/:id} : delete the "id" productOption.
     *
     * @param id the id of the productOptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-options/{id}")
    public ResponseEntity<Void> deleteProductOption(@PathVariable Long id) {
        log.debug("REST request to delete ProductOption : {}", id);
        productOptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
