package asc.foods.order.repository;

import asc.foods.order.domain.StoreFollower;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StoreFollower entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreFollowerRepository extends JpaRepository<StoreFollower, Long> {}
