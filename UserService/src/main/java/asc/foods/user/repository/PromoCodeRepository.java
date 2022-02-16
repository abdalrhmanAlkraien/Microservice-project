package asc.foods.user.repository;

import asc.foods.user.domain.PromoCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PromoCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {}
