package asc.foods.store.repository;

import asc.foods.store.domain.LikePost;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LikePost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {}
