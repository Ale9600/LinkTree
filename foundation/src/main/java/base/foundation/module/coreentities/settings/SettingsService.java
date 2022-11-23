package base.foundation.module.coreentities.settings;

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

import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_END;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_START;
import static base.utility.module.utilities.responses.ResponseConstants.*;
import static java.util.Objects.isNull;

@Singleton
public class SettingsService implements BasicCrudService {

    private static final Logger log = LoggerFactory.getLogger(SettingsService.class);

    @Inject
    SettingsRepository settingsRepository;

    @Inject
    AuthenticationService authenticationService;

    @PersistenceContext
    public EntityManager em;

    public SettingsDto findById(SessionData pSessionData, Long pId) {

        Optional<SettingsDto> settingsOptional;
        SettingsDto returnDto;

        log.info(String.format(LOG_SERVICE_START, pId, pSessionData));

        settingsOptional = settingsRepository.findByIdWithStatusDescription(pId, pSessionData.getLanguageCode());

        if (settingsOptional.isEmpty()) {
            returnDto =  new SettingsDto(NOT_FOUND, pId);
        } else {
            returnDto = settingsOptional.get();
            returnDto.setMessage(MESSAGE_OK);
       }

        log.info(String.format(LOG_SERVICE_END, returnDto.getMessage()));

        return returnDto;
    }

    @Transactional
    public SettingsDto insertUpdate(SessionData pSessionData, SettingsDto pSettingsDto, Long pId){

        Settings insertUpdateSettings;
        Optional<Settings> settingsDb;
        SettingsDto returnSettings = null;

        log.info(String.format(LOG_SERVICE_START, pSettingsDto, pSessionData));

        if (pId == -1){
            //INSERT

            insertUpdateSettings = new Settings();

            //CHECK CODE NOT NULL
            if (isNull(pSettingsDto.getCode())){
                returnSettings = new SettingsDto(String.format(MESSAGE_FIELD_MANDATORY,
                                                "Code",
                                                pSettingsDto.obtainEntityName()));
            }

            //CHECK UNIQUE CODE
            settingsDb = settingsRepository.findByCode(pSettingsDto.getCode());
            if (settingsDb.isPresent()) {
                returnSettings = new SettingsDto(String.format(MESSAGE_UNIQUE_VALUE, pSettingsDto.obtainEntityName(),
                                                                            "Code",
                                                                            pSettingsDto.getCode()));
            }
            if (isNull(returnSettings)){
                settingsRepository.save(SettingsMapper.INSTANCE.dtoToEntity(insertUpdateSettings, pSettingsDto));
                returnSettings = SettingsMapper.INSTANCE.entityToDto(new SettingsDto(MESSAGE_OK), insertUpdateSettings);
            }
        } else {
            //UPDATE

            //GET ROLE
            settingsDb = settingsRepository.findById(pId);
            if (settingsDb.isEmpty()) {
                returnSettings = new SettingsDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Settings.ENTITY_NAME, pSettingsDto.getId()));
            }

            if (isNull(returnSettings)) {
                insertUpdateSettings = settingsDb.get();
                insertUpdateSettings = SettingsMapper.INSTANCE.dtoToEntity(insertUpdateSettings, pSettingsDto);
                settingsRepository.update(insertUpdateSettings);
                returnSettings = SettingsMapper.INSTANCE.entityToDto(new SettingsDto(MESSAGE_OK), insertUpdateSettings);
            }
        }

        log.info(LOG_SERVICE_END);

        return returnSettings;
    }

    @Transactional
    public SettingsDto delete(SessionData sessionData, Long id) {

        Optional<Settings> settingsDb;

        settingsDb = settingsRepository.findById(id);

        if (settingsDb.isEmpty()){
            return new SettingsDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Settings.ENTITY_NAME, id));
        }

        settingsRepository.deleteById(id);

        return new SettingsDto(String.format(MESSAGE_OK, id));
    }

    @Transactional
    public NativeQueryReturnType nativeQuery(SessionData sessionData, String query){

        List<Object[]> list;
        Query q = em.createNativeQuery(query);

        list = q.getResultList();

        return new NativeQueryReturnType(MESSAGE_OK, list);
    }
}
