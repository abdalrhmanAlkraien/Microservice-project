package asc.foods.user.service.mapper;

import asc.foods.user.domain.UserAndRoom;
import asc.foods.user.service.dto.UserAndRoomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAndRoom} and its DTO {@link UserAndRoomDTO}.
 */
@Mapper(componentModel = "spring", uses = { RoomMapper.class, AppUserMapper.class })
public interface UserAndRoomMapper extends EntityMapper<UserAndRoomDTO, UserAndRoom> {
    @Mapping(target = "room", source = "room", qualifiedByName = "id")
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    UserAndRoomDTO toDto(UserAndRoom s);
}
