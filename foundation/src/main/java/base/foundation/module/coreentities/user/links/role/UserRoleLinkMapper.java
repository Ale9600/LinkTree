package base.foundation.module.coreentities.user.links.role;

import base.foundation.module.coreentities.role.Role;
import base.foundation.module.coreentities.user.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRoleLinkMapper {
    UserRoleLinkMapper INSTANCE = Mappers.getMapper(UserRoleLinkMapper.class);

    @Mapping(target = "UserRoleLink.id", ignore = true)
    @Mapping(target = "UserRoleLink.insertDate", ignore = true )
    @Mapping(target = "UserRoleLink.role", source = "role")
    @Mapping(target = "UserRoleLink.user", source = "user")
    @Mapping(target = "UserRoleLink.lastUpdatedDate", ignore = true)
    UserRoleLink mergeToEntity(@MappingTarget UserRoleLink UserRoleLink, Role role, User user, UserRoleLinkDto UserRoleLinkDto);

    @Mapping(target = "UserRoleLinkDto.roleId", source = "UserRoleLink.role.id")
    @Mapping(target = "UserRoleLinkDto.userId", source = "UserRoleLink.user.id")
    @Mapping(target = "UserRoleLinkDto.id", source = "user.id")
    UserRoleLinkDto entityToDto(@MappingTarget UserRoleLinkDto UserRoleLinkDto, UserRoleLink UserRoleLink);
}
