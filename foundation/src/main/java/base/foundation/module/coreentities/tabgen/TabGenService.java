package base.foundation.module.coreentities.tabgen;

import base.foundation.module.authentication.AuthenticationService;
import base.foundation.module.coreentities.role.Role;
import base.foundation.module.coreentities.role.RoleDto;
import base.foundation.module.coreentities.translation.TranslationService;
import base.foundation.module.coreentities.user.User;
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
public class TabGenService implements BasicCrudService {

    private static final Logger log = LoggerFactory.getLogger(TabGenService.class);

    @Inject
    TabGenRepository tabGenRepository;

    @Inject
    TranslationService translationService;

    @PersistenceContext
    public EntityManager em;

    public TabGenDto findById(SessionData pSessionData, Long pId) {

        Optional<TabGen> tabGenOptional;
        TabGenDto tabGenDto;

        tabGenOptional = tabGenRepository.findById(pId);

        if (tabGenOptional.isEmpty()) {
            tabGenDto =  new TabGenDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {TabGen.ENTITY_NAME, "id", String.valueOf(pId)}));
        } else {
            tabGenDto = TabGenMapper.INSTANCE.entityToDto(new TabGenDto(), tabGenOptional.get());
            tabGenDto.setMessage(translationService.extractMessage(pSessionData, MESSAGE_OK));
       }

        return tabGenDto;
    }

    @Transactional
    public TabGenDto insertUpdate(SessionData pSessionData, TabGenDto pTabGenDto, Long pId){

        TabGen insertUpdateTabGen;
        Optional<TabGen> tabGenDb;
        TabGenDto returnTabGen = null;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId + " " + pTabGenDto, pSessionData));

        if (pId == -1){
            //INSERT
            insertUpdateTabGen = new TabGen();

            //CHECK CODE NOT NULL
            if (isNull(pTabGenDto.getCode())){
                returnTabGen = new TabGenDto(translationService.extractMessage(pSessionData, MESSAGE_FIELD_MANDATORY, new String [] {"Code", Role.ENTITY_NAME}));
            }

            //CHECK UNIQUE CODE
            tabGenDb = tabGenRepository.findByCode(pTabGenDto.getCode());
            if (tabGenDb.isPresent()) {
                returnTabGen = new TabGenDto(translationService.extractMessage(pSessionData, MESSAGE_UNIQUE_VALUE, new String[]{TabGen.ENTITY_NAME, "Code", pTabGenDto.getCode()}));
            }
            if (isNull(returnTabGen)){
                tabGenRepository.save(TabGenMapper.INSTANCE.dtoToEntity(insertUpdateTabGen, pTabGenDto));
                returnTabGen = TabGenMapper.INSTANCE.entityToDto(new TabGenDto(translationService.extractMessage(pSessionData, MESSAGE_OK)), insertUpdateTabGen);
            }
        } else {
            tabGenDb = tabGenRepository.findById(pId);
            if (tabGenDb.isEmpty()) {
                returnTabGen = new TabGenDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {TabGen.ENTITY_NAME, "id", String.valueOf(pId)}));
            }

            if (isNull(returnTabGen)) {
                insertUpdateTabGen = tabGenDb.get();
                insertUpdateTabGen = TabGenMapper.INSTANCE.dtoToEntity(insertUpdateTabGen, pTabGenDto);
                tabGenRepository.update(insertUpdateTabGen);
                returnTabGen = TabGenMapper.INSTANCE.entityToDto(new TabGenDto(translationService.extractMessage(pSessionData, MESSAGE_OK)), insertUpdateTabGen);
            }
        }

        log.info(LOG_SERVICE_END);

        return returnTabGen;
    }

    @Transactional
    public TabGenDto delete(SessionData pSessionData, Long pId) {

        Optional<TabGen> tabGenDb;
        TabGenDto returnDto;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId, pSessionData));


        tabGenDb = tabGenRepository.findById(pId);

        if (tabGenDb.isEmpty()){
            returnDto = new TabGenDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {Role.ENTITY_NAME, "id", String.valueOf(pId)}));
        } else {
                tabGenRepository.deleteById(pId);

                returnDto = new TabGenDto(translationService.extractMessage(pSessionData, MESSAGE_OK));
        }

        tabGenRepository.deleteById(pId);

        log.info(String.format(LOG_SERVICE_END, returnDto.getMessage()));

        return new TabGenDto(translationService.extractMessage(pSessionData, MESSAGE_OK));
    }

    @Transactional
    public NativeQueryReturnType nativeQuery(SessionData pSessionData, String query){

        List<Object[]> list;
        Query q = em.createNativeQuery(query);

        log.info(String.format(LOG_SERVICE_START, "query" + query, pSessionData));

        list = q.getResultList();

        log.info(LOG_SERVICE_END + "Found " + list.size() + " record");

        return new NativeQueryReturnType(MESSAGE_OK, list);
    }
}
