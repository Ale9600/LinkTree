package base.foundation.module.coreentities.tabgen;

import base.foundation.module.coreentities.language.Language;
import base.utility.module.utilities.entities.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "core_tab_gen")
public class TabGen extends BasicEntity{

    public static final String TABLE_NAME = "core_tab_gen";
    public static final String ENTITY_NAME = "Generic code";

    @Column
    private String description;

    @Column
    private String key;

    @OneToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    @Override
    public String toString(){

        return "Entity: GENERIC CODE - id = " + getId() + " code = " + getCode() + " description = " + getDescription() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public TabGen(){}

    // GETTERS AND SETTERS
    public static String getTableName(){
        return TABLE_NAME;
    }

    public static String getEntityName(){
        return ENTITY_NAME;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    // Queries
    public static final String QUERY_LIST = """
                                                SELECT tabgen.id
                                                FROM %s core_tab_gen tabgen
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
