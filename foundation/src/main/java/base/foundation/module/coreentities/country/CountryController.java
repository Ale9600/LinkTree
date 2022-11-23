package base.foundation.module.coreentities.country;

import base.foundation.module.coreentities.country.CountryService;
import base.utility.module.utilities.SessionData;
import base.utility.module.utilities.queries.NativeQueryReturnType;
import base.utility.module.utilities.queries.QueryParameter;
import base.utility.module.utilities.responses.ResponseConstants;
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
@Controller(BASE_URL + "country/")
@RolesAllowed({"ADMIN"})
public class CountryController extends BasicCrudController {

    private static final Logger log = LoggerFactory.getLogger(CountryController.class);

    @Inject
    CountryService countryService;

    @Get("findById/{id}")
    public HttpResponse<?> findById(
           @Parameter Long id,
           @RequestAttribute SessionData sessionData
    ) {

        HttpResponse<?> response;
        CountryDto countryDto;

        try {

            log.info(LOG_CONTROLLER_START);

            countryDto = countryService.findById(sessionData, id);

            response = responseUtility.elaborateQueryResponse(countryDto);

            log.info(String.format(LOG_CONTROLLER_END, countryDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @Post("insert")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> insert(
            @Body CountryDto countryDto,
            @RequestAttribute SessionData sessionData
    ){

        CountryDto insertUpdateCountry;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);


            insertUpdateCountry = countryService.insertUpdate(sessionData, countryDto);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateCountry);

            log.info(String.format(LOG_CONTROLLER_END, countryDto.getMessage()));


        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @Delete("delete/{id}")
    @RolesAllowed({"APP_USER"})
    public HttpResponse<?> delete(
            @Parameter Long id,
            @RequestAttribute SessionData sessionData
    ){

        CountryDto returnDto;
        HttpResponse<?> response;

        try {
            log.info(LOG_CONTROLLER_START);

            returnDto = countryService.delete(sessionData, id);

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

            rawQuery = Country.queries.get(queryParameter.getQueryKey());

            query = elaborateQueryFiltersAndPagination(queryParameter, rawQuery);

            result = countryService.nativeQuery(sessionData, query);

            returnMap = responseUtility.nativeQueryReturnResponse(result, query);

            log.info(String.format(LOG_CONTROLLER_END, result.getMessage()));

            response =  HttpResponse.ok(returnMap);

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }

        return response;
    }
}