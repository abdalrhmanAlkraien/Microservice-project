package asc.foods.user.service.mapper;

import asc.foods.user.domain.ProductTag;
import asc.foods.user.service.dto.ProductTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductTag} and its DTO {@link ProductTagDTO}.
 */
@Mapper(componentModel = "spring", uses = { AscStoreMapper.class })
public interface ProductTagMapper extends EntityMapper<ProductTagDTO, ProductTag> {
    @Mapping(target = "store", source = "store", qualifiedByName = "id")
    ProductTagDTO toDto(ProductTag s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductTagDTO toDtoId(ProductTag productTag);
}
