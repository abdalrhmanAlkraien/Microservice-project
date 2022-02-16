package asc.foods.store.service;

import asc.foods.store.domain.ProductOption;
import asc.foods.store.repository.ProductOptionRepository;
import asc.foods.store.service.dto.ProductOptionDTO;
import asc.foods.store.service.mapper.ProductOptionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductOption}.
 */
@Service
@Transactional
public class ProductOptionService {

    private final Logger log = LoggerFactory.getLogger(ProductOptionService.class);

    private final ProductOptionRepository productOptionRepository;

    private final ProductOptionMapper productOptionMapper;

    public ProductOptionService(ProductOptionRepository productOptionRepository, ProductOptionMapper productOptionMapper) {
        this.productOptionRepository = productOptionRepository;
        this.productOptionMapper = productOptionMapper;
    }

    /**
     * Save a productOption.
     *
     * @param productOptionDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductOptionDTO save(ProductOptionDTO productOptionDTO) {
        log.debug("Request to save ProductOption : {}", productOptionDTO);
        ProductOption productOption = productOptionMapper.toEntity(productOptionDTO);
        productOption = productOptionRepository.save(productOption);
        return productOptionMapper.toDto(productOption);
    }

    /**
     * Partially update a productOption.
     *
     * @param productOptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductOptionDTO> partialUpdate(ProductOptionDTO productOptionDTO) {
        log.debug("Request to partially update ProductOption : {}", productOptionDTO);

        return productOptionRepository
            .findById(productOptionDTO.getId())
            .map(existingProductOption -> {
                productOptionMapper.partialUpdate(existingProductOption, productOptionDTO);

                return existingProductOption;
            })
            .map(productOptionRepository::save)
            .map(productOptionMapper::toDto);
    }

    /**
     * Get all the productOptions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductOptionDTO> findAll() {
        log.debug("Request to get all ProductOptions");
        return productOptionRepository.findAll().stream().map(productOptionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one productOption by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductOptionDTO> findOne(Long id) {
        log.debug("Request to get ProductOption : {}", id);
        return productOptionRepository.findById(id).map(productOptionMapper::toDto);
    }

    /**
     * Delete the productOption by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductOption : {}", id);
        productOptionRepository.deleteById(id);
    }
}
