package asc.foods.user.service.mapper;

import asc.foods.user.domain.ProductBranch;
import asc.foods.user.service.dto.ProductBranchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductBranch} and its DTO {@link ProductBranchDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, BranchMapper.class })
public interface ProductBranchMapper extends EntityMapper<ProductBranchDTO, ProductBranch> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    @Mapping(target = "branch", source = "branch", qualifiedByName = "id")
    ProductBranchDTO toDto(ProductBranch s);
}
