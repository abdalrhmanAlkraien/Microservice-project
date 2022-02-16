package asc.foods.order.repository;

import asc.foods.order.domain.Branch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Branch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {}
