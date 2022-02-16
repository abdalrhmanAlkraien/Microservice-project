package asc.foods.order.repository;

import asc.foods.order.domain.LikeComment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LikeComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {}
