package base.foundation.module.coreentities.role;

import base.utility.module.utilities.entities.BasicEntity;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "core_role")
public class Role extends BasicEntity{

    public static final String TABLE_NAME = "core_role";
    public static final String ENTITY_NAME = "Role";

    private String description;

    @Override
    public String toString(){

        return "Entity: ROLE - id = " + getId() + " code = " + getCode() + " description = " + getDescription() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public Role(){}

    // GETTERS AND SETTERS
    public static String getTableName(){
        return TABLE_NAME;
    }

    public static String getEntityName(){
        return ENTITY_NAME;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Queries
    public static final String QUERY_LIST = """
                                                SELECT role.id as id,
                                                       role.code as code,
                                                       role.description as description,
                                                       fn_get_status_description('ST_ROLE', status, 'ENG') as statusDescription,
                                                       role.last_updated_date as lastUpdatedDate,
                                                       role.insert_date as insertDate
                                                FROM %s role
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
