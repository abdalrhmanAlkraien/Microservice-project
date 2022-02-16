package asc.foods.user.service.mapper;

import asc.foods.user.domain.Product;
import asc.foods.user.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { AscStoreMapper.class, FoodGenreMapper.class, ProductTagMapper.class })
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "store", source = "store", qualifiedByName = "id")
    @Mapping(target = "foodGenre", source = "foodGenre", qualifiedByName = "id")
    @Mapping(target = "productTag", source = "productTag", qualifiedByName = "id")
    ProductDTO toDto(Product s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoId(Product product);
}
