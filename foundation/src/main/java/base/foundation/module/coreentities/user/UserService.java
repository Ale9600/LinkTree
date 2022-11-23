package base.foundation.module.coreentities.user;

import base.foundation.module.authentication.AuthenticationService;
import base.foundation.module.coreentities.company.Company;
import base.foundation.module.coreentities.company.CompanyRepository;
import base.foundation.module.coreentities.country.Country;
import base.foundation.module.coreentities.country.CountryRepository;
import base.foundation.module.coreentities.language.Language;
import base.foundation.module.coreentities.language.LanguageRepository;
import base.foundation.module.coreentities.role.Role;
import base.foundation.module.coreentities.settings.Settings;
import base.foundation.module.coreentities.settings.SettingsRepository;
import base.foundation.module.coreentities.translation.TranslationService;
import base.foundation.module.coreentities.user.links.role.UserRoleLink;
import base.foundation.module.coreentities.user.links.role.UserRoleLinkRepository;
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
import static base.foundation.module.FoundationConstants.KEY_DEFAULT_LANGUAGE;
import static base.foundation.module.authentication.AuthenticationService.getSalt;
import static base.utility.module.utilities.general.GeneralUtility.isValidEmail;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_END;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_START;
import static base.utility.module.utilities.responses.ResponseConstants.*;
import static java.util.Objects.isNull;

@Singleton
public class UserService implements BasicCrudService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    UserRepository userRepository;

    @Inject
    LanguageRepository languageRepository;

    @Inject
    SettingsRepository settingsRepository;

    @Inject
    CountryRepository countryRepository;

    @Inject
    CompanyRepository companyRepository;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    TranslationService translationService;

    @Inject
    UserRoleLinkRepository userRoleLinkRepository;

    @PersistenceContext
    public EntityManager em;

    public UserDto findById(SessionData pSessionData, Long pId) {

        Optional<UserDto> userOptional;
        UserDto returnDto;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId, pSessionData));

        userOptional = userRepository.findByIdWithStatusDescription(pId, pSessionData.getLanguageCode());

        if (userOptional.isEmpty()) {
            returnDto =  new UserDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {Role.ENTITY_NAME, "id", String.valueOf(pId)}));
        } else {
            returnDto = userOptional.get();
            returnDto.setMessage(translationService.extractMessage(pSessionData, MESSAGE_OK));
       }

        log.info(String.format(LOG_SERVICE_END, returnDto.getMessage()));

        return returnDto;
    }

    //TODO GESTIRE STATO DA CONFERMARE ECC CON CONFERMA VIA EMAIL
    @Transactional
    public UserDto insertUpdate(SessionData pSessionData, UserDto userDto, Long pId){

        User insertUpdateUser;
        Optional<User> userDb;
        UserDto returnUser = null;
        Optional<Language> languageDb = Optional.empty();
        Optional<Company> companyDb;
        Optional<Country> countryDb = Optional.empty();
        Optional<Settings> settingsDb;
        Company company = null;
        Language language = null;
        Country country = null;
        String hashedPw;

        log.info(String.format(LOG_SERVICE_START, userDto, pSessionData));

        if (!isNull(userDto.getCountryId())){
            countryDb = countryRepository.findById(userDto.getCountryId());
            if (countryDb.isEmpty() ){
                returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {Country.ENTITY_NAME, "id", String.valueOf(pId)}));
            }
        } else if (userDto.getId() == -1){
            settingsDb = settingsRepository.findByKey(KEY_DEFAULT_COUNTRY);
            if (settingsDb.isPresent()) {
                countryDb = countryRepository.findByCode(settingsDb.get().getValue());
            }
        }

        if (countryDb.isPresent()){
            country = countryDb.get();
        }

        if (!isNull(userDto.getLanguageId())){
            languageDb = languageRepository.findById(userDto.getLanguageId());
            if (languageDb.isEmpty()){
                returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {Language.ENTITY_NAME, "id", String.valueOf(pId)}));
            }
        } else if (userDto.getId() == -1) {
            if (!isNull(country)) {
                languageDb = languageRepository.findById(country.getLanguage().getId());
            } else {
                settingsDb = settingsRepository.findByKey(KEY_DEFAULT_LANGUAGE);
                if (settingsDb.isPresent()) {
                    languageDb = languageRepository.findByCode(settingsDb.get().getValue());
                }
            }
        }

        if (languageDb.isPresent()){
            language = languageDb.get();
        }

        if (!isNull(userDto.getCompanyId())){
            companyDb = companyRepository.findById(userDto.getCompanyId());
            if (companyDb.isEmpty()){
                returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {Company.ENTITY_NAME, "id", String.valueOf(pId)}));
            } else {
                company = companyDb.get();
            }
        }
        if (returnUser == null) {
            if (pId == -1 ){
                //INSERT
                insertUpdateUser = new User();

                //Check email present
                if (isNull(userDto.getEmail())){
                    returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_FIELD_MANDATORY, new String [] {"Email", User.ENTITY_NAME}));
                }

                // Check email unique
                userDb = userRepository.findByEmail(userDto.getEmail());
                if (userDb.isPresent() ) {
                    returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_UNIQUE_VALUE, new String[]{Role.ENTITY_NAME, "Email", userDto.getCode()}));
                }

                //Check username present
                if (isNull(userDto.getUsername())){
                    returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_FIELD_MANDATORY, new String [] {"Username", User.ENTITY_NAME}));
                }

                //Check username unique
                userDb = userRepository.findByUsername(userDto.getUsername());
                if (userDb.isPresent()) {
                    returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_UNIQUE_VALUE, new String [] {User.ENTITY_NAME, "Username", userDto.getUsername()}));
                }

                //Check code present
                if (isNull(userDto.getCode())){
                    returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_FIELD_MANDATORY, new String [] {"Code", User.ENTITY_NAME}));
                }

                //Check code unique
                if (userRepository.findByCode( userDto.getCode()).isPresent()){
                    returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_UNIQUE_VALUE, new String [] {User.ENTITY_NAME, "Code", userDto.getCode()}));
                }

                // Check email format
                if (!isValidEmail(userDto.email)) {
                    returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_INCORRECT_EMAIL_FORMAT));
                }
                //Check password present
                if (isNull(userDto.getPassword())){
                    returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_FIELD_MANDATORY, new String [] {"Password", Role.ENTITY_NAME}));
                }

                // Insert
                if(isNull(returnUser)){
                    hashedPw = authenticationService.hashPassword(userDto.getPassword(), getSalt(userDto.getUsername()));
                    insertUpdateUser.setPassword(hashedPw);
                    insertUpdateUser = UserMapper.INSTANCE.dtoToEntity(insertUpdateUser, userDto, language, company, country);
                    userRepository.save(insertUpdateUser);
                    returnUser = UserMapper.INSTANCE.entityToDto(new UserDto(translationService.extractMessage(pSessionData, MESSAGE_OK)), insertUpdateUser);
                }
            } else {
                //UPDATE
                userDb = userRepository.findById(pId);
                if (userDb.isEmpty()) {
                    returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {User.ENTITY_NAME, "id", String.valueOf(pId)}));
                } else {
                    if (!isNull(userDto.getEmail())) {
                        if (!isValidEmail(userDto.getEmail())) {
                            returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_INCORRECT_EMAIL_FORMAT));
                        }
                    }

                    if (!isNull(userDto.getCode())) {
                        if (userRepository.findByCode(userDto.getCode()).isPresent()) {
                            returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_UNIQUE_VALUE, new String [] {User.ENTITY_NAME, "Code", userDto.getCode()}));
                        }
                    }

                    if (!isNull(userDto.getUsername())) {
                        if (userRepository.findByCode(userDto.getCode()).isPresent()) {
                            returnUser = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_UNIQUE_VALUE, new String [] {User.ENTITY_NAME, "Username", userDto.getUsername()}));
                        }
                    }

                    if(isNull(returnUser)) {
                        insertUpdateUser = userDb.get();
                        //Check password change
                        if (!isNull(userDto.getPassword())){
                            hashedPw = authenticationService.hashPassword(userDto.getPassword(), getSalt(insertUpdateUser.getUsername()));
                            insertUpdateUser.setPassword(hashedPw);
                        }
                        insertUpdateUser = UserMapper.INSTANCE.dtoToEntity(insertUpdateUser, userDto, language, company, country);
                        userRepository.update(insertUpdateUser);
                        returnUser = UserMapper.INSTANCE.entityToDto(new UserDto(translationService.extractMessage(pSessionData, MESSAGE_OK)), insertUpdateUser);
                    }
                }
            }
        }

        log.info(String.format(LOG_SERVICE_END, returnUser.getMessage()));

        return returnUser;
    }

    @Transactional
    public UserDto delete(SessionData pSessionData, Long pId) {

        Optional<User> userDb;
        UserDto returnDto;
        List<UserRoleLink> userRoleLinkList;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId, pSessionData));

        userDb = userRepository.findById(pId);

        if (userDb.isEmpty()){
            returnDto =  new UserDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {User.ENTITY_NAME, "id", String.valueOf(pId)}));
        } else {
            // elimino tutti i link con i ruoli
            userRoleLinkList = userRoleLinkRepository.findByUser(userDb.get());
            userRoleLinkList.forEach( (UserRoleLink it)->{
                userRoleLinkRepository.deleteById(it.getId());
            });
            userRepository.deleteById(pId);
            returnDto = new UserDto(translationService.extractMessage(pSessionData, MESSAGE_OK));
        }

        log.info(String.format(LOG_SERVICE_END, returnDto.getMessage()));

        return returnDto;
    }

    @Transactional
    public NativeQueryReturnType nativeQuery(SessionData pSessionData, String pQuery){

        List<Object[]> list;
        Query q = em.createNativeQuery(pQuery);

        log.info(String.format(LOG_SERVICE_START, "query: " + pQuery, pSessionData));

        list = q.getResultList();

        log.info(String.format(LOG_SERVICE_END, MESSAGE_OK));

        return new NativeQueryReturnType(translationService.extractMessage(pSessionData, MESSAGE_OK), list);
    }

}
