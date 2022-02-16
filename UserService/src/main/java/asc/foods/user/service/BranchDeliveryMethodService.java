package asc.foods.user.service;

import asc.foods.user.domain.BranchDeliveryMethod;
import asc.foods.user.repository.BranchDeliveryMethodRepository;
import asc.foods.user.service.dto.BranchDeliveryMethodDTO;
import asc.foods.user.service.mapper.BranchDeliveryMethodMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BranchDeliveryMethod}.
 */
@Service
@Transactional
public class BranchDeliveryMethodService {

    private final Logger log = LoggerFactory.getLogger(BranchDeliveryMethodService.class);

    private final BranchDeliveryMethodRepository branchDeliveryMethodRepository;

    private final BranchDeliveryMethodMapper branchDeliveryMethodMapper;

    public BranchDeliveryMethodService(
        BranchDeliveryMethodRepository branchDeliveryMethodRepository,
        BranchDeliveryMethodMapper branchDeliveryMethodMapper
    ) {
        this.branchDeliveryMethodRepository = branchDeliveryMethodRepository;
        this.branchDeliveryMethodMapper = branchDeliveryMethodMapper;
    }

    /**
     * Save a branchDeliveryMethod.
     *
     * @param branchDeliveryMethodDTO the entity to save.
     * @return the persisted entity.
     */
    public BranchDeliveryMethodDTO save(BranchDeliveryMethodDTO branchDeliveryMethodDTO) {
        log.debug("Request to save BranchDeliveryMethod : {}", branchDeliveryMethodDTO);
        BranchDeliveryMethod branchDeliveryMethod = branchDeliveryMethodMapper.toEntity(branchDeliveryMethodDTO);
        branchDeliveryMethod = branchDeliveryMethodRepository.save(branchDeliveryMethod);
        return branchDeliveryMethodMapper.toDto(branchDeliveryMethod);
    }

    /**
     * Partially update a branchDeliveryMethod.
     *
     * @param branchDeliveryMethodDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BranchDeliveryMethodDTO> partialUpdate(BranchDeliveryMethodDTO branchDeliveryMethodDTO) {
        log.debug("Request to partially update BranchDeliveryMethod : {}", branchDeliveryMethodDTO);

        return branchDeliveryMethodRepository
            .findById(branchDeliveryMethodDTO.getId())
            .map(existingBranchDeliveryMethod -> {
                branchDeliveryMethodMapper.partialUpdate(existingBranchDeliveryMethod, branchDeliveryMethodDTO);

                return existingBranchDeliveryMethod;
            })
            .map(branchDeliveryMethodRepository::save)
            .map(branchDeliveryMethodMapper::toDto);
    }

    /**
     * Get all the branchDeliveryMethods.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BranchDeliveryMethodDTO> findAll() {
        log.debug("Request to get all BranchDeliveryMethods");
        return branchDeliveryMethodRepository
            .findAll()
            .stream()
            .map(branchDeliveryMethodMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one branchDeliveryMethod by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BranchDeliveryMethodDTO> findOne(Long id) {
        log.debug("Request to get BranchDeliveryMethod : {}", id);
        return branchDeliveryMethodRepository.findById(id).map(branchDeliveryMethodMapper::toDto);
    }

    /**
     * Delete the branchDeliveryMethod by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BranchDeliveryMethod : {}", id);
        branchDeliveryMethodRepository.deleteById(id);
    }
}
