package asc.foods.store.repository;

import asc.foods.store.domain.AscOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AscOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AscOrderRepository extends JpaRepository<AscOrder, Long> {}
