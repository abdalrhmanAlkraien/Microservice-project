package asc.foods.order.service.mapper;

import asc.foods.order.domain.PromoCode;
import asc.foods.order.service.dto.PromoCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PromoCode} and its DTO {@link PromoCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { AscStoreMapper.class })
public interface PromoCodeMapper extends EntityMapper<PromoCodeDTO, PromoCode> {
    @Mapping(target = "store", source = "store", qualifiedByName = "id")
    PromoCodeDTO toDto(PromoCode s);
}
