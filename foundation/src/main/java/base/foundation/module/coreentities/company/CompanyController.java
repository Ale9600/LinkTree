package base.foundation.module.coreentities.company;

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

@Controller(BASE_URL + "company/")
public class CompanyController extends BasicCrudController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);

    @Inject
    CompanyService companyService;

    @RolesAllowed({"R_ADMIN"})
    @Get("findById/{id}")
    public HttpResponse<?> findById(
           @Parameter Long id,
           @RequestAttribute SessionData sessionData
    ) {

        HttpResponse<?> response;
        CompanyDto companyDto;

        try {

            log.info(LOG_CONTROLLER_START);

            companyDto = companyService.findById(sessionData, id);

            response = responseUtility.elaborateQueryResponse(companyDto);

            log.info(String.format(LOG_CONTROLLER_END, companyDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @RolesAllowed({"R_ADMIN"})
    @Post
    public HttpResponse<?> insert(
            @Body CompanyDto companyDto,
            @RequestAttribute SessionData sessionData
    ){

        CompanyDto insertUpdateCompany;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateCompany = companyService.insertUpdate(sessionData, companyDto, -1L);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateCompany);

            log.info(String.format(LOG_CONTROLLER_END, companyDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @RolesAllowed({"R_ADMIN"})
    @Post("/{id}")
    public HttpResponse<?> update(
            @Body CompanyDto companyDto,
            @RequestAttribute SessionData sessionData,
            @Parameter Long id
    ){

        CompanyDto insertUpdateCompany;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdateCompany = companyService.insertUpdate(sessionData, companyDto, id);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdateCompany);

            log.info(String.format(LOG_CONTROLLER_END, companyDto.getMessage()));


        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @RolesAllowed({"R_ADMIN"})
    @Delete("delete/{id}")
    public HttpResponse<?> delete(
            @Parameter Long id,
            @RequestAttribute SessionData sessionData
    ){

        CompanyDto returnDto;
        HttpResponse<?> response;

        try {
            log.info(LOG_CONTROLLER_START);

            returnDto = companyService.delete(sessionData, id);

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

            rawQuery = Company.queries.get(queryParameter.getQueryKey());

            query = elaborateQueryFiltersAndPagination(queryParameter, rawQuery);

            result = companyService.nativeQuery(sessionData, query);

            returnMap = responseUtility.nativeQueryReturnResponse(result, query);

            log.info(String.format(LOG_CONTROLLER_END, result.getMessage()));

            response = HttpResponse.ok(returnMap);

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }
}