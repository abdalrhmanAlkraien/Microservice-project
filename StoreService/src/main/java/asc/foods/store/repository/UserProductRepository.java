package asc.foods.store.repository;

import asc.foods.store.domain.UserProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProductRepository extends JpaRepository<UserProduct, Long> {}
