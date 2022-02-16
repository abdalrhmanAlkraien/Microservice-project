package asc.foods.store.service;

import asc.foods.store.domain.OrderProduct;
import asc.foods.store.repository.OrderProductRepository;
import asc.foods.store.service.dto.OrderProductDTO;
import asc.foods.store.service.mapper.OrderProductMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderProduct}.
 */
@Service
@Transactional
public class OrderProductService {

    private final Logger log = LoggerFactory.getLogger(OrderProductService.class);

    private final OrderProductRepository orderProductRepository;

    private final OrderProductMapper orderProductMapper;

    public OrderProductService(OrderProductRepository orderProductRepository, OrderProductMapper orderProductMapper) {
        this.orderProductRepository = orderProductRepository;
        this.orderProductMapper = orderProductMapper;
    }

    /**
     * Save a orderProduct.
     *
     * @param orderProductDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderProductDTO save(OrderProductDTO orderProductDTO) {
        log.debug("Request to save OrderProduct : {}", orderProductDTO);
        OrderProduct orderProduct = orderProductMapper.toEntity(orderProductDTO);
        orderProduct = orderProductRepository.save(orderProduct);
        return orderProductMapper.toDto(orderProduct);
    }

    /**
     * Partially update a orderProduct.
     *
     * @param orderProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderProductDTO> partialUpdate(OrderProductDTO orderProductDTO) {
        log.debug("Request to partially update OrderProduct : {}", orderProductDTO);

        return orderProductRepository
            .findById(orderProductDTO.getId())
            .map(existingOrderProduct -> {
                orderProductMapper.partialUpdate(existingOrderProduct, orderProductDTO);

                return existingOrderProduct;
            })
            .map(orderProductRepository::save)
            .map(orderProductMapper::toDto);
    }

    /**
     * Get all the orderProducts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderProductDTO> findAll() {
        log.debug("Request to get all OrderProducts");
        return orderProductRepository.findAll().stream().map(orderProductMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one orderProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderProductDTO> findOne(Long id) {
        log.debug("Request to get OrderProduct : {}", id);
        return orderProductRepository.findById(id).map(orderProductMapper::toDto);
    }

    /**
     * Delete the orderProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderProduct : {}", id);
        orderProductRepository.deleteById(id);
    }
}
