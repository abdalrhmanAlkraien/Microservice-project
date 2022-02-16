package asc.foods.store.repository;

import asc.foods.store.domain.MealCustmize;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MealCustmize entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MealCustmizeRepository extends JpaRepository<MealCustmize, Long> {}
