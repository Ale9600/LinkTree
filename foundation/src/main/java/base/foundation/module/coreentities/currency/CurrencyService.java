package base.foundation.module.coreentities.currency;

import base.foundation.module.coreentities.country.Country;
import base.foundation.module.coreentities.country.CountryRepository;
import base.utility.module.utilities.SessionData;
import base.utility.module.utilities.queries.NativeQueryReturnType;
import base.utility.module.utilities.singleton.services.BasicCrudService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_END;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_START;
import static base.utility.module.utilities.responses.ResponseConstants.*;
import static java.util.Objects.isNull;

@Singleton
public class CurrencyService implements BasicCrudService {

    private static final Logger log = LoggerFactory.getLogger(CurrencyService.class);

    @Inject
    CurrencyRepository currencyRepository;

    @Inject
    CountryRepository countryRepository;

    @PersistenceContext
    public EntityManager em;

    public CurrencyDto findById(SessionData sessionData, Long pId) {

        Optional<CurrencyDto> currencyOptional;
        CurrencyDto currencyDto;

        log.info(String.format(LOG_SERVICE_START, "id: " +pId, sessionData));

        currencyOptional = currencyRepository.findByIdWithStatusDescription(pId, sessionData.getLanguageCode());

        if (currencyOptional.isEmpty()) {
            currencyDto =  new CurrencyDto(NOT_FOUND, pId);
        } else {
            currencyDto = currencyOptional.get();
            currencyDto.setMessage(MESSAGE_OK);
       }

        log.info(String.format(LOG_SERVICE_END, currencyDto.getMessage()));

        return currencyDto;
    }

    @Transactional
    public CurrencyDto insertUpdate(SessionData sessionData, CurrencyDto pCurrencyDto, Long pId){

        Currency insertUpdateCurrency;
        Optional<Currency> currencyDb;
        CurrencyDto returnCurrency = null;

        log.info(String.format(LOG_SERVICE_START, pCurrencyDto, sessionData));

        if (pId == -1){
            //INSERT
            insertUpdateCurrency = new Currency();

            //CHECK CODE NOT NULL
            if (isNull(pCurrencyDto.getCode())){
                returnCurrency = new CurrencyDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Code",
                        pCurrencyDto.obtainEntityName()));
            }

            //CHECK UNIQUE CODE
            currencyDb = currencyRepository.findByCode(pCurrencyDto.getCode());
            if (currencyDb.isPresent()) {
                returnCurrency = new CurrencyDto(String.format(MESSAGE_UNIQUE_VALUE, pCurrencyDto.obtainEntityName(),
                        "Code",
                        pCurrencyDto.getCode()));
            }

            //Check description present
            if (isNull(pCurrencyDto.getDescription())) {
                returnCurrency = new CurrencyDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Description",
                        pCurrencyDto.getDescription()));
            }

            //Check description unique
            if (currencyRepository.findByDescription(pCurrencyDto.getDescription()).isPresent()) {
                returnCurrency = new CurrencyDto(String.format(MESSAGE_UNIQUE_VALUE,
                        pCurrencyDto.obtainEntityName(),
                        "Description",
                        pCurrencyDto.getDescription()));
            }
            if (isNull(returnCurrency)){
                currencyRepository.save(CurrencyMapper.INSTANCE.dtoToEntity(insertUpdateCurrency, pCurrencyDto));
                returnCurrency = CurrencyMapper.INSTANCE.entityToDto(new CurrencyDto(MESSAGE_OK), insertUpdateCurrency);
            }
        } else {
            //UPDATE
            //CHECK CODE NOT NULL
            if (!isNull(pCurrencyDto.getCode())){
                //CHECK UNIQUE CODE
                currencyDb = currencyRepository.findByCodeAndIdNotEquals(pCurrencyDto.getCode(), pId);
                if (currencyDb.isPresent()) {
                    returnCurrency = new CurrencyDto(String.format(MESSAGE_UNIQUE_VALUE, pCurrencyDto.obtainEntityName(),
                            "Code",
                            pCurrencyDto.obtainEntityName()));
                }
            }

            //CHECK DESCRIPTION NOT NULL
            if (!isNull(pCurrencyDto.getDescription())){
                //CHECK UNIQUE DESCRIPTION
                currencyDb = currencyRepository.findByDescriptionAndIdNotEquals(pCurrencyDto.getDescription(), pId);
                if (currencyDb.isPresent()) {
                    returnCurrency = new CurrencyDto(String.format(MESSAGE_UNIQUE_VALUE, pCurrencyDto.obtainEntityName(),
                            "Description",
                            pCurrencyDto.getDescription()));
                }
            }

            //GET ROLE
            currencyDb = currencyRepository.findById(pId);
            if (currencyDb.isEmpty()) {
                returnCurrency = new CurrencyDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Currency.ENTITY_NAME, pCurrencyDto.getId()));
            }

            if (isNull(returnCurrency)) {
                insertUpdateCurrency = currencyDb.get();
                insertUpdateCurrency = CurrencyMapper.INSTANCE.dtoToEntity(insertUpdateCurrency, pCurrencyDto);
                currencyRepository.update(insertUpdateCurrency);
                returnCurrency = CurrencyMapper.INSTANCE.entityToDto(new CurrencyDto(MESSAGE_OK), insertUpdateCurrency);
            }
        }
        log.info(String.format(LOG_SERVICE_END, returnCurrency.getMessage()));

        return returnCurrency;
    }

    @Transactional
    public CurrencyDto delete(SessionData sessionData, Long pId) {

        Optional<Currency> currencyDb;
        CurrencyDto returnDto;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId, sessionData));

        currencyDb = currencyRepository.findById(pId);

        if (currencyDb.isEmpty()){
            returnDto = new CurrencyDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Currency.ENTITY_NAME, pId));
        } else {
            if (countryRepository.findByCurrency(currencyDb.get()).size() > 0) {
                returnDto = new CurrencyDto(String.format(MESSAGE_ENTITY_LINKED, Currency.ENTITY_NAME, Country.ENTITY_NAME));
            } else {
                currencyRepository.deleteById(pId);
                returnDto = new CurrencyDto(MESSAGE_OK);
            }
        }

        log.info(String.format(LOG_SERVICE_END, returnDto.getMessage()));

        return returnDto;
    }

    @Transactional
    public NativeQueryReturnType nativeQuery(SessionData sessionData, String query){

        List<Object[]> list;
        Query q = em.createNativeQuery(query);

        log.info(String.format(LOG_SERVICE_START, "query: " + query, sessionData));

        list = q.getResultList();

        log.info(String.format(LOG_SERVICE_END, MESSAGE_OK));

        return new NativeQueryReturnType(MESSAGE_OK, list);
    }

}
