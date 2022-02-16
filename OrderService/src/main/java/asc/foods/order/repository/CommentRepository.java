package asc.foods.order.repository;

import asc.foods.order.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Comment entity.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(
        value = "select distinct comment from Comment comment left join fetch comment.replays",
        countQuery = "select count(distinct comment) from Comment comment"
    )
    Page<Comment> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct comment from Comment comment left join fetch comment.replays")
    List<Comment> findAllWithEagerRelationships();

    @Query("select comment from Comment comment left join fetch comment.replays where comment.id =:id")
    Optional<Comment> findOneWithEagerRelationships(@Param("id") Long id);
}
