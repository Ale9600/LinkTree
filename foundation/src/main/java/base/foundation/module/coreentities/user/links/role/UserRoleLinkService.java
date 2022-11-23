package base.foundation.module.coreentities.user.links.role;

import base.foundation.module.coreentities.role.Role;
import base.foundation.module.coreentities.role.RoleRepository;
import base.foundation.module.coreentities.user.User;
import base.foundation.module.coreentities.user.UserRepository;
import base.utility.module.utilities.SessionData;
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
public class UserRoleLinkService implements BasicCrudService {

    private static final Logger log = LoggerFactory.getLogger(UserRoleLinkService.class);

    @PersistenceContext
    public EntityManager em;

    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    @Inject
    UserRoleLinkRepository userRoleLinkRepository;

    public UserRoleLinkDto findById(SessionData sessionData, Long pId) {

        Optional<UserRoleLink> userRoleLinkOptional;
        UserRoleLinkDto returnUserRoleLink;

        log.info(String.format(LOG_SERVICE_START, pId, sessionData));

        userRoleLinkOptional = userRoleLinkRepository.findById(pId);

        if (userRoleLinkOptional.isEmpty()) {
            returnUserRoleLink =  new UserRoleLinkDto(NOT_FOUND);
        }else {
            returnUserRoleLink = UserRoleLinkMapper.INSTANCE.entityToDto(new UserRoleLinkDto(), userRoleLinkOptional.get());
        }

        log.info(String.format(LOG_SERVICE_END, returnUserRoleLink.getMessage()));

        return returnUserRoleLink;
    }

    @Transactional
    public UserRoleLinkDto insert(SessionData sessionData, UserRoleLinkDto userRoleLinkDto){

        Optional<Role> roleDb;
        Optional<User> userDb;
        UserRoleLinkDto returnUserRoleLink = null;
        UserRoleLink insertUpdateUserRoleLink;

        log.info(String.format(LOG_SERVICE_START, userRoleLinkDto, sessionData));

        roleDb = roleRepository.findById(userRoleLinkDto.getRoleId());

        if(roleDb.isEmpty()){
            returnUserRoleLink =  new UserRoleLinkDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY,
                                Role.ENTITY_NAME,
                                userRoleLinkDto.getRoleId()));
        }

        userDb = userRepository.findById(userRoleLinkDto.getUserId());

        if(userDb.isEmpty() && isNull(returnUserRoleLink)){
            returnUserRoleLink =  new UserRoleLinkDto(String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY,
                                                                    Role.ENTITY_NAME,
                                                                    userRoleLinkDto.getUserId()));
        } else {
            //INSERT
            insertUpdateUserRoleLink = new UserRoleLink();
            userRoleLinkRepository.save(UserRoleLinkMapper.INSTANCE.mergeToEntity(insertUpdateUserRoleLink, roleDb.get(), userDb.get(), userRoleLinkDto));
            returnUserRoleLink = UserRoleLinkMapper.INSTANCE.entityToDto(new UserRoleLinkDto(MESSAGE_OK), insertUpdateUserRoleLink);
        }
        log.info(String.format(LOG_SERVICE_END, returnUserRoleLink.getMessage()));

        return returnUserRoleLink;
    }

    @Transactional
    public UserRoleLinkDto delete(SessionData sessionData, Long id) {

        Optional<UserRoleLink> userRoleLinkDb;
        UserRoleLinkDto returnUserRoleLink;

        log.info(String.format(LOG_SERVICE_START, "id: " + id, sessionData));

        userRoleLinkDb = userRoleLinkRepository.findById(id);

        if (userRoleLinkDb.isEmpty()){
            returnUserRoleLink = new UserRoleLinkDto(NOT_FOUND);
        } else {
            userRoleLinkRepository.deleteById(id);
            returnUserRoleLink = new UserRoleLinkDto(MESSAGE_OK);
        }

        log.info(String.format(LOG_SERVICE_END, returnUserRoleLink.getMessage()));

        return returnUserRoleLink;
    }

    @Transactional
    public List<Object[]> nativeQuery(SessionData sessionData, String query){

        List<Object[]> list;

        log.info(String.format(LOG_SERVICE_START, "query: " + query, sessionData));

        Query q = em.createNativeQuery(query);

        list = q.getResultList();

        log.info(String.format(LOG_SERVICE_END, MESSAGE_OK));

        return list;
    }
}
