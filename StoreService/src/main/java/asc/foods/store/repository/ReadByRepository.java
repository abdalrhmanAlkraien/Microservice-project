package asc.foods.store.repository;

import asc.foods.store.domain.ReadBy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReadBy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReadByRepository extends JpaRepository<ReadBy, Long> {}
