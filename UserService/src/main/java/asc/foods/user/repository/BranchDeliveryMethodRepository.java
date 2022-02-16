package asc.foods.user.repository;

import asc.foods.user.domain.BranchDeliveryMethod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BranchDeliveryMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchDeliveryMethodRepository extends JpaRepository<BranchDeliveryMethod, Long> {}
