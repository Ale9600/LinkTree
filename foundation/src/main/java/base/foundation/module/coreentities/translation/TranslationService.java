package base.foundation.module.coreentities.translation;

import base.foundation.module.coreentities.settings.Settings;
import base.foundation.module.coreentities.settings.SettingsRepository;
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

import static base.foundation.module.FoundationConstants.KEY_DEFAULT_LANGUAGE;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_END;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_START;
import static base.utility.module.utilities.responses.ResponseConstants.*;
import static java.util.Objects.isNull;

@Singleton
public class TranslationService implements BasicCrudService {

    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);

    @Inject
    TranslationRepository translationRepository;

    @Inject
    SettingsRepository settingsRepository;

    @PersistenceContext
    public EntityManager em;

    public TranslationDto findById(SessionData sessionData, Long pId) {

        Optional<Translation> translationOptional;
        TranslationDto translationDto;

        translationOptional = translationRepository.findById(pId);

        if (translationOptional.isEmpty()) {
            translationDto = new TranslationDto(NOT_FOUND, pId);
        } else {
            translationDto = TranslationMapper.INSTANCE.entityToDto(new TranslationDto(), translationOptional.get());
            translationDto.setMessage(MESSAGE_OK);
        }

        return translationDto;
    }

    @Transactional
    public TranslationDto insertUpdate(SessionData sessionData, TranslationDto translationDto, Long id) {

        Translation insertUpdateTranslation;
        Optional<Translation> translationDb;
        TranslationDto returnTranslation = null;

        log.info(String.format(LOG_SERVICE_START, translationDto, sessionData));

        if (id == -1) {
            //INSERT
            insertUpdateTranslation = new Translation();

            //CHECK CODE NOT NULL
            if (isNull(translationDto.getCode())) {
                returnTranslation = new TranslationDto(String.format(MESSAGE_FIELD_MANDATORY,
                        "Code",
                        translationDto.obtainEntityName()));
            }

            //CHECK UNIQUE CODE
            translationDb = translationRepository.findByCode(translationDto.getCode());
            if (translationDb.isPresent()) {
                returnTranslation = new TranslationDto(String.format(MESSAGE_UNIQUE_VALUE, translationDto.obtainEntityName(),
                        "Code",
                        translationDto.getCode()));
            }
            if (isNull(returnTranslation)) {
                translationRepository.save(TranslationMapper.INSTANCE.dtoToEntity(insertUpdateTranslation, translationDto));
                returnTranslation = TranslationMapper.INSTANCE.entityToDto(new TranslationDto(MESSAGE_OK), insertUpdateTranslation);
            }
        } else {
            translationDb = translationRepository.findById(id);
            if (translationDb.isEmpty()) {
                returnTranslation = new TranslationDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Translation.ENTITY_NAME, translationDto.getId()));
            }

            if (isNull(returnTranslation)) {
                insertUpdateTranslation = translationDb.get();
                insertUpdateTranslation = TranslationMapper.INSTANCE.dtoToEntity(insertUpdateTranslation, translationDto);
                translationRepository.update(insertUpdateTranslation);
                returnTranslation = TranslationMapper.INSTANCE.entityToDto(new TranslationDto(MESSAGE_OK), insertUpdateTranslation);
            }
        }

        log.info(LOG_SERVICE_END);

        return returnTranslation;
    }

    @Transactional
    public TranslationDto delete(SessionData sessionData, Long id) {

        Optional<Translation> translationDb;

        translationDb = translationRepository.findById(id);

        if (translationDb.isEmpty()) {
            return new TranslationDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Translation.ENTITY_NAME, id));
        }

        translationRepository.deleteById(id);

        return new TranslationDto(String.format(MESSAGE_OK, Translation.ENTITY_NAME, id));
    }

    @Transactional
    public NativeQueryReturnType nativeQuery(SessionData sessionData, String query) {

        List<Object[]> list;
        Query q = em.createNativeQuery(query);

        list = q.getResultList();

        return new NativeQueryReturnType(MESSAGE_OK, list);
    }

    public String extractMessage(SessionData sessionData, String key, String[] args) {

        Optional<TranslationDto> optionalTranslation;
        Optional<Settings> optionalSettings;
        String defaultLanguage = null;


        optionalSettings = settingsRepository.findByKey(KEY_DEFAULT_LANGUAGE);

        if (optionalSettings.isPresent()){
            defaultLanguage = optionalSettings.get().getValue();
        }
        optionalTranslation = translationRepository.findByKeyAndLanguageOrDefaultLanguage(key, sessionData.getLanguageCode(), defaultLanguage);
        if (optionalTranslation.isPresent()) {
            if (!isNull(optionalTranslation.get().getDescription())) {
                return String.format(optionalTranslation.get().getDescription(), (Object[]) args);
            } else if (!isNull(optionalTranslation.get().getDefaultDescription())) {
                return String.format(optionalTranslation.get().getDefaultDescription(), (Object[]) args);
            }
        }
        return "<EMPTY> key:" + key;
    }

    public String extractMessage(SessionData sessionData, String key, Object arg) {

        Optional<TranslationDto> optionalTranslation;
        Optional<Settings> optionalSettings;
        String defaultLanguage = null;


        optionalSettings = settingsRepository.findByKey(KEY_DEFAULT_LANGUAGE);

        if (optionalSettings.isPresent()){
            defaultLanguage = optionalSettings.get().getValue();
        }
        optionalTranslation = translationRepository.findByKeyAndLanguageOrDefaultLanguage(key, sessionData.getLanguageCode(), defaultLanguage);
        if (optionalTranslation.isPresent()) {
            if (!isNull(optionalTranslation.get().getDescription())) {
                return String.format(optionalTranslation.get().getDescription(), arg);
            } else if (!isNull(optionalTranslation.get().getDefaultDescription())) {
                return String.format(optionalTranslation.get().getDefaultDescription(), arg);
            }
        }
        return "<EMPTY> key:" + key;
    }

    public String extractMessage(SessionData sessionData, String key) {

        Optional<TranslationDto> optionalTranslation;
        Optional<Settings> optionalSettings;
        String defaultLanguage = null;


        optionalSettings = settingsRepository.findByKey(KEY_DEFAULT_LANGUAGE);

        if (optionalSettings.isPresent()){
            defaultLanguage = optionalSettings.get().getValue();
        }
        optionalTranslation = translationRepository.findByKeyAndLanguageOrDefaultLanguage(key, sessionData.getLanguageCode(), defaultLanguage);
        if (optionalTranslation.isPresent()) {
            if (!isNull(optionalTranslation.get().getDescription())) {
                return optionalTranslation.get().getDescription();
            } else if (!isNull(optionalTranslation.get().getDefaultDescription())) {
                return optionalTranslation.get().getDefaultDescription();
            }
        }
        return "<EMPTY> key:" + key;
    }
}
