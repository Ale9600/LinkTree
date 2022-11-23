package base.utility.module.utilities.queries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static base.utility.module.utilities.queries.QueryConstants.*;

public class QueryUtility {

    private static final String WRAP_QUERY = """
                SELECT * from 
                ( %s ) query
                %s
                %s
                %s
            """;

    public static String elaborateQueryFiltersAndPagination(QueryParameter pQueryParameter, String pQuery) {
        String whereClause, paginationClause, orderByClause, retString;
        QueryParameter queryParameter;

        queryParameter = pQueryParameter;

        if (queryParameter == null) {
            queryParameter = new QueryParameter();
        }

        if (queryParameter.getLimit() == -1) {
            queryParameter.setLimit(DEFAULT_LIMIT);
        }

        if (queryParameter.getOffset() == -1) {
            queryParameter.setOffset(DEFAULT_OFFSET);
        }

        if (queryParameter.getPage() == -1) {
            queryParameter.setPage(DEFAULT_PAGE);
        }

        whereClause = queryParameter.getFilterString();
        orderByClause = queryParameter.getOrderByString();
        paginationClause = queryParameter.getPaginationString();

        retString = String.format(WRAP_QUERY, pQuery, whereClause, orderByClause, paginationClause);

        return retString;
    }

    public static List<String> getQueryFields(String pQueryValue) {
        List<String> list = new ArrayList<>(Arrays.asList(pQueryValue.trim().substring(pQueryValue.indexOf("SELECT") + 11, pQueryValue.indexOf("FROM") - 5).trim().substring(pQueryValue.indexOf("SELECT") + 11).replaceAll("\\(.*?\\)", "").split(",")));
        List<String> returnList = new ArrayList<>();

        list.forEach((s) -> {
            returnList.add(s.substring(s.indexOf(" as ") + 4));
                }
        );

        return returnList;
    }
}
