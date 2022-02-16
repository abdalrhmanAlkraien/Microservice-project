package asc.foods.user.service.mapper;

import asc.foods.user.domain.AscOrder;
import asc.foods.user.service.dto.AscOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AscOrder} and its DTO {@link AscOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { BranchMapper.class, UserAddressMapper.class, AppUserMapper.class, DriverMapper.class })
public interface AscOrderMapper extends EntityMapper<AscOrderDTO, AscOrder> {
    @Mapping(target = "store", source = "store", qualifiedByName = "id")
    @Mapping(target = "userAddress", source = "userAddress", qualifiedByName = "id")
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "driver", source = "driver", qualifiedByName = "id")
    AscOrderDTO toDto(AscOrder s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AscOrderDTO toDtoId(AscOrder ascOrder);
}
