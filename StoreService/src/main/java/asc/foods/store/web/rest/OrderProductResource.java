package asc.foods.store.web.rest;

import asc.foods.store.repository.OrderProductRepository;
import asc.foods.store.service.OrderProductService;
import asc.foods.store.service.dto.OrderProductDTO;
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
 * REST controller for managing {@link asc.foods.store.domain.OrderProduct}.
 */
@RestController
@RequestMapping("/api")
public class OrderProductResource {

    private final Logger log = LoggerFactory.getLogger(OrderProductResource.class);

    private static final String ENTITY_NAME = "storeServiceOrderProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderProductService orderProductService;

    private final OrderProductRepository orderProductRepository;

    public OrderProductResource(OrderProductService orderProductService, OrderProductRepository orderProductRepository) {
        this.orderProductService = orderProductService;
        this.orderProductRepository = orderProductRepository;
    }

    /**
     * {@code POST  /order-products} : Create a new orderProduct.
     *
     * @param orderProductDTO the orderProductDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderProductDTO, or with status {@code 400 (Bad Request)} if the orderProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-products")
    public ResponseEntity<OrderProductDTO> createOrderProduct(@RequestBody OrderProductDTO orderProductDTO) throws URISyntaxException {
        log.debug("REST request to save OrderProduct : {}", orderProductDTO);
        if (orderProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderProductDTO result = orderProductService.save(orderProductDTO);
        return ResponseEntity
            .created(new URI("/api/order-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-products/:id} : Updates an existing orderProduct.
     *
     * @param id the id of the orderProductDTO to save.
     * @param orderProductDTO the orderProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderProductDTO,
     * or with status {@code 400 (Bad Request)} if the orderProductDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-products/{id}")
    public ResponseEntity<OrderProductDTO> updateOrderProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderProductDTO orderProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderProduct : {}, {}", id, orderProductDTO);
        if (orderProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderProductDTO result = orderProductService.save(orderProductDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-products/:id} : Partial updates given fields of an existing orderProduct, field will ignore if it is null
     *
     * @param id the id of the orderProductDTO to save.
     * @param orderProductDTO the orderProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderProductDTO,
     * or with status {@code 400 (Bad Request)} if the orderProductDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderProductDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderProductDTO> partialUpdateOrderProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderProductDTO orderProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderProduct partially : {}, {}", id, orderProductDTO);
        if (orderProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderProductDTO> result = orderProductService.partialUpdate(orderProductDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderProductDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-products} : get all the orderProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderProducts in body.
     */
    @GetMapping("/order-products")
    public List<OrderProductDTO> getAllOrderProducts() {
        log.debug("REST request to get all OrderProducts");
        return orderProductService.findAll();
    }

    /**
     * {@code GET  /order-products/:id} : get the "id" orderProduct.
     *
     * @param id the id of the orderProductDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderProductDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-products/{id}")
    public ResponseEntity<OrderProductDTO> getOrderProduct(@PathVariable Long id) {
        log.debug("REST request to get OrderProduct : {}", id);
        Optional<OrderProductDTO> orderProductDTO = orderProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderProductDTO);
    }

    /**
     * {@code DELETE  /order-products/:id} : delete the "id" orderProduct.
     *
     * @param id the id of the orderProductDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-products/{id}")
    public ResponseEntity<Void> deleteOrderProduct(@PathVariable Long id) {
        log.debug("REST request to delete OrderProduct : {}", id);
        orderProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
