package asc.foods.store.service;

import asc.foods.store.domain.OrderCustome;
import asc.foods.store.repository.OrderCustomeRepository;
import asc.foods.store.service.dto.OrderCustomeDTO;
import asc.foods.store.service.mapper.OrderCustomeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderCustome}.
 */
@Service
@Transactional
public class OrderCustomeService {

    private final Logger log = LoggerFactory.getLogger(OrderCustomeService.class);

    private final OrderCustomeRepository orderCustomeRepository;

    private final OrderCustomeMapper orderCustomeMapper;

    public OrderCustomeService(OrderCustomeRepository orderCustomeRepository, OrderCustomeMapper orderCustomeMapper) {
        this.orderCustomeRepository = orderCustomeRepository;
        this.orderCustomeMapper = orderCustomeMapper;
    }

    /**
     * Save a orderCustome.
     *
     * @param orderCustomeDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderCustomeDTO save(OrderCustomeDTO orderCustomeDTO) {
        log.debug("Request to save OrderCustome : {}", orderCustomeDTO);
        OrderCustome orderCustome = orderCustomeMapper.toEntity(orderCustomeDTO);
        orderCustome = orderCustomeRepository.save(orderCustome);
        return orderCustomeMapper.toDto(orderCustome);
    }

    /**
     * Partially update a orderCustome.
     *
     * @param orderCustomeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderCustomeDTO> partialUpdate(OrderCustomeDTO orderCustomeDTO) {
        log.debug("Request to partially update OrderCustome : {}", orderCustomeDTO);

        return orderCustomeRepository
            .findById(orderCustomeDTO.getId())
            .map(existingOrderCustome -> {
                orderCustomeMapper.partialUpdate(existingOrderCustome, orderCustomeDTO);

                return existingOrderCustome;
            })
            .map(orderCustomeRepository::save)
            .map(orderCustomeMapper::toDto);
    }

    /**
     * Get all the orderCustomes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderCustomeDTO> findAll() {
        log.debug("Request to get all OrderCustomes");
        return orderCustomeRepository.findAll().stream().map(orderCustomeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one orderCustome by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderCustomeDTO> findOne(Long id) {
        log.debug("Request to get OrderCustome : {}", id);
        return orderCustomeRepository.findById(id).map(orderCustomeMapper::toDto);
    }

    /**
     * Delete the orderCustome by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderCustome : {}", id);
        orderCustomeRepository.deleteById(id);
    }
}
