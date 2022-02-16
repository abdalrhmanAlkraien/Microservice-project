package asc.foods.user.repository;

import asc.foods.user.domain.FoodGenre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FoodGenre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodGenreRepository extends JpaRepository<FoodGenre, Long> {}
