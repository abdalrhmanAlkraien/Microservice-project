package asc.foods.order.service.mapper;

import asc.foods.order.domain.LikeComment;
import asc.foods.order.service.dto.LikeCommentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeComment} and its DTO {@link LikeCommentDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class, CommentMapper.class })
public interface LikeCommentMapper extends EntityMapper<LikeCommentDTO, LikeComment> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "comment", source = "comment", qualifiedByName = "id")
    LikeCommentDTO toDto(LikeComment s);
}
