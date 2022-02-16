package asc.foods.store.repository;

import asc.foods.store.domain.AscStore;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AscStore entity.
 */
@Repository
public interface AscStoreRepository extends JpaRepository<AscStore, Long> {
    @Query(
        value = "select distinct ascStore from AscStore ascStore left join fetch ascStore.foodGeners",
        countQuery = "select count(distinct ascStore) from AscStore ascStore"
    )
    Page<AscStore> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct ascStore from AscStore ascStore left join fetch ascStore.foodGeners")
    List<AscStore> findAllWithEagerRelationships();

    @Query("select ascStore from AscStore ascStore left join fetch ascStore.foodGeners where ascStore.id =:id")
    Optional<AscStore> findOneWithEagerRelationships(@Param("id") Long id);
}
