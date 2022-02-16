package asc.foods.user.service.mapper;

import asc.foods.user.domain.BranchDeliveryMethod;
import asc.foods.user.service.dto.BranchDeliveryMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchDeliveryMethod} and its DTO {@link BranchDeliveryMethodDTO}.
 */
@Mapper(componentModel = "spring", uses = { BranchMapper.class })
public interface BranchDeliveryMethodMapper extends EntityMapper<BranchDeliveryMethodDTO, BranchDeliveryMethod> {
    @Mapping(target = "branch", source = "branch", qualifiedByName = "id")
    BranchDeliveryMethodDTO toDto(BranchDeliveryMethod s);
}
