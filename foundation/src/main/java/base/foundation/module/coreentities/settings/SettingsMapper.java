package base.foundation.module.coreentities.settings;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SettingsMapper {
    SettingsMapper INSTANCE = Mappers.getMapper(SettingsMapper.class);

    @Mapping(target = "settings.id", ignore = true)
    @Mapping(target = "settings.insertDate", ignore = true )
    @Mapping(target = "settings.lastUpdatedDate", ignore = true)
    Settings dtoToEntity(@MappingTarget Settings settings, SettingsDto settingsDto);

    SettingsDto entityToDto(@MappingTarget SettingsDto settingsDto, Settings settings);
}
