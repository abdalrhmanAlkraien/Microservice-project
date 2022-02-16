package asc.foods.store.service;

import asc.foods.store.domain.ProductBranch;
import asc.foods.store.repository.ProductBranchRepository;
import asc.foods.store.service.dto.ProductBranchDTO;
import asc.foods.store.service.mapper.ProductBranchMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductBranch}.
 */
@Service
@Transactional
public class ProductBranchService {

    private final Logger log = LoggerFactory.getLogger(ProductBranchService.class);

    private final ProductBranchRepository productBranchRepository;

    private final ProductBranchMapper productBranchMapper;

    public ProductBranchService(ProductBranchRepository productBranchRepository, ProductBranchMapper productBranchMapper) {
        this.productBranchRepository = productBranchRepository;
        this.productBranchMapper = productBranchMapper;
    }

    /**
     * Save a productBranch.
     *
     * @param productBranchDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductBranchDTO save(ProductBranchDTO productBranchDTO) {
        log.debug("Request to save ProductBranch : {}", productBranchDTO);
        ProductBranch productBranch = productBranchMapper.toEntity(productBranchDTO);
        productBranch = productBranchRepository.save(productBranch);
        return productBranchMapper.toDto(productBranch);
    }

    /**
     * Partially update a productBranch.
     *
     * @param productBranchDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductBranchDTO> partialUpdate(ProductBranchDTO productBranchDTO) {
        log.debug("Request to partially update ProductBranch : {}", productBranchDTO);

        return productBranchRepository
            .findById(productBranchDTO.getId())
            .map(existingProductBranch -> {
                productBranchMapper.partialUpdate(existingProductBranch, productBranchDTO);

                return existingProductBranch;
            })
            .map(productBranchRepository::save)
            .map(productBranchMapper::toDto);
    }

    /**
     * Get all the productBranches.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductBranchDTO> findAll() {
        log.debug("Request to get all ProductBranches");
        return productBranchRepository.findAll().stream().map(productBranchMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one productBranch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductBranchDTO> findOne(Long id) {
        log.debug("Request to get ProductBranch : {}", id);
        return productBranchRepository.findById(id).map(productBranchMapper::toDto);
    }

    /**
     * Delete the productBranch by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductBranch : {}", id);
        productBranchRepository.deleteById(id);
    }
}
