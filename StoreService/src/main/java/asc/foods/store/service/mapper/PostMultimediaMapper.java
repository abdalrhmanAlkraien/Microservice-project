package asc.foods.store.service.mapper;

import asc.foods.store.domain.PostMultimedia;
import asc.foods.store.service.dto.PostMultimediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostMultimedia} and its DTO {@link PostMultimediaDTO}.
 */
@Mapper(componentModel = "spring", uses = { PostMapper.class, CommentMapper.class, StoryMapper.class })
public interface PostMultimediaMapper extends EntityMapper<PostMultimediaDTO, PostMultimedia> {
    @Mapping(target = "post", source = "post", qualifiedByName = "id")
    @Mapping(target = "comment", source = "comment", qualifiedByName = "id")
    @Mapping(target = "story", source = "story", qualifiedByName = "id")
    PostMultimediaDTO toDto(PostMultimedia s);
}
