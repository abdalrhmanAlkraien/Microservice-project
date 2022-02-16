package asc.foods.order.repository;

import asc.foods.order.domain.Room;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Room entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {}
