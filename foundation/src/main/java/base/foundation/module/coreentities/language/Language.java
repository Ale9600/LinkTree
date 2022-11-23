package base.foundation.module.coreentities.language;

import base.utility.module.utilities.entities.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "core_language")
public class Language extends BasicEntity{

    public static final String TABLE_NAME = "core_language";
    public static final String ENTITY_NAME = "Language";

    @Column
    private String description;

    @Override
    public String toString(){

        return "Entity: LANGUAGE - id: " + getId() + " description: " + getDescription() + " code: " + getCode() + " lastUpdatedDate: " + getLastUpdatedDate() + " insertDate: " + getInsertDate();
    }

    public Language(){
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
                                                SELECT language.id as id,
                                                       language.code as code,
                                                       language.description as description,
                                                       language.status as status,
                                                       fn_get_status_description('ST_LANGUAGE', status, 'ENG') as statusDescription,
                                                       language.last_updated_date as lastUpdatedDate,
                                                       language.insert_date as insertDate
                                                FROM %s language
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
