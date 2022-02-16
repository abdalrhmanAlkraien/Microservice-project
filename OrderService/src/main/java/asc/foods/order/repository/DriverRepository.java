package asc.foods.order.repository;

import asc.foods.order.domain.Driver;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Driver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {}
