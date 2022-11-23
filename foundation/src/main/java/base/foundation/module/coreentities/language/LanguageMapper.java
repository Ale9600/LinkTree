package base.foundation.module.coreentities.language;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LanguageMapper {
    LanguageMapper INSTANCE = Mappers.getMapper(LanguageMapper.class);

    @Mapping(target = "language.id", ignore = true)
    @Mapping(target = "language.insertDate", ignore = true )
    @Mapping(target = "language.lastUpdatedDate", ignore = true)
    Language dtoToEntity(@MappingTarget Language language, LanguageDto languageDto);

    LanguageDto entityToDto(@MappingTarget LanguageDto languageDto, Language language);
}
