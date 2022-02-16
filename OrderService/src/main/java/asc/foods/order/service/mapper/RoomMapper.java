package asc.foods.order.service.mapper;

import asc.foods.order.domain.Room;
import asc.foods.order.service.dto.RoomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Room} and its DTO {@link RoomDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoomDTO toDtoId(Room room);
}
