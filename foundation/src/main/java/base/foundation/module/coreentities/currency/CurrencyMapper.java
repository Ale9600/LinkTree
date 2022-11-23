package base.foundation.module.coreentities.currency;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mapping(target = "currency.id", ignore = true)
    @Mapping(target = "currency.insertDate", ignore = true )
    @Mapping(target = "currency.lastUpdatedDate", ignore = true)
    Currency dtoToEntity(@MappingTarget Currency currency, CurrencyDto currencyDto);

    CurrencyDto entityToDto(@MappingTarget CurrencyDto currencyDto, Currency currency);
}
