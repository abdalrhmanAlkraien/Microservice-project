package asc.foods.order.repository;

import asc.foods.order.domain.Story;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Story entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {}
