package base.foundation.module.coreentities.country;

import base.foundation.module.authentication.AuthenticationService;
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

import static base.utility.module.utilities.general.GeneralUtility.isValidEmail;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_END;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_START;
import static base.utility.module.utilities.responses.ResponseConstants.*;
import static java.util.Objects.isNull;

@Singleton
public class CountryService implements BasicCrudService {

    private static final Logger log = LoggerFactory.getLogger(CountryService.class);

    @Inject
    CountryRepository countryRepository;

    @Inject
    AuthenticationService authenticationService;

    @PersistenceContext
    public EntityManager em;

    public CountryDto findById(SessionData sessionData, Long pId) {

        Optional<Country> countryOptional;
        CountryDto countryDto;

        log.info(String.format(LOG_SERVICE_START, "id: " +pId, sessionData));

        countryOptional = countryRepository.findById(pId);

        if (countryOptional.isEmpty()) {
            countryDto =  new CountryDto(NOT_FOUND, pId);
        } else {
            countryDto = CountryMapper.INSTANCE.entityToDto(new CountryDto(), countryOptional.get());
            countryDto.setMessage(MESSAGE_OK);
       }

        log.info(String.format(LOG_SERVICE_END, countryDto.getMessage()));

        return countryDto;
    }

    @Transactional
    public CountryDto insertUpdate(SessionData sessionData, CountryDto countryDto){

        Country insertUpdateCountry;
        Optional<Country> countryDb;
        CountryDto returnCountry = null;
        String hashedPw;

        log.info(String.format(LOG_SERVICE_START, countryDto, sessionData));

        if (countryDto.getId() == -1){
            //INSERT
            insertUpdateCountry = new Country();

            //Check code present
            if (isNull(countryDto.getCode())){
                returnCountry = new CountryDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Code",
                        countryDto.obtainEntityName()));
            }

            //Check code unique
            if (countryRepository.findByCode( countryDto.getCode()).isPresent()){
                returnCountry = new CountryDto(String.format(MESSAGE_UNIQUE_VALUE,
                        countryDto.obtainEntityName(),
                        "Code",
                        countryDto.obtainEntityName()));
            }

            // Insert
            if(isNull(returnCountry)){
                insertUpdateCountry = CountryMapper.INSTANCE.dtoToEntity(insertUpdateCountry, countryDto);
                countryRepository.save(insertUpdateCountry);
                returnCountry = CountryMapper.INSTANCE.entityToDto(new CountryDto(MESSAGE_OK), insertUpdateCountry);
            }
        } else {
            //UPDATE
            countryDb = countryRepository.findById(countryDto.getId());
            if (countryDb.isEmpty()) {
                returnCountry = new CountryDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Country.ENTITY_NAME, countryDto.getId()));
            } else {
                if (!isValidEmail(countryDto.email)) {
                    returnCountry = new CountryDto(String.format(MESSAGE_INCORRECT_EMAIL_FORMAT, countryDto.getEmail()));
                }
                if( isNull(returnCountry)) {
                    insertUpdateCountry = countryDb.get();
                    insertUpdateCountry = CountryMapper.INSTANCE.dtoToEntity(insertUpdateCountry, countryDto);
                    countryRepository.update(insertUpdateCountry);
                    returnCountry = CountryMapper.INSTANCE.entityToDto(new CountryDto(MESSAGE_OK), insertUpdateCountry);
                }
            }
        }
        log.info(String.format(LOG_SERVICE_END, returnCountry.getMessage()));

        return returnCountry;
    }

    @Transactional
    public CountryDto delete(SessionData sessionData, Long id) {

        Optional<Country> countryDb;
        CountryDto returnDto;

        log.info(String.format(LOG_SERVICE_START, "id: " + id, sessionData));

        countryDb = countryRepository.findById(id);

        if (countryDb.isEmpty()){
            returnDto =  new CountryDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Country.ENTITY_NAME, id));
        } else {
            countryRepository.deleteById(id);
            returnDto = new CountryDto(MESSAGE_OK);
        }

        log.info(String.format(LOG_SERVICE_END, returnDto.getMessage()));

        return new CountryDto(String.format(MESSAGE_OK, id));
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
