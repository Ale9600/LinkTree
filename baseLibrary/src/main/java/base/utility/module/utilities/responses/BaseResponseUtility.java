package base.utility.module.utilities.responses;

import base.utility.module.utilities.queries.NativeQueryReturnType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Singleton;

import java.util.*;

import static base.utility.module.utilities.general.GeneralUtility.map;
import static base.utility.module.utilities.queries.QueryUtility.getQueryFields;
import static base.utility.module.utilities.responses.ResponseConstants.*;

@Singleton
public abstract class BaseResponseUtility {

    public <T extends ActionCompletedResponse<?>>  HttpResponse<?> simpleQueryReturnResponse(T pActionCompletedResult){

        if (pActionCompletedResult.isSuccess()) {
            return HttpResponse.ok(pActionCompletedResult);
        }

        pActionCompletedResult.toOutputNoData();

        return HttpResponse.ok(pActionCompletedResult);
    }

    public Map<String, Object> nativeQueryReturnResponse(NativeQueryReturnType pQueryResponse, String pQuery){

        List<String> fields;
        List<Object> retList = new ArrayList<>();
        Map <String, Object> map = new HashMap<>();
        Map<String, Object> sortedMap = new TreeMap<>(Collections.reverseOrder());

        //Mappo per output in base al risultato
        if (pQueryResponse.getResult().size() == 0){
            map.put(MESSAGE_KEY_VALUE, MESSAGE_NOT_FOUND_ELABORATED_ENTITY);
        } else {
            //estraggo i data index del risultato a partire dalla query
            fields = getQueryFields(pQuery);
            pQueryResponse.getResult().forEach(o -> {
                retList.add(map(fields, o));
            });
            map.put(MESSAGE_KEY_VALUE, pQueryResponse.getMessage());
            map.put(DATA_KEY_VALUE, retList);
        }
        sortedMap.putAll(map);

        return sortedMap;
    }

    public <T extends ActionCompletedResponse<?>> HttpResponse<?> insertUpdateReturnResponse(T pActionCompletedResult){

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode returnJson = mapper.createObjectNode();

        if (pActionCompletedResult.getMessage().equals(NOT_FOUND)){
            returnJson.putPOJO(MESSAGE_KEY_VALUE, String.format(MESSAGE_NOT_FOUND_SINGLE_ENTITY, pActionCompletedResult.data.obtainEntityName(), pActionCompletedResult.data.getId()));
            return HttpResponse.ok(returnJson);
        }

        if (pActionCompletedResult.data.getId() != -1) {
            pActionCompletedResult.data.toOutputWithoutAuditDatesAndMessage();
        } else {
            pActionCompletedResult.data = null;
        }

        return HttpResponse.ok(pActionCompletedResult);
    }

    public <T extends ActionCompletedResponse<?>> HttpResponse<?> deleteReturnResponse(T pActionCompletedResult){

        if (pActionCompletedResult.getMessage().equals(MESSAGE_OK)){
            pActionCompletedResult.setMessage(String.format(MESSAGE_OK, pActionCompletedResult.getData().obtainEntityName(), pActionCompletedResult.getData().getId()));
            pActionCompletedResult.toOutputNoData();
            return HttpResponse.ok(pActionCompletedResult);
        }

        pActionCompletedResult.toOutputNoData();

        return HttpResponse.ok(pActionCompletedResult);
    }
}
