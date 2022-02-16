package asc.foods.store.repository;

import asc.foods.store.domain.ProductTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {}
