package asc.foods.order.service;

import asc.foods.order.domain.AscOrder;
import asc.foods.order.repository.AscOrderRepository;
import asc.foods.order.service.dto.AscOrderDTO;
import asc.foods.order.service.mapper.AscOrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AscOrder}.
 */
@Service
@Transactional
public class AscOrderService {

    private final Logger log = LoggerFactory.getLogger(AscOrderService.class);

    private final AscOrderRepository ascOrderRepository;

    private final AscOrderMapper ascOrderMapper;

    public AscOrderService(AscOrderRepository ascOrderRepository, AscOrderMapper ascOrderMapper) {
        this.ascOrderRepository = ascOrderRepository;
        this.ascOrderMapper = ascOrderMapper;
    }

    /**
     * Save a ascOrder.
     *
     * @param ascOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public AscOrderDTO save(AscOrderDTO ascOrderDTO) {
        log.debug("Request to save AscOrder : {}", ascOrderDTO);
        AscOrder ascOrder = ascOrderMapper.toEntity(ascOrderDTO);
        ascOrder = ascOrderRepository.save(ascOrder);
        return ascOrderMapper.toDto(ascOrder);
    }

    /**
     * Partially update a ascOrder.
     *
     * @param ascOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AscOrderDTO> partialUpdate(AscOrderDTO ascOrderDTO) {
        log.debug("Request to partially update AscOrder : {}", ascOrderDTO);

        return ascOrderRepository
            .findById(ascOrderDTO.getId())
            .map(existingAscOrder -> {
                ascOrderMapper.partialUpdate(existingAscOrder, ascOrderDTO);

                return existingAscOrder;
            })
            .map(ascOrderRepository::save)
            .map(ascOrderMapper::toDto);
    }

    /**
     * Get all the ascOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AscOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AscOrders");
        return ascOrderRepository.findAll(pageable).map(ascOrderMapper::toDto);
    }

    /**
     * Get one ascOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AscOrderDTO> findOne(Long id) {
        log.debug("Request to get AscOrder : {}", id);
        return ascOrderRepository.findById(id).map(ascOrderMapper::toDto);
    }

    /**
     * Delete the ascOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AscOrder : {}", id);
        ascOrderRepository.deleteById(id);
    }
}
