package base.utility.module.utilities.exceptions;

import io.micronaut.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static base.utility.module.utilities.responses.ResponseConstants.MESSAGE_KEY_VALUE;
import static base.utility.module.utilities.responses.ResponseConstants.SUCCESS_KEY_VALUE;

public class ExceptionUtility {

    private static final Logger log = LoggerFactory.getLogger(ExceptionUtility.class);

    public static <T extends  Exception> HttpResponse<?> elaborateExceptionResponse(T ex){

        Map<String, String> responseBody;

        responseBody = new HashMap<>();

        ex.printStackTrace();

        responseBody.put(MESSAGE_KEY_VALUE, ex.getLocalizedMessage());

        responseBody.put(SUCCESS_KEY_VALUE, "false");
        return HttpResponse.serverError(responseBody);
    }
}
