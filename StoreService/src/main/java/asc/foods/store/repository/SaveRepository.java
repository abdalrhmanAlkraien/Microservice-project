package asc.foods.store.repository;

import asc.foods.store.domain.Save;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Save entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {}
