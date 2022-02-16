package asc.foods.user.repository;

import asc.foods.user.domain.UserAndRoom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserAndRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAndRoomRepository extends JpaRepository<UserAndRoom, Long> {}
