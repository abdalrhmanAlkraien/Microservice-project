package asc.foods.user.service;

import asc.foods.user.domain.OrderStatusHistory;
import asc.foods.user.repository.OrderStatusHistoryRepository;
import asc.foods.user.service.dto.OrderStatusHistoryDTO;
import asc.foods.user.service.mapper.OrderStatusHistoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderStatusHistory}.
 */
@Service
@Transactional
public class OrderStatusHistoryService {

    private final Logger log = LoggerFactory.getLogger(OrderStatusHistoryService.class);

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    private final OrderStatusHistoryMapper orderStatusHistoryMapper;

    public OrderStatusHistoryService(
        OrderStatusHistoryRepository orderStatusHistoryRepository,
        OrderStatusHistoryMapper orderStatusHistoryMapper
    ) {
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
        this.orderStatusHistoryMapper = orderStatusHistoryMapper;
    }

    /**
     * Save a orderStatusHistory.
     *
     * @param orderStatusHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderStatusHistoryDTO save(OrderStatusHistoryDTO orderStatusHistoryDTO) {
        log.debug("Request to save OrderStatusHistory : {}", orderStatusHistoryDTO);
        OrderStatusHistory orderStatusHistory = orderStatusHistoryMapper.toEntity(orderStatusHistoryDTO);
        orderStatusHistory = orderStatusHistoryRepository.save(orderStatusHistory);
        return orderStatusHistoryMapper.toDto(orderStatusHistory);
    }

    /**
     * Partially update a orderStatusHistory.
     *
     * @param orderStatusHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderStatusHistoryDTO> partialUpdate(OrderStatusHistoryDTO orderStatusHistoryDTO) {
        log.debug("Request to partially update OrderStatusHistory : {}", orderStatusHistoryDTO);

        return orderStatusHistoryRepository
            .findById(orderStatusHistoryDTO.getId())
            .map(existingOrderStatusHistory -> {
                orderStatusHistoryMapper.partialUpdate(existingOrderStatusHistory, orderStatusHistoryDTO);

                return existingOrderStatusHistory;
            })
            .map(orderStatusHistoryRepository::save)
            .map(orderStatusHistoryMapper::toDto);
    }

    /**
     * Get all the orderStatusHistories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderStatusHistoryDTO> findAll() {
        log.debug("Request to get all OrderStatusHistories");
        return orderStatusHistoryRepository
            .findAll()
            .stream()
            .map(orderStatusHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one orderStatusHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderStatusHistoryDTO> findOne(Long id) {
        log.debug("Request to get OrderStatusHistory : {}", id);
        return orderStatusHistoryRepository.findById(id).map(orderStatusHistoryMapper::toDto);
    }

    /**
     * Delete the orderStatusHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderStatusHistory : {}", id);
        orderStatusHistoryRepository.deleteById(id);
    }
}
