package base.foundation.module.coreentities.currency;

import base.utility.module.utilities.entities.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "core_currency")
public class Currency extends BasicEntity{

    public static final String TABLE_NAME = "core_currency";
    public static final String ENTITY_NAME = "Currency";

    @Column
    private String description;

    @Override
    public String toString(){

        return "Entity: CURRENCY - id: " + getId() + " description: " + getDescription() + " code: " + getCode() + " lastUpdatedDate: " + getLastUpdatedDate() + " insertDate: " + getInsertDate();
    }

    public Currency(){
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //GETTER AND SETTER
    public static String getTableName(){
        return TABLE_NAME;
    }

    public static String getEntityName(){
        return ENTITY_NAME;
    }

    //Queries
    public static final String QUERY_LIST = """
                                                SELECT currency.id as id,
                                                        currency.code as code
                                                FROM %s currency
                                            """;

    public static Map<String, String> queries;
    static{
        queries = new HashMap<>();
        queries.put("list", String.format(QUERY_LIST, getTableName()));
    }

    public static Map<String, String> getQueries() {
        return queries;
    }
}
