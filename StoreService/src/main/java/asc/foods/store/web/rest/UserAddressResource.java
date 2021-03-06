package asc.foods.store.web.rest;

import asc.foods.store.repository.UserAddressRepository;
import asc.foods.store.service.UserAddressService;
import asc.foods.store.service.dto.UserAddressDTO;
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
 * REST controller for managing {@link asc.foods.store.domain.UserAddress}.
 */
@RestController
@RequestMapping("/api")
public class UserAddressResource {

    private final Logger log = LoggerFactory.getLogger(UserAddressResource.class);

    private static final String ENTITY_NAME = "storeServiceUserAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAddressService userAddressService;

    private final UserAddressRepository userAddressRepository;

    public UserAddressResource(UserAddressService userAddressService, UserAddressRepository userAddressRepository) {
        this.userAddressService = userAddressService;
        this.userAddressRepository = userAddressRepository;
    }

    /**
     * {@code POST  /user-addresses} : Create a new userAddress.
     *
     * @param userAddressDTO the userAddressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAddressDTO, or with status {@code 400 (Bad Request)} if the userAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-addresses")
    public ResponseEntity<UserAddressDTO> createUserAddress(@RequestBody UserAddressDTO userAddressDTO) throws URISyntaxException {
        log.debug("REST request to save UserAddress : {}", userAddressDTO);
        if (userAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAddressDTO result = userAddressService.save(userAddressDTO);
        return ResponseEntity
            .created(new URI("/api/user-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-addresses/:id} : Updates an existing userAddress.
     *
     * @param id the id of the userAddressDTO to save.
     * @param userAddressDTO the userAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAddressDTO,
     * or with status {@code 400 (Bad Request)} if the userAddressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-addresses/{id}")
    public ResponseEntity<UserAddressDTO> updateUserAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAddressDTO userAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserAddress : {}, {}", id, userAddressDTO);
        if (userAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAddressDTO result = userAddressService.save(userAddressDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-addresses/:id} : Partial updates given fields of an existing userAddress, field will ignore if it is null
     *
     * @param id the id of the userAddressDTO to save.
     * @param userAddressDTO the userAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAddressDTO,
     * or with status {@code 400 (Bad Request)} if the userAddressDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAddressDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-addresses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAddressDTO> partialUpdateUserAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAddressDTO userAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAddress partially : {}, {}", id, userAddressDTO);
        if (userAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAddressDTO> result = userAddressService.partialUpdate(userAddressDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAddressDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-addresses} : get all the userAddresses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAddresses in body.
     */
    @GetMapping("/user-addresses")
    public List<UserAddressDTO> getAllUserAddresses() {
        log.debug("REST request to get all UserAddresses");
        return userAddressService.findAll();
    }

    /**
     * {@code GET  /user-addresses/:id} : get the "id" userAddress.
     *
     * @param id the id of the userAddressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAddressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-addresses/{id}")
    public ResponseEntity<UserAddressDTO> getUserAddress(@PathVariable Long id) {
        log.debug("REST request to get UserAddress : {}", id);
        Optional<UserAddressDTO> userAddressDTO = userAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAddressDTO);
    }

    /**
     * {@code DELETE  /user-addresses/:id} : delete the "id" userAddress.
     *
     * @param id the id of the userAddressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-addresses/{id}")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable Long id) {
        log.debug("REST request to delete UserAddress : {}", id);
        userAddressService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
