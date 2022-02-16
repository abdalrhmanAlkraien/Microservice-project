package asc.foods.user.service.mapper;

import asc.foods.user.domain.UserProduct;
import asc.foods.user.service.dto.UserProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProduct} and its DTO {@link UserProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class, ProductMapper.class })
public interface UserProductMapper extends EntityMapper<UserProductDTO, UserProduct> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    UserProductDTO toDto(UserProduct s);
}
