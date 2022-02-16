package asc.foods.user.web.rest;

import asc.foods.user.repository.ItemTypeRepository;
import asc.foods.user.service.ItemTypeService;
import asc.foods.user.service.dto.ItemTypeDTO;
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
 * REST controller for managing {@link asc.foods.user.domain.ItemType}.
 */
@RestController
@RequestMapping("/api")
public class ItemTypeResource {

    private final Logger log = LoggerFactory.getLogger(ItemTypeResource.class);

    private static final String ENTITY_NAME = "userServiceItemType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemTypeService itemTypeService;

    private final ItemTypeRepository itemTypeRepository;

    public ItemTypeResource(ItemTypeService itemTypeService, ItemTypeRepository itemTypeRepository) {
        this.itemTypeService = itemTypeService;
        this.itemTypeRepository = itemTypeRepository;
    }

    /**
     * {@code POST  /item-types} : Create a new itemType.
     *
     * @param itemTypeDTO the itemTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemTypeDTO, or with status {@code 400 (Bad Request)} if the itemType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-types")
    public ResponseEntity<ItemTypeDTO> createItemType(@RequestBody ItemTypeDTO itemTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ItemType : {}", itemTypeDTO);
        if (itemTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemTypeDTO result = itemTypeService.save(itemTypeDTO);
        return ResponseEntity
            .created(new URI("/api/item-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-types/:id} : Updates an existing itemType.
     *
     * @param id the id of the itemTypeDTO to save.
     * @param itemTypeDTO the itemTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemTypeDTO,
     * or with status {@code 400 (Bad Request)} if the itemTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-types/{id}")
    public ResponseEntity<ItemTypeDTO> updateItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemTypeDTO itemTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemType : {}, {}", id, itemTypeDTO);
        if (itemTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemTypeDTO result = itemTypeService.save(itemTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-types/:id} : Partial updates given fields of an existing itemType, field will ignore if it is null
     *
     * @param id the id of the itemTypeDTO to save.
     * @param itemTypeDTO the itemTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemTypeDTO,
     * or with status {@code 400 (Bad Request)} if the itemTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemTypeDTO> partialUpdateItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemTypeDTO itemTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemType partially : {}, {}", id, itemTypeDTO);
        if (itemTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemTypeDTO> result = itemTypeService.partialUpdate(itemTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-types} : get all the itemTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemTypes in body.
     */
    @GetMapping("/item-types")
    public List<ItemTypeDTO> getAllItemTypes() {
        log.debug("REST request to get all ItemTypes");
        return itemTypeService.findAll();
    }

    /**
     * {@code GET  /item-types/:id} : get the "id" itemType.
     *
     * @param id the id of the itemTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-types/{id}")
    public ResponseEntity<ItemTypeDTO> getItemType(@PathVariable Long id) {
        log.debug("REST request to get ItemType : {}", id);
        Optional<ItemTypeDTO> itemTypeDTO = itemTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemTypeDTO);
    }

    /**
     * {@code DELETE  /item-types/:id} : delete the "id" itemType.
     *
     * @param id the id of the itemTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-types/{id}")
    public ResponseEntity<Void> deleteItemType(@PathVariable Long id) {
        log.debug("REST request to delete ItemType : {}", id);
        itemTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
