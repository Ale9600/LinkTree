package core.md.module.page;

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
import static java.lang.String.format;

@Controller(BASE_URL + "page/")
public class PageController extends BasicCrudController {

    private static final Logger log = LoggerFactory.getLogger(PageController.class);

    @Inject
    PageService pageService;

    @RolesAllowed({"R_ADMIN"})
    @Get("findById/{id}")
    public HttpResponse<?> findById(
           @Parameter Long id,
           @RequestAttribute SessionData sessionData
    ) {

        HttpResponse<?> response;
        PageDto pageDto;

        try {

            log.info(LOG_CONTROLLER_START);

            pageDto = pageService.findById(sessionData, id);

            response = responseUtility.elaborateQueryResponse(pageDto);

            log.info(format(LOG_CONTROLLER_END, pageDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @RolesAllowed({"R_ADMIN"})
    @Post
    public HttpResponse<?> insert(
            @Body PageDto pageDto,
            @RequestAttribute SessionData sessionData
    ){

        PageDto insertUpdatePage;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdatePage = pageService.insertUpdate(sessionData, pageDto, -1L);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdatePage);

            log.info(format(LOG_CONTROLLER_END, pageDto.getMessage()));

        } catch (Exception ex){

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }

    @RolesAllowed({"R_ADMIN"})
    @Post("/{id}")
    public HttpResponse<?> update(
            @Body PageDto pageDto,
            @RequestAttribute SessionData sessionData,
            @Parameter Long id
    ){

        PageDto insertUpdatePage;
        HttpResponse<?> response;

        try {

            log.info(LOG_CONTROLLER_START);

            insertUpdatePage = pageService.insertUpdate(sessionData, pageDto, id);

            response = responseUtility.elaborateInsertUpdateResponse(insertUpdatePage);

            log.info(format(LOG_CONTROLLER_END, pageDto.getMessage()));


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

        PageDto returnDto;
        HttpResponse<?> response;

        try {
            log.info(LOG_CONTROLLER_START);

            returnDto = pageService.delete(sessionData, id);

            response = responseUtility.elaborateDeleteResponse(returnDto);

            log.info(format(LOG_CONTROLLER_END, returnDto.getMessage()));

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

            rawQuery = Page.queries.get(queryParameter.getQueryKey());

            query = elaborateQueryFiltersAndPagination(queryParameter, rawQuery);

            result = pageService.nativeQuery(sessionData, query);

            returnMap = responseUtility.nativeQueryReturnResponse(result, query);

            log.info(format(LOG_CONTROLLER_END, result.getMessage()));

            response = HttpResponse.ok(returnMap);

        } catch (Exception ex) {

            response = elaborateExceptionResponse(ex);
        }
        return response;
    }
}