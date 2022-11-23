package base.utility.module.utilities.responses;

import base.utility.module.utilities.entities.BasicDto;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Singleton;

@Singleton
public class ResponseUtility  extends BaseResponseUtility{

    public <T extends BasicDto> HttpResponse<?>  elaborateQueryResponse(T pDto){

        ActionCompletedResponse<T> actionCompletedResponse;

        actionCompletedResponse = new ActionCompletedResponse<>(pDto);

        return simpleQueryReturnResponse(actionCompletedResponse);
    }

    public <T extends BasicDto>HttpResponse<?> elaborateInsertUpdateResponse(T pDto){

        ActionCompletedResponse<T> actionCompletedResponse;

        actionCompletedResponse = new ActionCompletedResponse<>(pDto);

        return insertUpdateReturnResponse(actionCompletedResponse);
    }

    public <T extends BasicDto> HttpResponse<?> elaborateDeleteResponse(T pDto){

        ActionCompletedResponse<T> actionCompletedResponse;

        actionCompletedResponse = new ActionCompletedResponse<>(pDto);

        return deleteReturnResponse(actionCompletedResponse);
    }
}
