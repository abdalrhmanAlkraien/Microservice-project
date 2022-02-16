package asc.foods.user.web.rest;

import asc.foods.user.repository.ProductTagRepository;
import asc.foods.user.service.ProductTagService;
import asc.foods.user.service.dto.ProductTagDTO;
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
 * REST controller for managing {@link asc.foods.user.domain.ProductTag}.
 */
@RestController
@RequestMapping("/api")
public class ProductTagResource {

    private final Logger log = LoggerFactory.getLogger(ProductTagResource.class);

    private static final String ENTITY_NAME = "userServiceProductTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductTagService productTagService;

    private final ProductTagRepository productTagRepository;

    public ProductTagResource(ProductTagService productTagService, ProductTagRepository productTagRepository) {
        this.productTagService = productTagService;
        this.productTagRepository = productTagRepository;
    }

    /**
     * {@code POST  /product-tags} : Create a new productTag.
     *
     * @param productTagDTO the productTagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productTagDTO, or with status {@code 400 (Bad Request)} if the productTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-tags")
    public ResponseEntity<ProductTagDTO> createProductTag(@RequestBody ProductTagDTO productTagDTO) throws URISyntaxException {
        log.debug("REST request to save ProductTag : {}", productTagDTO);
        if (productTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new productTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductTagDTO result = productTagService.save(productTagDTO);
        return ResponseEntity
            .created(new URI("/api/product-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-tags/:id} : Updates an existing productTag.
     *
     * @param id the id of the productTagDTO to save.
     * @param productTagDTO the productTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productTagDTO,
     * or with status {@code 400 (Bad Request)} if the productTagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-tags/{id}")
    public ResponseEntity<ProductTagDTO> updateProductTag(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductTagDTO productTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductTag : {}, {}", id, productTagDTO);
        if (productTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductTagDTO result = productTagService.save(productTagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-tags/:id} : Partial updates given fields of an existing productTag, field will ignore if it is null
     *
     * @param id the id of the productTagDTO to save.
     * @param productTagDTO the productTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productTagDTO,
     * or with status {@code 400 (Bad Request)} if the productTagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productTagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-tags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductTagDTO> partialUpdateProductTag(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductTagDTO productTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductTag partially : {}, {}", id, productTagDTO);
        if (productTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductTagDTO> result = productTagService.partialUpdate(productTagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productTagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-tags} : get all the productTags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productTags in body.
     */
    @GetMapping("/product-tags")
    public List<ProductTagDTO> getAllProductTags() {
        log.debug("REST request to get all ProductTags");
        return productTagService.findAll();
    }

    /**
     * {@code GET  /product-tags/:id} : get the "id" productTag.
     *
     * @param id the id of the productTagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productTagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-tags/{id}")
    public ResponseEntity<ProductTagDTO> getProductTag(@PathVariable Long id) {
        log.debug("REST request to get ProductTag : {}", id);
        Optional<ProductTagDTO> productTagDTO = productTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productTagDTO);
    }

    /**
     * {@code DELETE  /product-tags/:id} : delete the "id" productTag.
     *
     * @param id the id of the productTagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-tags/{id}")
    public ResponseEntity<Void> deleteProductTag(@PathVariable Long id) {
        log.debug("REST request to delete ProductTag : {}", id);
        productTagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
