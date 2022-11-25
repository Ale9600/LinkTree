package core.md.module.page;

import base.utility.module.utilities.entities.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "md_page")
public class Page extends BasicEntity{

    public static final String TABLE_NAME = "md_page";
    public static final String ENTITY_NAME = "Page";

    @Column
    public String code;

    @Column
    public String prova;

    @Column
    public String value;

    @Column
    public String username;

    @Column
    public String description;

    @Column
    public String links;

    @Override
    public String toString(){

        return "Entity: PAGE - id = " + getId() + " code = " + getCode() + " description = " + getDescription() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public Page(){}

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

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    // Queries
    public static final String QUERY_LIST = """
                                                SELECT page.id as id,
                                                        page.code as code
                                                FROM %s md_page
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
