package asc.foods.store.repository;

import asc.foods.store.domain.OrderStatusHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderStatusHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {}
