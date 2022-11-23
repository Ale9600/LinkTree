package base.foundation.module.coreentities.settings;

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
@Controller(BASE_URL + "settings/")
public class SettingsController extends BasicCrudController {

    private static final Logger log = LoggerFactory.getLogger(SettingsController.class);

    @Inject
    SettingsService settingsService;

    @RolesAllowed({"R_ADMIN"})
    @Get("findById/{id}")
    public HttpResponse<?> findById(
           @Parameter Long id,
           @RequestAttribute SessionData sessionData
    ) {

        HttpResponse<?> response;
        SettingsDto settingsDto;

        try {

            log.info(LOG_CONTROLLER_START);

            settingsDto = settingsService.findById(sessionData, id);

            response = responseUtility.elaborateQueryResponse(settingsDto);

            log.info(String.format(LOG_CONTROLLER_END, settingsDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @Post
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> insertUpdate(
            @Body SettingsDto settingsDto,
            @RequestAttribute SessionData sessionData
    ){

        SettingsDto insertUpdateSettings;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateSettings = settingsService.insertUpdate(sessionData, settingsDto, -1L);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateSettings);

            log.info(String.format(LOG_CONTROLLER_END, settingsDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @RolesAllowed({"R_ADMIN"})
    @Post("/{id}")
    public HttpResponse<?> update(
            @Body SettingsDto settingsDto,
            @RequestAttribute SessionData sessionData,
            @Parameter Long id
    ){

        SettingsDto insertUpdateSettings;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateSettings = settingsService.insertUpdate(sessionData, settingsDto, id);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateSettings);

            log.info(String.format(LOG_CONTROLLER_END, settingsDto.getMessage()));


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

        SettingsDto returnDto;
        HttpResponse<?> response;

        try {
            log.info(LOG_CONTROLLER_START);

            returnDto = settingsService.delete(sessionData, id);

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

            rawQuery = Settings.queries.get(queryParameter.getQueryKey());

            query = elaborateQueryFiltersAndPagination(queryParameter, rawQuery);

            result = settingsService.nativeQuery(sessionData, query);

            returnMap = responseUtility.nativeQueryReturnResponse(result, query);

            log.info(String.format(LOG_CONTROLLER_END, result.getMessage()));

            response = HttpResponse.ok(returnMap);

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }
}