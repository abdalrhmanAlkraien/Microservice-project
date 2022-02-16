package asc.foods.order.repository;

import asc.foods.order.domain.UserAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {}
