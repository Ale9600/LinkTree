package base.foundation.module.coreentities.user;

import base.foundation.module.coreentities.company.Company;
import base.foundation.module.coreentities.country.Country;
import base.foundation.module.coreentities.language.Language;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "user.insertDate", ignore = true )
    @Mapping(target = "user.lastUpdatedDate", ignore = true)
    @Mapping(target = "user.password", ignore = true)
    @Mapping(target = "user.language", source = "language")
    @Mapping(target = "user.email", source = "userDto.email")
    @Mapping(target = "user.status", source = "userDto.status")
    @Mapping(target = "user.code", source = "userDto.code")
    @Mapping(target = "user.enabled", source = "userDto.enabled")
    User dtoToEntity(@MappingTarget User user, UserDto userDto, Language language, Company company, Country country);

    @Mapping(target = "userDto.password", ignore = true)
    UserDto entityToDto(@MappingTarget UserDto userDto, User user);
}
