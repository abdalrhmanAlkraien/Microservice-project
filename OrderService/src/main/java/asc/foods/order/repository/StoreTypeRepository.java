package asc.foods.order.repository;

import asc.foods.order.domain.StoreType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StoreType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreTypeRepository extends JpaRepository<StoreType, Long> {}
