package asc.foods.store.service.mapper;

import asc.foods.store.domain.ProductTag;
import asc.foods.store.service.dto.ProductTagDTO;
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
