package base.foundation.module.coreentities.translation;

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
@Controller(BASE_URL + "Translation/")
public class TranslationController extends BasicCrudController {

    private static final Logger log = LoggerFactory.getLogger(TranslationController.class);

    @Inject
    TranslationService translationService;

    @Get("findById/{id}")
    public HttpResponse<?> findById(
           @Parameter Long id,
           @RequestAttribute SessionData sessionData
    ) {

        HttpResponse<?> response;
        TranslationDto TranslationDto;

        try {

            log.info(LOG_CONTROLLER_START);

            TranslationDto = translationService.findById(sessionData, id);

            response = responseUtility.elaborateQueryResponse(TranslationDto);

            log.info(String.format(LOG_CONTROLLER_END, TranslationDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @Post
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> insertUpdate(
            @Body TranslationDto TranslationDto,
            @RequestAttribute SessionData sessionData
    ){

        TranslationDto insertUpdateTranslation;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateTranslation = translationService.insertUpdate(sessionData, TranslationDto, -1L);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateTranslation);

            log.info(String.format(LOG_CONTROLLER_END, TranslationDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @Post("/{id}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> update(
            @Body TranslationDto TranslationDto,
            @RequestAttribute SessionData sessionData,
            @Parameter Long id
    ){

        TranslationDto insertUpdateTranslation;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateTranslation = translationService.insertUpdate(sessionData, TranslationDto, id);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateTranslation);

            log.info(String.format(LOG_CONTROLLER_END, TranslationDto.getMessage()));


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

        TranslationDto returnDto;
        HttpResponse<?> response;

        try {
            log.info(LOG_CONTROLLER_START);

            returnDto = translationService.delete(sessionData, id);

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

            rawQuery = Translation.queries.get(queryParameter.getQueryKey());

            query = elaborateQueryFiltersAndPagination(queryParameter, rawQuery);

            result = translationService.nativeQuery(sessionData, query);

            returnMap = responseUtility.nativeQueryReturnResponse(result, query);

            log.info(String.format(LOG_CONTROLLER_END, result.getMessage()));

            response = HttpResponse.ok(returnMap);

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }
}