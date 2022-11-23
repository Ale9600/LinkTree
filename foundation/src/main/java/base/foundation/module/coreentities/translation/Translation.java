package base.foundation.module.coreentities.translation;

import base.foundation.module.coreentities.language.Language;
import base.utility.module.utilities.entities.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "core_translation")
public class Translation extends BasicEntity{

    public static final String TABLE_NAME = "core_translation";
    public static final String ENTITY_NAME = "Translation";

    @Column
    private String description;

    @Column
    private String key;

    @Column
    private String type;

    @OneToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    @Override
    public String toString(){

        return "Entity: TRANSLATION - id = " + getId() + " code = " + getCode() + " description = " + getDescription() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public Translation(){}

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
