package asc.foods.order.repository;

import asc.foods.order.domain.Save;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Save entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {}
