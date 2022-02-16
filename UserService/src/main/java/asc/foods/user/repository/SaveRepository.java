package asc.foods.user.repository;

import asc.foods.user.domain.Save;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Save entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {}
