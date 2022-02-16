package asc.foods.user.repository;

import asc.foods.user.domain.AscOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AscOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AscOrderRepository extends JpaRepository<AscOrder, Long> {}
