package asc.foods.order.repository;

import asc.foods.order.domain.ViwedStory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ViwedStory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ViwedStoryRepository extends JpaRepository<ViwedStory, Long> {}
