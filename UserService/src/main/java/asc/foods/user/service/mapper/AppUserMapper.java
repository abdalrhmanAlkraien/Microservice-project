package asc.foods.user.service.mapper;

import asc.foods.user.domain.AppUser;
import asc.foods.user.service.dto.AppUserDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { ViwedStoryMapper.class })
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "friends", source = "friends", qualifiedByName = "idSet")
    @Mapping(target = "viwedStories", source = "viwedStories", qualifiedByName = "idSet")
    AppUserDTO toDto(AppUser s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoId(AppUser appUser);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<AppUserDTO> toDtoIdSet(Set<AppUser> appUser);

    @Mapping(target = "removeFriends", ignore = true)
    @Mapping(target = "removeViwedStory", ignore = true)
    AppUser toEntity(AppUserDTO appUserDTO);
}
