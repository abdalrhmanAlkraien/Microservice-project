package asc.foods.user.repository;

import asc.foods.user.domain.MealCustmize;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MealCustmize entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MealCustmizeRepository extends JpaRepository<MealCustmize, Long> {}
