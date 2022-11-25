package core.md.module.page;

import base.foundation.module.coreentities.country.Country;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PageMapper {
    PageMapper INSTANCE = Mappers.getMapper(PageMapper.class);

    @Mapping(target = "page.id", ignore = true)
    @Mapping(target = "page.insertDate", ignore = true )
    @Mapping(target = "page.lastUpdatedDate", ignore = true)
    @Mapping(target = "page.description", source = "pageDto.description")
    @Mapping(target = "page.status", source = "pageDto.status")
    @Mapping(target = "page.code", source = "pageDto.code")
    @Mapping(target = "page.enabled", source = "pageDto.enabled")
    Page dtoToEntity(@MappingTarget Page page, PageDto pageDto, Country country);

    PageDto entityToDto(@MappingTarget PageDto pageDto, Page page);
}
