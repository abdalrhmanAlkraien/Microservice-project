package asc.foods.user.web.rest;

import asc.foods.user.repository.OrderCustomeRepository;
import asc.foods.user.service.OrderCustomeService;
import asc.foods.user.service.dto.OrderCustomeDTO;
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
 * REST controller for managing {@link asc.foods.user.domain.OrderCustome}.
 */
@RestController
@RequestMapping("/api")
public class OrderCustomeResource {

    private final Logger log = LoggerFactory.getLogger(OrderCustomeResource.class);

    private static final String ENTITY_NAME = "userServiceOrderCustome";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderCustomeService orderCustomeService;

    private final OrderCustomeRepository orderCustomeRepository;

    public OrderCustomeResource(OrderCustomeService orderCustomeService, OrderCustomeRepository orderCustomeRepository) {
        this.orderCustomeService = orderCustomeService;
        this.orderCustomeRepository = orderCustomeRepository;
    }

    /**
     * {@code POST  /order-customes} : Create a new orderCustome.
     *
     * @param orderCustomeDTO the orderCustomeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderCustomeDTO, or with status {@code 400 (Bad Request)} if the orderCustome has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-customes")
    public ResponseEntity<OrderCustomeDTO> createOrderCustome(@RequestBody OrderCustomeDTO orderCustomeDTO) throws URISyntaxException {
        log.debug("REST request to save OrderCustome : {}", orderCustomeDTO);
        if (orderCustomeDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderCustome cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderCustomeDTO result = orderCustomeService.save(orderCustomeDTO);
        return ResponseEntity
            .created(new URI("/api/order-customes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-customes/:id} : Updates an existing orderCustome.
     *
     * @param id the id of the orderCustomeDTO to save.
     * @param orderCustomeDTO the orderCustomeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderCustomeDTO,
     * or with status {@code 400 (Bad Request)} if the orderCustomeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderCustomeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-customes/{id}")
    public ResponseEntity<OrderCustomeDTO> updateOrderCustome(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderCustomeDTO orderCustomeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderCustome : {}, {}", id, orderCustomeDTO);
        if (orderCustomeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderCustomeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderCustomeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderCustomeDTO result = orderCustomeService.save(orderCustomeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderCustomeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-customes/:id} : Partial updates given fields of an existing orderCustome, field will ignore if it is null
     *
     * @param id the id of the orderCustomeDTO to save.
     * @param orderCustomeDTO the orderCustomeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderCustomeDTO,
     * or with status {@code 400 (Bad Request)} if the orderCustomeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderCustomeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderCustomeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-customes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderCustomeDTO> partialUpdateOrderCustome(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderCustomeDTO orderCustomeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderCustome partially : {}, {}", id, orderCustomeDTO);
        if (orderCustomeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderCustomeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderCustomeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderCustomeDTO> result = orderCustomeService.partialUpdate(orderCustomeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderCustomeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-customes} : get all the orderCustomes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderCustomes in body.
     */
    @GetMapping("/order-customes")
    public List<OrderCustomeDTO> getAllOrderCustomes() {
        log.debug("REST request to get all OrderCustomes");
        return orderCustomeService.findAll();
    }

    /**
     * {@code GET  /order-customes/:id} : get the "id" orderCustome.
     *
     * @param id the id of the orderCustomeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderCustomeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-customes/{id}")
    public ResponseEntity<OrderCustomeDTO> getOrderCustome(@PathVariable Long id) {
        log.debug("REST request to get OrderCustome : {}", id);
        Optional<OrderCustomeDTO> orderCustomeDTO = orderCustomeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderCustomeDTO);
    }

    /**
     * {@code DELETE  /order-customes/:id} : delete the "id" orderCustome.
     *
     * @param id the id of the orderCustomeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-customes/{id}")
    public ResponseEntity<Void> deleteOrderCustome(@PathVariable Long id) {
        log.debug("REST request to delete OrderCustome : {}", id);
        orderCustomeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
