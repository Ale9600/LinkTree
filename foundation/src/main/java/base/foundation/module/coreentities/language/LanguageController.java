package base.foundation.module.coreentities.language;

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
import static base.utility.module.utilities.responses.ResponseConstants.MESSAGE_OK;

@Secured(SecurityRule.IS_AUTHENTICATED)
@RolesAllowed({"R_ADMIN"})
@Controller(BASE_URL + "language/")
public class LanguageController extends BasicCrudController {

    private static final Logger log = LoggerFactory.getLogger(LanguageController.class);

    @Inject
    LanguageService languageService;

    @Get("findById/{id}")
    public HttpResponse<?> findById(
           @Parameter Long id,
           @RequestAttribute SessionData sessionData
    ) {

        HttpResponse<?> response;
        LanguageDto languageDto;

        try {

            log.info(LOG_CONTROLLER_START);

            languageDto = languageService.findById(sessionData, id);

            response = responseUtility.elaborateQueryResponse(languageDto);

            log.info(String.format(LOG_CONTROLLER_END, MESSAGE_OK));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @Post
    public HttpResponse<?> insert(
            @Body LanguageDto languageDto,
            @RequestAttribute SessionData sessionData
    ){

        LanguageDto insertUpdateLanguage;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);


            insertUpdateLanguage = languageService.insertUpdate(sessionData, languageDto, -1L);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateLanguage);

            log.info(String.format(LOG_CONTROLLER_END, languageDto.getMessage()));


        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @Post("/{id}")
    public HttpResponse<?> update(
            @Body LanguageDto roleDto,
            @RequestAttribute SessionData sessionData,
            @Parameter Long id
    ){

        LanguageDto insertUpdateLanguage;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateLanguage = languageService.insertUpdate(sessionData, roleDto, id);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateLanguage);

            log.info(String.format(LOG_CONTROLLER_END, MESSAGE_OK));


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

        LanguageDto returnDto;
        HttpResponse<?> response;

        try {
            log.info(LOG_CONTROLLER_START);

            returnDto = languageService.delete(sessionData, id);

            response = responseUtility.elaborateDeleteResponse(returnDto);

            log.info(String.format(LOG_CONTROLLER_END, MESSAGE_OK));

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @Post("/query")
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

            rawQuery = Language.queries.get(queryParameter.getQueryKey());

            query = elaborateQueryFiltersAndPagination(queryParameter, rawQuery);

            result = languageService.nativeQuery(sessionData, query);

            returnMap = responseUtility.nativeQueryReturnResponse(result, query);

            log.info(String.format(LOG_CONTROLLER_END, result.getMessage()));

            response =  HttpResponse.ok(returnMap);

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }

        return response;
    }
}