package asc.foods.user.repository;

import asc.foods.user.domain.UserProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProductRepository extends JpaRepository<UserProduct, Long> {}
