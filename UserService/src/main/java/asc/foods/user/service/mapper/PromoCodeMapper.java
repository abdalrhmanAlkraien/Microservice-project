package asc.foods.user.service.mapper;

import asc.foods.user.domain.PromoCode;
import asc.foods.user.service.dto.PromoCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PromoCode} and its DTO {@link PromoCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { AscStoreMapper.class })
public interface PromoCodeMapper extends EntityMapper<PromoCodeDTO, PromoCode> {
    @Mapping(target = "store", source = "store", qualifiedByName = "id")
    PromoCodeDTO toDto(PromoCode s);
}
