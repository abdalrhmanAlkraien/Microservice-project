package asc.foods.order.repository;

import asc.foods.order.domain.PostMultimedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PostMultimedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostMultimediaRepository extends JpaRepository<PostMultimedia, Long> {}
