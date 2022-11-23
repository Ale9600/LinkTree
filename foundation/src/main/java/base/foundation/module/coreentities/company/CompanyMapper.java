package base.foundation.module.coreentities.company;

import base.foundation.module.coreentities.country.Country;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    @Mapping(target = "company.id", ignore = true)
    @Mapping(target = "company.insertDate", ignore = true )
    @Mapping(target = "company.lastUpdatedDate", ignore = true)
    @Mapping(target = "company.description", source = "companyDto.description")
    @Mapping(target = "company.status", source = "companyDto.status")
    @Mapping(target = "company.code", source = "companyDto.code")
    @Mapping(target = "company.enabled", source = "companyDto.enabled")
    Company dtoToEntity(@MappingTarget Company company, CompanyDto companyDto, Country country);

    CompanyDto entityToDto(@MappingTarget CompanyDto companyDto, Company company);
}
