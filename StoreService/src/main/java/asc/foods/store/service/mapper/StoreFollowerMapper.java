package asc.foods.store.service.mapper;

import asc.foods.store.domain.StoreFollower;
import asc.foods.store.service.dto.StoreFollowerDTO;
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
