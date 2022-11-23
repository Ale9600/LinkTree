package base.foundation.module.coreentities.company;

import base.foundation.module.coreentities.country.Country;
import base.foundation.module.coreentities.country.CountryRepository;
import base.foundation.module.coreentities.settings.Settings;
import base.foundation.module.coreentities.settings.SettingsRepository;
import base.foundation.module.coreentities.user.User;
import base.foundation.module.coreentities.user.UserRepository;
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

import static base.foundation.module.FoundationConstants.KEY_DEFAULT_COUNTRY;
import static base.utility.module.utilities.general.GeneralUtility.isValidEmail;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_END;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_START;
import static base.utility.module.utilities.responses.ResponseConstants.*;
import static java.util.Objects.isNull;

@Singleton
public class CompanyService implements BasicCrudService {

    private static final Logger log = LoggerFactory.getLogger(CompanyService.class);

    @Inject
    CompanyRepository companyRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    CountryRepository countryRepository;

    @Inject
    SettingsRepository settingsRepository;

    @PersistenceContext
    public EntityManager em;

    public CompanyDto findById(SessionData pSessionData, Long pId) {

        Optional<CompanyDto> companyOptional;
        CompanyDto returnCompany;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId, pSessionData));

        companyOptional = companyRepository.findByIdWithStatusDescription(pId, pSessionData.getLanguageCode());

        if (companyOptional.isEmpty()) {
            returnCompany =  new CompanyDto(NOT_FOUND, pId);
        } else {
            returnCompany = companyOptional.get();
            returnCompany.setMessage(MESSAGE_OK);
       }

        log.info(String.format(LOG_SERVICE_END, MESSAGE_OK));

        return returnCompany;
    }

    @Transactional
    public CompanyDto insertUpdate(SessionData pSessionData, CompanyDto pCompanyDto, Long pId){

        Company insertUpdateCompany;
        Optional<Company> companyDb;
        Optional<Country> countryDb = Optional.empty();
        Optional<Settings> settingsDb;
        Country country = null;
        CompanyDto returnCompany = null;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId + " " + pCompanyDto, pSessionData));

        if (!isNull(pCompanyDto.getCountryId())){
            countryDb = countryRepository.findById(pCompanyDto.getCountryId());
            if (countryDb.isEmpty() ){
                returnCompany = new CompanyDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Country.ENTITY_NAME, pCompanyDto.getCountryId()));
            }
        } else if (pCompanyDto.getId() == -1){
            settingsDb = settingsRepository.findByKey(KEY_DEFAULT_COUNTRY);
            if (settingsDb.isPresent()) {
                countryDb = countryRepository.findByCode(settingsDb.get().getValue());
            }
        }

        if (countryDb.isPresent()){
            country = countryDb.get();
        }

        if (pId == -1){
            //INSERT
            insertUpdateCompany = new Company();

            //CHECK CODE NOT NULL
            if (isNull(pCompanyDto.getCode())){
                returnCompany = new CompanyDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Code",
                        pCompanyDto.obtainEntityName()));
            }

            //CHECK UNIQUE CODE
            companyDb = companyRepository.findByCode(pCompanyDto.getCode());
            if (companyDb.isPresent()) {
                returnCompany = new CompanyDto(String.format(MESSAGE_UNIQUE_VALUE, pCompanyDto.obtainEntityName(),
                        "Code",
                        pCompanyDto.getCode()));
            }

            //CHECK ADDRESS NOT NULL
            if (isNull(pCompanyDto.getAddress())){
                returnCompany = new CompanyDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Address",
                        pCompanyDto.obtainEntityName()));
            }

            //CHECK VATNUMBER NOT NULL
            if (isNull(pCompanyDto.getVatNumber())){
                returnCompany = new CompanyDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Vat Number",
                        pCompanyDto.obtainEntityName()));
            }

            //CHECK DESCRIPTION NOT NULL
            if (isNull(pCompanyDto.getDescription())){
                returnCompany = new CompanyDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Description",
                        pCompanyDto.obtainEntityName()));
            }

            companyDb = companyRepository.findByDescriptionAndIdNotEquals(pCompanyDto.getDescription(), pId);
            if (companyDb.isPresent()) {
                returnCompany = new CompanyDto(String.format(MESSAGE_UNIQUE_VALUE, pCompanyDto.obtainEntityName(),
                        "Description",
                        pCompanyDto.getDescription()));
            }

            //CHECK BUSINESS NAME NOT NULL
            if (isNull(pCompanyDto.getBusinessName())){
                returnCompany = new CompanyDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Business Name",
                        pCompanyDto.obtainEntityName()));
            }

            //CHECK EMAIL NOT NULL
            if (isNull(pCompanyDto.getEmail())){
                returnCompany = new CompanyDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Email",
                        pCompanyDto.obtainEntityName()));
            }

            if (!isValidEmail(pCompanyDto.getEmail())) {
                returnCompany = new CompanyDto(String.format(MESSAGE_INCORRECT_EMAIL_FORMAT, pCompanyDto.getEmail()));
            }

            if (isNull(returnCompany)){
                companyRepository.save(CompanyMapper.INSTANCE.dtoToEntity(insertUpdateCompany, pCompanyDto, country));
                returnCompany = CompanyMapper.INSTANCE.entityToDto(new CompanyDto(MESSAGE_OK), insertUpdateCompany);
            }
        } else {
            //UPDATE
            //CHECK CODE NOT NULL
            if (!isNull(pCompanyDto.getCode())){
                //CHECK UNIQUE CODE
                companyDb = companyRepository.findByCodeAndIdNotEquals(pCompanyDto.getCode(), pId);
                if (companyDb.isPresent()) {
                    returnCompany = new CompanyDto(String.format(MESSAGE_UNIQUE_VALUE, pCompanyDto.obtainEntityName(),
                            "Code",
                            pCompanyDto.getCode()));
                }
            }

            //CHECK DESCRIPTION NOT NULL
            if (!isNull(pCompanyDto.getDescription())){
                //CHECK UNIQUE DESCRIPTION
                companyDb = companyRepository.findByDescriptionAndIdNotEquals(pCompanyDto.getDescription(), pId);
                if (companyDb.isPresent()) {
                    returnCompany = new CompanyDto(String.format(MESSAGE_UNIQUE_VALUE, pCompanyDto.obtainEntityName(),
                            "Description",
                            pCompanyDto.getDescription()));
                }
            }

            //GET COMPANY
            companyDb = companyRepository.findById(pId);
            if (companyDb.isEmpty()) {
                returnCompany = new CompanyDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Company.ENTITY_NAME, pId));
            }

            if (isNull(returnCompany)) {
                insertUpdateCompany = companyDb.get();
                insertUpdateCompany = CompanyMapper.INSTANCE.dtoToEntity(insertUpdateCompany, pCompanyDto, country);
                companyRepository.update(insertUpdateCompany);
                returnCompany = CompanyMapper.INSTANCE.entityToDto(new CompanyDto(MESSAGE_OK), insertUpdateCompany);
            }
        }

        log.info(String.format(LOG_SERVICE_END, returnCompany.getMessage()));

        return returnCompany;
    }

    @Transactional
    public CompanyDto delete(SessionData pSessionData, Long pId) {

        Optional<Company> companyDb;
        CompanyDto returnDto;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId, pSessionData));

        companyDb = companyRepository.findById(pId);


        if (companyDb.isEmpty()){
            returnDto = new CompanyDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Company.ENTITY_NAME, pId));
        } else {
            if (userRepository.findByCompany(companyDb.get()).isPresent()) {
                returnDto = new CompanyDto(String.format(MESSAGE_ENTITY_LINKED, Company.ENTITY_NAME, User.ENTITY_NAME));
            } else {
                companyRepository.deleteById(pId);

                returnDto = new CompanyDto(MESSAGE_OK, pId);
            }
        }

        log.info(String.format(LOG_SERVICE_END, returnDto.getMessage()));

        return returnDto;
    }

    @Transactional
    public NativeQueryReturnType nativeQuery(SessionData pSessionData, String query){

        List<Object[]> list;
        Query q = em.createNativeQuery(query);

        log.info(String.format(LOG_SERVICE_START, "query" + query, pSessionData));

        list = q.getResultList();

        log.info(String.format(LOG_SERVICE_END, String.format(MESSAGE_QUERY, list.size())));

        return new NativeQueryReturnType(MESSAGE_OK, list);
    }
}
