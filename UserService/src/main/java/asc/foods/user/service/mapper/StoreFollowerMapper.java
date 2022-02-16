package asc.foods.user.service.mapper;

import asc.foods.user.domain.StoreFollower;
import asc.foods.user.service.dto.StoreFollowerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StoreFollower} and its DTO {@link StoreFollowerDTO}.
 */
@Mapper(componentModel = "spring", uses = { AscStoreMapper.class, AppUserMapper.class })
public interface StoreFollowerMapper extends EntityMapper<StoreFollowerDTO, StoreFollower> {
    @Mapping(target = "store", source = "store", qualifiedByName = "id")
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    StoreFollowerDTO toDto(StoreFollower s);
}
