package base.foundation.module.coreentities.currency;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    Optional<Currency> findByCode(String pCode);
    Optional<Currency> findByDescription(String pDescription);

    @Query(value = """
                        SELECT  currency.id,
                                currency.code,
                                currency.status,
                                currency.description,
                                currency.flag_enabled enabled,
                                currency.insert_date as insertDate,
                                currency.last_updated_date as lastUpdatedDate,
                                fn_get_status_description('ST_CURRENCY', currency.status, :pLanguageCode) as statusDescription,
                                '' as message
                        FROM core_currency currency
                        WHERE currency.id = :id
                    """,
            nativeQuery = true)
    Optional<CurrencyDto> findByIdWithStatusDescription(Long id, String pLanguageCode);

    Optional<Currency> findByCodeAndIdNotEquals(String code, Long pId);


    Optional<Currency> findByDescriptionAndIdNotEquals(String pDescription, Long pId);
}