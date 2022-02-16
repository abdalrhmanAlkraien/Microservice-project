package asc.foods.order.web.rest;

import asc.foods.order.repository.AscOrderRepository;
import asc.foods.order.service.AscOrderService;
import asc.foods.order.service.dto.AscOrderDTO;
import asc.foods.order.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link asc.foods.order.domain.AscOrder}.
 */
@RestController
@RequestMapping("/api")
public class AscOrderResource {

    private final Logger log = LoggerFactory.getLogger(AscOrderResource.class);

    private static final String ENTITY_NAME = "orderServiceAscOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AscOrderService ascOrderService;

    private final AscOrderRepository ascOrderRepository;

    public AscOrderResource(AscOrderService ascOrderService, AscOrderRepository ascOrderRepository) {
        this.ascOrderService = ascOrderService;
        this.ascOrderRepository = ascOrderRepository;
    }

    /**
     * {@code POST  /asc-orders} : Create a new ascOrder.
     *
     * @param ascOrderDTO the ascOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ascOrderDTO, or with status {@code 400 (Bad Request)} if the ascOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asc-orders")
    public ResponseEntity<AscOrderDTO> createAscOrder(@RequestBody AscOrderDTO ascOrderDTO) throws URISyntaxException {
        log.debug("REST request to save AscOrder : {}", ascOrderDTO);
        if (ascOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new ascOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AscOrderDTO result = ascOrderService.save(ascOrderDTO);
        return ResponseEntity
            .created(new URI("/api/asc-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asc-orders/:id} : Updates an existing ascOrder.
     *
     * @param id the id of the ascOrderDTO to save.
     * @param ascOrderDTO the ascOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ascOrderDTO,
     * or with status {@code 400 (Bad Request)} if the ascOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ascOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asc-orders/{id}")
    public ResponseEntity<AscOrderDTO> updateAscOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AscOrderDTO ascOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AscOrder : {}, {}", id, ascOrderDTO);
        if (ascOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ascOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ascOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AscOrderDTO result = ascOrderService.save(ascOrderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ascOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asc-orders/:id} : Partial updates given fields of an existing ascOrder, field will ignore if it is null
     *
     * @param id the id of the ascOrderDTO to save.
     * @param ascOrderDTO the ascOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ascOrderDTO,
     * or with status {@code 400 (Bad Request)} if the ascOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ascOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ascOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asc-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AscOrderDTO> partialUpdateAscOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AscOrderDTO ascOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AscOrder partially : {}, {}", id, ascOrderDTO);
        if (ascOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ascOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ascOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AscOrderDTO> result = ascOrderService.partialUpdate(ascOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ascOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asc-orders} : get all the ascOrders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ascOrders in body.
     */
    @GetMapping("/asc-orders")
    public ResponseEntity<List<AscOrderDTO>> getAllAscOrders(Pageable pageable) {
        log.debug("REST request to get a page of AscOrders");
        Page<AscOrderDTO> page = ascOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asc-orders/:id} : get the "id" ascOrder.
     *
     * @param id the id of the ascOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ascOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asc-orders/{id}")
    public ResponseEntity<AscOrderDTO> getAscOrder(@PathVariable Long id) {
        log.debug("REST request to get AscOrder : {}", id);
        Optional<AscOrderDTO> ascOrderDTO = ascOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ascOrderDTO);
    }

    /**
     * {@code DELETE  /asc-orders/:id} : delete the "id" ascOrder.
     *
     * @param id the id of the ascOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asc-orders/{id}")
    public ResponseEntity<Void> deleteAscOrder(@PathVariable Long id) {
        log.debug("REST request to delete AscOrder : {}", id);
        ascOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
