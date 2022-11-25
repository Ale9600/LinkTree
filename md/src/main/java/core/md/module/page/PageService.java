package core.md.module.page;

import base.foundation.module.coreentities.country.Country;
import base.foundation.module.coreentities.country.CountryRepository;
import base.foundation.module.coreentities.settings.Settings;
import base.foundation.module.coreentities.settings.SettingsRepository;
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
import static java.lang.String.format;
import static java.util.Objects.isNull;

@Singleton
public class PageService implements BasicCrudService {

    private static final Logger log = LoggerFactory.getLogger(PageService.class);

    @Inject
    PageRepository pageRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    CountryRepository countryRepository;

    @Inject
    SettingsRepository settingsRepository;

    @PersistenceContext
    public EntityManager em;

    public PageDto findById(SessionData pSessionData, Long pId) {

        Optional<PageDto> PageOptional;
        PageDto returnPage;

        log.info(format(LOG_SERVICE_START, "id: " + pId, pSessionData));

        PageOptional = pageRepository.findByIdWithStatusDescription(pId, pSessionData.getLanguageCode());

        if (PageOptional.isEmpty()) {
            returnPage =  new PageDto(NOT_FOUND, pId);
        } else {
            returnPage = PageOptional.get();
            returnPage.setMessage(MESSAGE_OK);
       }

        log.info(format(LOG_SERVICE_END, MESSAGE_OK));

        return returnPage;
    }

    @Transactional
    public PageDto insertUpdate(SessionData pSessionData, PageDto pPageDto, Long pId){

        Page insertUpdatePage;
        Optional<Page> PageDb;
        Optional<Country> countryDb = Optional.empty();
        Optional<Settings> settingsDb;
        Country country = null;
        PageDto returnPage = null;

        log.info(format(LOG_SERVICE_START, "id: " + pId + " " + pPageDto, pSessionData));

        if (pId == -1){
            //INSERT
            insertUpdatePage = new Page();

            //CHECK CODE NOT NULL
            if (isNull(pPageDto.getCode())){
                returnPage = new PageDto(format(MESSAGE_FIELD_MANDATORY,
                        "Code",
                        pPageDto.obtainEntityName()));
            }

            //CHECK DESCRIPTION NOT NULL
            if (isNull(pPageDto.getDescription())){
                returnPage = new PageDto(format(MESSAGE_FIELD_MANDATORY,
                        "Description",
                        pPageDto.obtainEntityName()));
            }

            PageDb = pageRepository.findByDescriptionAndIdNotEquals(pPageDto.getDescription(), pId);
            if (PageDb.isPresent()) {
                returnPage = new PageDto(format(MESSAGE_UNIQUE_VALUE, pPageDto.obtainEntityName(),
                        "Description",
                        pPageDto.getDescription()));
            }


        } else {
            //UPDATE
            //CHECK CODE NOT NULL
            if (!isNull(pPageDto.getCode())){
                //CHECK UNIQUE CODE
                PageDb = pageRepository.findByCodeAndIdNotEquals(pPageDto.getCode(), pId);
                if (PageDb.isPresent()) {
                    returnPage = new PageDto(format(MESSAGE_UNIQUE_VALUE, pPageDto.obtainEntityName(),
                            "Code",
                            pPageDto.getCode()));
                }
            }

            //CHECK DESCRIPTION NOT NULL
            if (!isNull(pPageDto.getDescription())){
                //CHECK UNIQUE DESCRIPTION
                PageDb = pageRepository.findByDescriptionAndIdNotEquals(pPageDto.getDescription(), pId);
                if (PageDb.isPresent()) {
                    returnPage = new PageDto(format(MESSAGE_UNIQUE_VALUE, pPageDto.obtainEntityName(),
                            "Description",
                            pPageDto.getDescription()));
                }
            }

            //GET Page
            PageDb = pageRepository.findById(pId);
            if (PageDb.isEmpty()) {
                returnPage = new PageDto(format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Page.ENTITY_NAME, pId));
            }

            if (isNull(returnPage)) {
                insertUpdatePage = PageDb.get();
                insertUpdatePage = PageMapper.INSTANCE.dtoToEntity(insertUpdatePage, pPageDto, country);
                pageRepository.update(insertUpdatePage);
                returnPage = PageMapper.INSTANCE.entityToDto(new PageDto(MESSAGE_OK), insertUpdatePage);
            }
        }

        log.info(format(LOG_SERVICE_END, returnPage.getMessage()));

        return returnPage;
    }

    @Transactional
    public PageDto delete(SessionData pSessionData, Long pId) {

        Optional<Page> PageDb;
        PageDto returnDto;

        log.info(format(LOG_SERVICE_START, "id: " + pId, pSessionData));

        PageDb = pageRepository.findById(pId);


        if (PageDb.isEmpty()){
            returnDto = new PageDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, Page.ENTITY_NAME, pId));
        } else {
            pageRepository.deleteById(pId);

            returnDto = new PageDto(MESSAGE_OK, pId);
        }

        log.info(format(LOG_SERVICE_END, returnDto.getMessage()));

        return returnDto;
    }

    @Transactional
    public NativeQueryReturnType nativeQuery(SessionData pSessionData, String query){

        List<Object[]> list;
        Query q = em.createNativeQuery(query);

        log.info(format(LOG_SERVICE_START, "query" + query, pSessionData));

        list = q.getResultList();

        log.info(format(LOG_SERVICE_END, format(MESSAGE_QUERY, list.size())));

        return new NativeQueryReturnType(MESSAGE_OK, list);
    }
}
