package asc.foods.store.service;

import asc.foods.store.domain.UserProduct;
import asc.foods.store.repository.UserProductRepository;
import asc.foods.store.service.dto.UserProductDTO;
import asc.foods.store.service.mapper.UserProductMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserProduct}.
 */
@Service
@Transactional
public class UserProductService {

    private final Logger log = LoggerFactory.getLogger(UserProductService.class);

    private final UserProductRepository userProductRepository;

    private final UserProductMapper userProductMapper;

    public UserProductService(UserProductRepository userProductRepository, UserProductMapper userProductMapper) {
        this.userProductRepository = userProductRepository;
        this.userProductMapper = userProductMapper;
    }

    /**
     * Save a userProduct.
     *
     * @param userProductDTO the entity to save.
     * @return the persisted entity.
     */
    public UserProductDTO save(UserProductDTO userProductDTO) {
        log.debug("Request to save UserProduct : {}", userProductDTO);
        UserProduct userProduct = userProductMapper.toEntity(userProductDTO);
        userProduct = userProductRepository.save(userProduct);
        return userProductMapper.toDto(userProduct);
    }

    /**
     * Partially update a userProduct.
     *
     * @param userProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserProductDTO> partialUpdate(UserProductDTO userProductDTO) {
        log.debug("Request to partially update UserProduct : {}", userProductDTO);

        return userProductRepository
            .findById(userProductDTO.getId())
            .map(existingUserProduct -> {
                userProductMapper.partialUpdate(existingUserProduct, userProductDTO);

                return existingUserProduct;
            })
            .map(userProductRepository::save)
            .map(userProductMapper::toDto);
    }

    /**
     * Get all the userProducts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserProductDTO> findAll() {
        log.debug("Request to get all UserProducts");
        return userProductRepository.findAll().stream().map(userProductMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserProductDTO> findOne(Long id) {
        log.debug("Request to get UserProduct : {}", id);
        return userProductRepository.findById(id).map(userProductMapper::toDto);
    }

    /**
     * Delete the userProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserProduct : {}", id);
        userProductRepository.deleteById(id);
    }
}
