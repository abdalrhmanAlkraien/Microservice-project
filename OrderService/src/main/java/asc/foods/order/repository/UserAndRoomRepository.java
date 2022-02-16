package asc.foods.order.repository;

import asc.foods.order.domain.UserAndRoom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserAndRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAndRoomRepository extends JpaRepository<UserAndRoom, Long> {}
