package asc.foods.order.service.mapper;

import asc.foods.order.domain.Post;
import asc.foods.order.service.dto.PostDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Post} and its DTO {@link PostDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class })
public interface PostMapper extends EntityMapper<PostDTO, Post> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    PostDTO toDto(Post s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostDTO toDtoId(Post post);
}
