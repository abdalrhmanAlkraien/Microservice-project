package asc.foods.store.repository;

import asc.foods.store.domain.ProductBranch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductBranch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductBranchRepository extends JpaRepository<ProductBranch, Long> {}
