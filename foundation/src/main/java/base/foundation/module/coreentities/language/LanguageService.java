package base.foundation.module.coreentities.language;

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
public class LanguageService implements BasicCrudService {

    private static final Logger log = LoggerFactory.getLogger(LanguageService.class);

    @Inject
    LanguageRepository languageRepository;

    @PersistenceContext
    public EntityManager em;

    public LanguageDto findById(SessionData pSessionData, Long pId) {

        Optional<LanguageDto> languageOptional;
        LanguageDto languageDto;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId, pSessionData));

        languageOptional = languageRepository.findByIdWithStatusDescription(pId, pSessionData.getLanguageCode());

        if (languageOptional.isEmpty()) {
            languageDto = new LanguageDto(NOT_FOUND, pId);
        } else {
            languageDto = languageOptional.get();
            languageDto.setMessage(MESSAGE_OK);
        }

        log.info(String.format(LOG_SERVICE_END, languageDto.getMessage()));

        return languageDto;
    }

    @Transactional
    public LanguageDto insertUpdate(SessionData sessionData, LanguageDto languageDto, Long pId) {

        Language insertUpdateLanguage;
        Optional<Language> languageDb;
        LanguageDto returnLanguage = null;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId + " " + languageDto, sessionData));

        if (pId == -1) {
            //INSERT
            insertUpdateLanguage = new Language();

            //Check code present
            if (isNull(languageDto.getCode())) {
                returnLanguage = new LanguageDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Code",
                        languageDto.obtainEntityName()));
            }

            //Check code unique
            if (languageRepository.findByCode(languageDto.getCode()).isPresent()) {
                returnLanguage = new LanguageDto(String.format(MESSAGE_UNIQUE_VALUE,
                        languageDto.obtainEntityName(),
                        "Code",
                        languageDto.getCode()));
            }

            //Check description present
            if (isNull(languageDto.getDescription())) {
                returnLanguage = new LanguageDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Description",
                        languageDto.getDescription()));
            }

            //Check description unique
            if (languageRepository.findByDescription(languageDto.getDescription()).isPresent()) {
                returnLanguage = new LanguageDto(String.format(MESSAGE_UNIQUE_VALUE,
                        languageDto.obtainEntityName(),
                        "Description",
                        languageDto.getDescription()));
            }

            // Insert
            if (isNull(returnLanguage)) {
                insertUpdateLanguage = LanguageMapper.INSTANCE.dtoToEntity(insertUpdateLanguage, languageDto);
                languageRepository.save(insertUpdateLanguage);
                returnLanguage = LanguageMapper.INSTANCE.entityToDto(new LanguageDto(MESSAGE_OK), insertUpdateLanguage);
            }
        } else {
            //UPDATE
            languageDb = languageRepository.findById(pId);
            if (languageDb.isEmpty()) {
                returnLanguage = new LanguageDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Language.ENTITY_NAME, pId));
            }

            //Check description unique
            if (!isNull(languageDto.getDescription())) {
                if (languageRepository.findByDescriptionAndIdNotEquals(languageDto.getDescription(), pId).isPresent()) {
                    returnLanguage = new LanguageDto(String.format(MESSAGE_UNIQUE_VALUE,
                            languageDto.obtainEntityName(),
                            "Description",
                            languageDto.getDescription()));
                }
            }

            //Check code unique
            if (!isNull(languageDto.getDescription())) {
                if (languageRepository.findByCodeAndIdNotEquals(languageDto.getCode(), pId).isPresent()) {
                    returnLanguage = new LanguageDto(String.format(MESSAGE_UNIQUE_VALUE,
                            languageDto.obtainEntityName(),
                            "Code",
                            languageDto.getCode()));
                }
            }

            if (isNull(returnLanguage)) {
                insertUpdateLanguage = languageDb.get();
                insertUpdateLanguage = LanguageMapper.INSTANCE.dtoToEntity(insertUpdateLanguage, languageDto);
                languageRepository.update(insertUpdateLanguage);
                returnLanguage = LanguageMapper.INSTANCE.entityToDto(new LanguageDto(MESSAGE_OK), insertUpdateLanguage);
            }
        }
        log.info(String.format(LOG_SERVICE_END, returnLanguage.getMessage()));

        return returnLanguage;
    }

    @Transactional
    public LanguageDto delete(SessionData sessionData, Long id) {

        Optional<Language> languageDb;
        LanguageDto returnDto;

        log.info(String.format(LOG_SERVICE_START, "id: " + id, sessionData));

        languageDb = languageRepository.findById(id);

        if (languageDb.isEmpty()) {
            returnDto = new LanguageDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Language.ENTITY_NAME, id));
        } else {
            languageRepository.deleteById(id);
            returnDto = new LanguageDto(MESSAGE_OK, id);
        }

        log.info(String.format(LOG_SERVICE_END, returnDto.getMessage()));

        return returnDto;
    }

    @Transactional
    public NativeQueryReturnType nativeQuery(SessionData sessionData, String query) {

        List<Object[]> list;
        Query q = em.createNativeQuery(query);

        log.info(String.format(LOG_SERVICE_START, "query: " + query, sessionData));

        list = q.getResultList();

        log.info(String.format(LOG_SERVICE_END, String.format(MESSAGE_QUERY, list.size())));

        return new NativeQueryReturnType(MESSAGE_OK, list);
    }

}
