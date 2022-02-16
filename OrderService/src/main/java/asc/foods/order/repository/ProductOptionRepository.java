package asc.foods.order.repository;

import asc.foods.order.domain.ProductOption;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {}
