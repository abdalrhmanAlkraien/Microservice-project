package asc.foods.store.service.mapper;

import asc.foods.store.domain.Driver;
import asc.foods.store.service.dto.DriverDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Driver} and its DTO {@link DriverDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class })
public interface DriverMapper extends EntityMapper<DriverDTO, Driver> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    DriverDTO toDto(Driver s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DriverDTO toDtoId(Driver driver);
}
