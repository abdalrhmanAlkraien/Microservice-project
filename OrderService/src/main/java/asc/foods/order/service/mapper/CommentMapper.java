package asc.foods.order.service.mapper;

import asc.foods.order.domain.Comment;
import asc.foods.order.service.dto.CommentDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class, PostMapper.class })
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "replays", source = "replays", qualifiedByName = "idSet")
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "post", source = "post", qualifiedByName = "id")
    CommentDTO toDto(Comment s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommentDTO toDtoId(Comment comment);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<CommentDTO> toDtoIdSet(Set<Comment> comment);

    @Mapping(target = "removeReplay", ignore = true)
    Comment toEntity(CommentDTO commentDTO);
}
