package asc.foods.user.repository;

import asc.foods.user.domain.PostMultimedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PostMultimedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostMultimediaRepository extends JpaRepository<PostMultimedia, Long> {}
