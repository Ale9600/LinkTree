package base.foundation.module.coreentities.translation;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TranslationMapper {
    TranslationMapper INSTANCE = Mappers.getMapper(TranslationMapper.class);

    @Mapping(target = "tabGen.id", ignore = true)
    @Mapping(target = "tabGen.insertDate", ignore = true )
    @Mapping(target = "tabGen.lastUpdatedDate", ignore = true)
    Translation dtoToEntity(@MappingTarget Translation translation, TranslationDto tabGenDto);

    TranslationDto entityToDto(@MappingTarget TranslationDto tabGenDto, Translation translation);
}
