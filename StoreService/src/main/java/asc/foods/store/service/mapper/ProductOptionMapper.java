package asc.foods.store.service.mapper;

import asc.foods.store.domain.ProductOption;
import asc.foods.store.service.dto.ProductOptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductOption} and its DTO {@link ProductOptionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface ProductOptionMapper extends EntityMapper<ProductOptionDTO, ProductOption> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    ProductOptionDTO toDto(ProductOption s);
}
