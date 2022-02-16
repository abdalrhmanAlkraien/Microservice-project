package asc.foods.store.service.mapper;

import asc.foods.store.domain.PromoCode;
import asc.foods.store.service.dto.PromoCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PromoCode} and its DTO {@link PromoCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { AscStoreMapper.class })
public interface PromoCodeMapper extends EntityMapper<PromoCodeDTO, PromoCode> {
    @Mapping(target = "store", source = "store", qualifiedByName = "id")
    PromoCodeDTO toDto(PromoCode s);
}
