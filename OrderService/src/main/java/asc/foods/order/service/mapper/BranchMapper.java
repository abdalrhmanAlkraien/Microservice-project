package asc.foods.order.service.mapper;

import asc.foods.order.domain.Branch;
import asc.foods.order.service.dto.BranchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Branch} and its DTO {@link BranchDTO}.
 */
@Mapper(componentModel = "spring", uses = { AscStoreMapper.class })
public interface BranchMapper extends EntityMapper<BranchDTO, Branch> {
    @Mapping(target = "store", source = "store", qualifiedByName = "id")
    BranchDTO toDto(Branch s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BranchDTO toDtoId(Branch branch);
}
