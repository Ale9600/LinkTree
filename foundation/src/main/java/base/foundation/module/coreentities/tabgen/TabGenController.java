package base.foundation.module.coreentities.tabgen;

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
@RolesAllowed({"ADMIN"})
@Controller(BASE_URL + "tabGen/")
public class TabGenController extends BasicCrudController {

    private static final Logger log = LoggerFactory.getLogger(TabGenController.class);

    @Inject
    TabGenService tabGenService;

    @Get("findById/{id}")
    public HttpResponse<?> findById(
           @Parameter Long id,
           @RequestAttribute SessionData sessionData
    ) {

        HttpResponse<?> response;
        TabGenDto tabGenDto;

        try {

            log.info(LOG_CONTROLLER_START);

            tabGenDto = tabGenService.findById(sessionData, id);

            response = responseUtility.elaborateQueryResponse(tabGenDto);

            log.info(String.format(LOG_CONTROLLER_END, tabGenDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @Post
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> insertUpdate(
            @Body TabGenDto tabGenDto,
            @RequestAttribute SessionData sessionData
    ){

        TabGenDto insertUpdateTabGen;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateTabGen = tabGenService.insertUpdate(sessionData, tabGenDto, -1L);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateTabGen);

            log.info(String.format(LOG_CONTROLLER_END, tabGenDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @Post("/{id}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> update(
            @Body TabGenDto tabGenDto,
            @RequestAttribute SessionData sessionData,
            @Parameter Long id
    ){

        TabGenDto insertUpdateTabGen;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateTabGen = tabGenService.insertUpdate(sessionData, tabGenDto, id);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateTabGen);

            log.info(String.format(LOG_CONTROLLER_END, tabGenDto.getMessage()));


        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }


    @Delete("delete/{id}")
    public HttpResponse<?> delete(
            @Parameter Long id,
            @RequestAttribute SessionData sessionData
    ){

        TabGenDto returnDto;
        HttpResponse<?> response;

        try {
            log.info(LOG_CONTROLLER_START);

            returnDto = tabGenService.delete(sessionData, id);

            response = responseUtility.elaborateDeleteResponse(returnDto);

            log.info(String.format(LOG_CONTROLLER_END, returnDto.getMessage()));

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

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

            rawQuery = TabGen.queries.get(queryParameter.getQueryKey());

            query = elaborateQueryFiltersAndPagination(queryParameter, rawQuery);

            result = tabGenService.nativeQuery(sessionData, query);

            returnMap = responseUtility.nativeQueryReturnResponse(result, query);

            log.info(String.format(LOG_CONTROLLER_END, result.getMessage()));

            response = HttpResponse.ok(returnMap);

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }
}