package asc.foods.store.service.mapper;

import asc.foods.store.domain.Message;
import asc.foods.store.service.dto.MessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring", uses = { RoomMapper.class })
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "room", source = "room", qualifiedByName = "id")
    MessageDTO toDto(Message s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MessageDTO toDtoId(Message message);
}
