package base.foundation.module.coreentities.country;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CountryMapper {
    CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);

    @Mapping(target = "country.id", ignore = true)
    @Mapping(target = "country.insertDate", ignore = true )
    @Mapping(target = "country.lastUpdatedDate", ignore = true)
    Country dtoToEntity(@MappingTarget Country country, CountryDto countryDto);

    CountryDto entityToDto(@MappingTarget CountryDto countryDto, Country country);
}
