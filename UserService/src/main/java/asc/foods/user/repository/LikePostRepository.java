package asc.foods.user.repository;

import asc.foods.user.domain.LikePost;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LikePost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {}
