package asc.foods.store.repository;

import asc.foods.store.domain.OrderCustome;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderCustome entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderCustomeRepository extends JpaRepository<OrderCustome, Long> {}
