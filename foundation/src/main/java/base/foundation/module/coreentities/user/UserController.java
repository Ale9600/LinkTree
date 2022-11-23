package base.foundation.module.coreentities.user;

import base.foundation.module.coreentities.user.links.role.UserRoleLinkDto;
import base.foundation.module.coreentities.user.links.role.UserRoleLinkService;
import base.utility.module.utilities.SessionData;
import base.utility.module.utilities.queries.NativeQueryReturnType;
import base.utility.module.utilities.queries.QueryParameter;
import base.utility.module.utilities.singleton.controllers.BasicCrudController;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

import static base.foundation.module.FoundationConstants.BASE_URL;
import static base.utility.module.utilities.exceptions.ExceptionUtility.elaborateExceptionResponse;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_CONTROLLER_END;
import static base.utility.module.utilities.general.logs.LogsMessages.LOG_CONTROLLER_START;
import static base.utility.module.utilities.queries.QueryUtility.elaborateQueryFiltersAndPagination;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(BASE_URL + "user/")
public class UserController extends BasicCrudController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Inject
    UserService userService;

    @Inject
    UserRoleLinkService userRoleLinkService;

    @RolesAllowed({"R_ADMIN"})
    @Get("findById/{id}")
    public HttpResponse<?> findById(
           @Parameter Long id,
           @RequestAttribute SessionData sessionData
    ) {

        HttpResponse<?> response;
        UserDto userDto;

        try {

            log.info(LOG_CONTROLLER_START);

            userDto = userService.findById(sessionData, id);

            response = responseUtility.elaborateQueryResponse(userDto);

            log.info(String.format(LOG_CONTROLLER_END, userDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @Post
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> insert(
            @Body UserDto userDto,
            @RequestAttribute SessionData sessionData
    ){

        UserDto insertUpdateUser;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);


            insertUpdateUser = userService.insertUpdate(sessionData, userDto, -1L);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateUser);

            log.info(String.format(LOG_CONTROLLER_END, userDto.getMessage()));


        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @RolesAllowed({"R_ADMIN"})
    @Post("/{id}")
    public HttpResponse<?> update(
            @Body UserDto userDto,
            @RequestAttribute SessionData sessionData,
            @Parameter Long id
    ){

        UserDto insertUpdateUser;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateUser = userService.insertUpdate(sessionData, userDto, id);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateUser);

            log.info(String.format(LOG_CONTROLLER_END, userDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    // TODO UPDATE PASSWORD/MAIL/USERNAME

    @RolesAllowed({"R_ADMIN"})
    @Delete("delete/{id}")
    public HttpResponse<?> delete(
            @Parameter Long id,
            @RequestAttribute SessionData sessionData
    ){

        UserDto returnDto;
        HttpResponse<?> response;

        try {
            log.info(LOG_CONTROLLER_START);

            returnDto = userService.delete(sessionData, id);

            response = responseUtility.elaborateDeleteResponse(returnDto);

            log.info(String.format(LOG_CONTROLLER_END, returnDto.getMessage()));

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @RolesAllowed({"R_ADMIN"})
    @Post("query")
    public HttpResponse<?> query(
            @Body QueryParameter queryParameter,
            @RequestAttribute SessionData sessionData
    ) {

        String query, rawQuery;
        NativeQueryReturnType result;
        Map<String, Object> returnMap;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            rawQuery = User.queries.get(queryParameter.getQueryKey());

            query = elaborateQueryFiltersAndPagination(queryParameter, rawQuery);

            result = userService.nativeQuery(sessionData, query);

            returnMap = responseUtility.nativeQueryReturnResponse(result, query);

            log.info(String.format(LOG_CONTROLLER_END, result.getMessage()));

            response =  HttpResponse.ok(returnMap);

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }

        return response;
    }

    @RolesAllowed({"ADMIN"})
    @Post("insertRoleLink")
    public HttpResponse<?> insertRoleLink(
            @Body UserRoleLinkDto UserRoleLinkDto,
            @RequestAttribute SessionData sessionData
    ){

        UserRoleLinkDto insertUpdateUserRoleDto;
        HttpResponse<?> response;

        try {
            log.info(LOG_CONTROLLER_START);

            insertUpdateUserRoleDto = userRoleLinkService.insert(sessionData, UserRoleLinkDto);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateUserRoleDto);

            log.info(String.format(LOG_CONTROLLER_END, insertUpdateUserRoleDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @RolesAllowed({"ADMIN"})
    @Delete("deleteRoleLink/{id}")
    public HttpResponse<?> insertRoleLink(
            @Parameter Long id,
            @RequestAttribute SessionData sessionData
    ){

        UserRoleLinkDto insertUpdateUserRoleDto;
        HttpResponse<?> response;

        try {
            log.info(LOG_CONTROLLER_START);

            insertUpdateUserRoleDto = userRoleLinkService.delete(sessionData, id);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateUserRoleDto);

            log.info(String.format(LOG_CONTROLLER_END, insertUpdateUserRoleDto.getMessage()));

        } catch (Exception ex){

            response =  elaborateExceptionResponse(ex);
        }
        return response;
    }
}