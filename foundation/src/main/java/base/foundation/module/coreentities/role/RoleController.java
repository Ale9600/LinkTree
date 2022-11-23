package base.foundation.module.coreentities.role;

import base.foundation.module.coreentities.translation.TranslationService;
import base.utility.module.utilities.SessionData;
import base.utility.module.utilities.queries.NativeQueryReturnType;
import base.utility.module.utilities.queries.QueryParameter;
import base.utility.module.utilities.singleton.controllers.BasicCrudController;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
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
import static base.utility.module.utilities.responses.ResponseConstants.MESSAGE_OK;

@Controller(BASE_URL + "role/")
public class RoleController extends BasicCrudController {

    private static final Logger log = LoggerFactory.getLogger(RoleController.class);

    @Inject
    RoleService roleService;

    @RolesAllowed({"R_ADMIN"})
    @Get("findById/{id}")
    public HttpResponse<?> findById(
           @Parameter Long id,
           @RequestAttribute SessionData sessionData
    ) {

        HttpResponse<?> response;
        RoleDto roleDto;

        try {

            log.info(LOG_CONTROLLER_START);

            roleDto = roleService.findById(sessionData, id);

            response = responseUtility.elaborateQueryResponse(roleDto);

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }

        log.info(LOG_CONTROLLER_END);

        return response;
    }

    @RolesAllowed({"R_ADMIN"})
    @Post
    public HttpResponse<?> insert(
            @Body RoleDto roleDto,
            @RequestAttribute SessionData sessionData
    ){

        RoleDto insertUpdateRole;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateRole = roleService.insertUpdate(sessionData, roleDto, -1L);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateRole);

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }

        log.info(LOG_CONTROLLER_END);

        return response;
    }

    @RolesAllowed({"R_ADMIN"})
    @Post("/{id}")
    public HttpResponse<?> update(
            @Body RoleDto roleDto,
            @RequestAttribute SessionData sessionData,
            @Parameter Long id
    ){

        RoleDto insertUpdateRole;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateRole = roleService.insertUpdate(sessionData, roleDto, id);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateRole);

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }

        log.info(LOG_CONTROLLER_END);

        return response;
    }

    @RolesAllowed({"R_ADMIN"})
    @Delete("delete/{id}")
    public HttpResponse<?> delete(
            @Parameter Long id,
            @RequestAttribute SessionData sessionData
    ){

        RoleDto returnDto;
        HttpResponse<?> response;

        try {
            log.info(LOG_CONTROLLER_START);

            returnDto = roleService.delete(sessionData, id);

            response = responseUtility.elaborateDeleteResponse(returnDto);

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }

        log.info(LOG_CONTROLLER_END);

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

            rawQuery = Role.queries.get(queryParameter.getQueryKey());

            query = elaborateQueryFiltersAndPagination(queryParameter, rawQuery);

            result = roleService.nativeQuery(sessionData, query);

            returnMap = responseUtility.nativeQueryReturnResponse(result, query);

            response = HttpResponse.ok(returnMap);

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }

        log.info(LOG_CONTROLLER_END);

        return response;
    }
}