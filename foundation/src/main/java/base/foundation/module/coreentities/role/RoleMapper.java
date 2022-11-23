package base.foundation.module.coreentities.role;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "role.id", ignore = true)
    @Mapping(target = "role.insertDate", ignore = true )
    @Mapping(target = "role.lastUpdatedDate", ignore = true)
    Role dtoToEntity(@MappingTarget Role role, RoleDto roleDto);

    RoleDto entityToDto(@MappingTarget RoleDto roleDto, Role role);
}
