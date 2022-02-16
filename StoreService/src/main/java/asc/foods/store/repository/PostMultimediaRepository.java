package asc.foods.store.repository;

import asc.foods.store.domain.PostMultimedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PostMultimedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostMultimediaRepository extends JpaRepository<PostMultimedia, Long> {}
