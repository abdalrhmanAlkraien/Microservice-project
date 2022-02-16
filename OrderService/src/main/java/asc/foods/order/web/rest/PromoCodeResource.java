package asc.foods.order.web.rest;

import asc.foods.order.repository.PromoCodeRepository;
import asc.foods.order.service.PromoCodeService;
import asc.foods.order.service.dto.PromoCodeDTO;
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
 * REST controller for managing {@link asc.foods.order.domain.PromoCode}.
 */
@RestController
@RequestMapping("/api")
public class PromoCodeResource {

    private final Logger log = LoggerFactory.getLogger(PromoCodeResource.class);

    private static final String ENTITY_NAME = "orderServicePromoCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromoCodeService promoCodeService;

    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeResource(PromoCodeService promoCodeService, PromoCodeRepository promoCodeRepository) {
        this.promoCodeService = promoCodeService;
        this.promoCodeRepository = promoCodeRepository;
    }

    /**
     * {@code POST  /promo-codes} : Create a new promoCode.
     *
     * @param promoCodeDTO the promoCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promoCodeDTO, or with status {@code 400 (Bad Request)} if the promoCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promo-codes")
    public ResponseEntity<PromoCodeDTO> createPromoCode(@RequestBody PromoCodeDTO promoCodeDTO) throws URISyntaxException {
        log.debug("REST request to save PromoCode : {}", promoCodeDTO);
        if (promoCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new promoCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PromoCodeDTO result = promoCodeService.save(promoCodeDTO);
        return ResponseEntity
            .created(new URI("/api/promo-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /promo-codes/:id} : Updates an existing promoCode.
     *
     * @param id the id of the promoCodeDTO to save.
     * @param promoCodeDTO the promoCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promoCodeDTO,
     * or with status {@code 400 (Bad Request)} if the promoCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promoCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promo-codes/{id}")
    public ResponseEntity<PromoCodeDTO> updatePromoCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PromoCodeDTO promoCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PromoCode : {}, {}", id, promoCodeDTO);
        if (promoCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promoCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promoCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PromoCodeDTO result = promoCodeService.save(promoCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promoCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /promo-codes/:id} : Partial updates given fields of an existing promoCode, field will ignore if it is null
     *
     * @param id the id of the promoCodeDTO to save.
     * @param promoCodeDTO the promoCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promoCodeDTO,
     * or with status {@code 400 (Bad Request)} if the promoCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the promoCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the promoCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/promo-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PromoCodeDTO> partialUpdatePromoCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PromoCodeDTO promoCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PromoCode partially : {}, {}", id, promoCodeDTO);
        if (promoCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promoCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promoCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PromoCodeDTO> result = promoCodeService.partialUpdate(promoCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promoCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /promo-codes} : get all the promoCodes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promoCodes in body.
     */
    @GetMapping("/promo-codes")
    public List<PromoCodeDTO> getAllPromoCodes() {
        log.debug("REST request to get all PromoCodes");
        return promoCodeService.findAll();
    }

    /**
     * {@code GET  /promo-codes/:id} : get the "id" promoCode.
     *
     * @param id the id of the promoCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promoCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promo-codes/{id}")
    public ResponseEntity<PromoCodeDTO> getPromoCode(@PathVariable Long id) {
        log.debug("REST request to get PromoCode : {}", id);
        Optional<PromoCodeDTO> promoCodeDTO = promoCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promoCodeDTO);
    }

    /**
     * {@code DELETE  /promo-codes/:id} : delete the "id" promoCode.
     *
     * @param id the id of the promoCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promo-codes/{id}")
    public ResponseEntity<Void> deletePromoCode(@PathVariable Long id) {
        log.debug("REST request to delete PromoCode : {}", id);
        promoCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
