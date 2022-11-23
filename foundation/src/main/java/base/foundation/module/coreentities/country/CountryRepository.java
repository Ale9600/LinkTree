package base.foundation.module.coreentities.country;

import base.foundation.module.coreentities.currency.Currency;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    Optional<Country> findByCode(String pCode);
    List<Country> findByCurrency(Currency pCurrency);
}