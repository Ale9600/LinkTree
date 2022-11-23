package base.foundation.module.coreentities.role;

import base.foundation.module.coreentities.translation.TranslationService;
import base.foundation.module.coreentities.user.User;
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

import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_END;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_SERVICE_START;
import static base.utility.module.utilities.responses.ResponseConstants.*;
import static java.util.Objects.isNull;

@Singleton
public class RoleService implements BasicCrudService {

    private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    @Inject
    RoleRepository roleRepository;

    @Inject
    UserRoleLinkRepository userRoleLinkRepository;

    @Inject
    TranslationService translationService;

    @PersistenceContext
    public EntityManager em;

    public RoleDto findById(SessionData pSessionData, Long pId) {

        Optional<RoleDto> roleOptional;
        RoleDto returnDto;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId, pSessionData));

        roleOptional = roleRepository.findByIdWithStatusDescription(pId, pSessionData.getLanguageCode());

        if (roleOptional.isEmpty()) {
            returnDto =  new RoleDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {Role.ENTITY_NAME, "id", String.valueOf(pId)}));
        } else {
            returnDto = roleOptional.get();
            returnDto.setMessage(translationService.extractMessage(pSessionData, MESSAGE_OK));
       }

        log.info(String.format(LOG_SERVICE_END, returnDto.getMessage()));

        return returnDto;
    }

    @Transactional
    public RoleDto insertUpdate(SessionData pSessionData, RoleDto pRoleDto, Long pId){

        Role insertUpdateRole;
        Optional<Role> roleDb;
        RoleDto returnRole = null;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId + " " + pRoleDto, pSessionData));

        if (pId == -1){
            //INSERT
            insertUpdateRole = new Role();

            //CHECK CODE NOT NULL
            if (isNull(pRoleDto.getCode())){
                returnRole = new RoleDto(translationService.extractMessage(pSessionData, MESSAGE_FIELD_MANDATORY, new String [] {"Code", Role.ENTITY_NAME}));
            } else {
                //CHECK UNIQUE CODE
                roleDb = roleRepository.findByCode(pRoleDto.getCode());
                if (roleDb.isPresent()) {
                    returnRole = new RoleDto(translationService.extractMessage(pSessionData, MESSAGE_UNIQUE_VALUE, new String[]{Role.ENTITY_NAME, "Code", pRoleDto.getCode()}));
                }
                if (isNull(returnRole)) {
                    roleRepository.save(RoleMapper.INSTANCE.dtoToEntity(insertUpdateRole, pRoleDto));
                    returnRole = new RoleDto(translationService.extractMessage(pSessionData, MESSAGE_OK));
                    returnRole = RoleMapper.INSTANCE.entityToDto(returnRole, insertUpdateRole);
                }
            }
        } else {
            //UPDATE
            //CHECK CODE NOT NULL
            if (!isNull(pRoleDto.getCode())){
                //CHECK UNIQUE CODE
                roleDb = roleRepository.findByCodeAndIdNotEquals(pRoleDto.getCode(), pId);
                if (roleDb.isPresent()) {
                    returnRole = new RoleDto(translationService.extractMessage(pSessionData, MESSAGE_UNIQUE_VALUE, new String [] {Role.ENTITY_NAME, "Code", pRoleDto.getCode()}));
                }
            }

            //GET ROLE
            roleDb = roleRepository.findById(pId);
            if (roleDb.isEmpty()) {
                returnRole = new RoleDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {Role.ENTITY_NAME, "id", String.valueOf(pId)}));
            }

            if (isNull(returnRole)) {
                insertUpdateRole = roleDb.get();
                insertUpdateRole = RoleMapper.INSTANCE.dtoToEntity(insertUpdateRole, pRoleDto);
                roleRepository.update(insertUpdateRole);
                returnRole = RoleMapper.INSTANCE.entityToDto(new RoleDto(translationService.extractMessage(pSessionData, MESSAGE_OK)), insertUpdateRole);
            }
        }

        log.info(String.format(LOG_SERVICE_END, returnRole.getMessage()));

        return returnRole;
    }

    @Transactional
    public RoleDto delete(SessionData pSessionData, Long pId) {

        Optional<Role> roleDb;
        RoleDto returnDto;

        log.info(String.format(LOG_SERVICE_START, "id: " + pId, pSessionData));

        roleDb = roleRepository.findById(pId);

        if (roleDb.isEmpty()){
            returnDto = new RoleDto(translationService.extractMessage(pSessionData, MESSAGE_NOT_FOUND_SINGLE_ENTITY, new String[] {Role.ENTITY_NAME, "id", String.valueOf(pId)}));
        } else {
            if (userRoleLinkRepository.findByRole(roleDb.get()).size() > 0) {
                returnDto = new RoleDto(translationService.extractMessage(pSessionData, MESSAGE_ENTITY_LINKED, new String [] {Role.ENTITY_NAME, User.ENTITY_NAME}));
            } else {
                roleRepository.deleteById(pId);

                returnDto = new RoleDto(translationService.extractMessage(pSessionData, MESSAGE_OK));
            }
        }

        log.info(String.format(LOG_SERVICE_END, returnDto.getMessage()));

        return returnDto;
    }

    @Transactional
    public NativeQueryReturnType nativeQuery(SessionData pSessionData, String query){

        List<Object[]> list;
        String message;
        Query q = em.createNativeQuery(query);

        log.info(String.format(LOG_SERVICE_START, "query" + query, pSessionData));

        list = q.getResultList();

        message = translationService.extractMessage(pSessionData, MESSAGE_QUERY, list.size());

        log.info(LOG_SERVICE_END + "Found " + list.size() + " record");

        return new NativeQueryReturnType(message, list);
    }
}
