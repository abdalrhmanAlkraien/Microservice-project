package asc.foods.order.repository;

import asc.foods.order.domain.FoodGenre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FoodGenre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodGenreRepository extends JpaRepository<FoodGenre, Long> {}
