package base.utility.module.utilities.queries;

import java.util.HashMap;
import java.util.Map;

import static base.utility.module.utilities.general.GeneralUtility.isNumeric;

public class QueryParameter {

    private Map<String, String> filters = new HashMap<>();

    private Map<String, String> orderBy = new HashMap<>();

    private String queryKey;

    private int limit = -1;

    private int offset = -1;

    private int page = -1;


    public String getFilterString(){

        String retString = "";

        if (filters.size() == 0){
            return retString;
        }

        retString = retString.concat("\n WHERE 1 = 1 ");

        for ( String entry : filters.keySet()){
            if (isNumeric(filters.get(entry))) {
                retString = retString.concat("\n AND " + entry + " = " + filters.get(entry));
            } else {
                retString = retString.concat("\n AND " + entry + " LIKE " + " \"%" + filters.get(entry) +  "%\"");
            }
        }
        return retString;
    }

    public String getPaginationString(){
        return String.format(QueryConstants.PAGINATION_STRING, getLimit(), getLimit() * getPage() + getOffset());
    }

    public String getOrderByString(){

        String retString = "";

        if (orderBy.size() == 0){
            return retString;
        }

        retString =  retString.concat(" ORDER BY ");

        for ( String entry : orderBy.keySet()){
            if (entry.contains("+")){
                retString = retString.concat( entry + " ASC ");
            } else {
                retString = retString.concat( entry + " DESC ");
            }
        }
        return retString;
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public Map<String, String> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Map<String, String> orderBy) {
        this.orderBy = orderBy;
    }
}
