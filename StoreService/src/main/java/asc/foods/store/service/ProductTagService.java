package asc.foods.store.service;

import asc.foods.store.domain.ProductTag;
import asc.foods.store.repository.ProductTagRepository;
import asc.foods.store.service.dto.ProductTagDTO;
import asc.foods.store.service.mapper.ProductTagMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductTag}.
 */
@Service
@Transactional
public class ProductTagService {

    private final Logger log = LoggerFactory.getLogger(ProductTagService.class);

    private final ProductTagRepository productTagRepository;

    private final ProductTagMapper productTagMapper;

    public ProductTagService(ProductTagRepository productTagRepository, ProductTagMapper productTagMapper) {
        this.productTagRepository = productTagRepository;
        this.productTagMapper = productTagMapper;
    }

    /**
     * Save a productTag.
     *
     * @param productTagDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductTagDTO save(ProductTagDTO productTagDTO) {
        log.debug("Request to save ProductTag : {}", productTagDTO);
        ProductTag productTag = productTagMapper.toEntity(productTagDTO);
        productTag = productTagRepository.save(productTag);
        return productTagMapper.toDto(productTag);
    }

    /**
     * Partially update a productTag.
     *
     * @param productTagDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductTagDTO> partialUpdate(ProductTagDTO productTagDTO) {
        log.debug("Request to partially update ProductTag : {}", productTagDTO);

        return productTagRepository
            .findById(productTagDTO.getId())
            .map(existingProductTag -> {
                productTagMapper.partialUpdate(existingProductTag, productTagDTO);

                return existingProductTag;
            })
            .map(productTagRepository::save)
            .map(productTagMapper::toDto);
    }

    /**
     * Get all the productTags.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductTagDTO> findAll() {
        log.debug("Request to get all ProductTags");
        return productTagRepository.findAll().stream().map(productTagMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one productTag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductTagDTO> findOne(Long id) {
        log.debug("Request to get ProductTag : {}", id);
        return productTagRepository.findById(id).map(productTagMapper::toDto);
    }

    /**
     * Delete the productTag by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductTag : {}", id);
        productTagRepository.deleteById(id);
    }
}
