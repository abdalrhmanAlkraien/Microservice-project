package asc.foods.user.web.rest;

import asc.foods.user.repository.UserAndRoomRepository;
import asc.foods.user.service.UserAndRoomService;
import asc.foods.user.service.dto.UserAndRoomDTO;
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
 * REST controller for managing {@link asc.foods.user.domain.UserAndRoom}.
 */
@RestController
@RequestMapping("/api")
public class UserAndRoomResource {

    private final Logger log = LoggerFactory.getLogger(UserAndRoomResource.class);

    private static final String ENTITY_NAME = "userServiceUserAndRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAndRoomService userAndRoomService;

    private final UserAndRoomRepository userAndRoomRepository;

    public UserAndRoomResource(UserAndRoomService userAndRoomService, UserAndRoomRepository userAndRoomRepository) {
        this.userAndRoomService = userAndRoomService;
        this.userAndRoomRepository = userAndRoomRepository;
    }

    /**
     * {@code POST  /user-and-rooms} : Create a new userAndRoom.
     *
     * @param userAndRoomDTO the userAndRoomDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAndRoomDTO, or with status {@code 400 (Bad Request)} if the userAndRoom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-and-rooms")
    public ResponseEntity<UserAndRoomDTO> createUserAndRoom(@RequestBody UserAndRoomDTO userAndRoomDTO) throws URISyntaxException {
        log.debug("REST request to save UserAndRoom : {}", userAndRoomDTO);
        if (userAndRoomDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAndRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAndRoomDTO result = userAndRoomService.save(userAndRoomDTO);
        return ResponseEntity
            .created(new URI("/api/user-and-rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-and-rooms/:id} : Updates an existing userAndRoom.
     *
     * @param id the id of the userAndRoomDTO to save.
     * @param userAndRoomDTO the userAndRoomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAndRoomDTO,
     * or with status {@code 400 (Bad Request)} if the userAndRoomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAndRoomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-and-rooms/{id}")
    public ResponseEntity<UserAndRoomDTO> updateUserAndRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAndRoomDTO userAndRoomDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserAndRoom : {}, {}", id, userAndRoomDTO);
        if (userAndRoomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAndRoomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAndRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAndRoomDTO result = userAndRoomService.save(userAndRoomDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAndRoomDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-and-rooms/:id} : Partial updates given fields of an existing userAndRoom, field will ignore if it is null
     *
     * @param id the id of the userAndRoomDTO to save.
     * @param userAndRoomDTO the userAndRoomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAndRoomDTO,
     * or with status {@code 400 (Bad Request)} if the userAndRoomDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAndRoomDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAndRoomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-and-rooms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAndRoomDTO> partialUpdateUserAndRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAndRoomDTO userAndRoomDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAndRoom partially : {}, {}", id, userAndRoomDTO);
        if (userAndRoomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAndRoomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAndRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAndRoomDTO> result = userAndRoomService.partialUpdate(userAndRoomDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAndRoomDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-and-rooms} : get all the userAndRooms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAndRooms in body.
     */
    @GetMapping("/user-and-rooms")
    public List<UserAndRoomDTO> getAllUserAndRooms() {
        log.debug("REST request to get all UserAndRooms");
        return userAndRoomService.findAll();
    }

    /**
     * {@code GET  /user-and-rooms/:id} : get the "id" userAndRoom.
     *
     * @param id the id of the userAndRoomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAndRoomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-and-rooms/{id}")
    public ResponseEntity<UserAndRoomDTO> getUserAndRoom(@PathVariable Long id) {
        log.debug("REST request to get UserAndRoom : {}", id);
        Optional<UserAndRoomDTO> userAndRoomDTO = userAndRoomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAndRoomDTO);
    }

    /**
     * {@code DELETE  /user-and-rooms/:id} : delete the "id" userAndRoom.
     *
     * @param id the id of the userAndRoomDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-and-rooms/{id}")
    public ResponseEntity<Void> deleteUserAndRoom(@PathVariable Long id) {
        log.debug("REST request to delete UserAndRoom : {}", id);
        userAndRoomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
