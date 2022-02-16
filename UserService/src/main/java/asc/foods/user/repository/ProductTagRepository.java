package asc.foods.user.repository;

import asc.foods.user.domain.ProductTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {}
