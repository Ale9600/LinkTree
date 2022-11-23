package base.foundation.module.coreentities.tabgen;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TabGenMapper {
    TabGenMapper INSTANCE = Mappers.getMapper(TabGenMapper.class);

    @Mapping(target = "tabGen.id", ignore = true)
    @Mapping(target = "tabGen.insertDate", ignore = true )
    @Mapping(target = "tabGen.lastUpdatedDate", ignore = true)
    TabGen dtoToEntity(@MappingTarget TabGen tabGen, TabGenDto tabGenDto);

    TabGenDto entityToDto(@MappingTarget TabGenDto tabGenDto, TabGen tabGen);
}
