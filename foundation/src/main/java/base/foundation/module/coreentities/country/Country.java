package base.foundation.module.coreentities.country;

import base.foundation.module.coreentities.currency.Currency;
import base.foundation.module.coreentities.language.Language;
import base.utility.module.utilities.entities.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "core_country")
public class Country extends BasicEntity{

    public static final String TABLE_NAME = "core_country";
    public static final String ENTITY_NAME = "Country";

    @Column
    private String description;

    @OneToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private Currency currency;

    @OneToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    @Override
    public String toString(){

        return "Entity: COUNTRY - id: " + getId() + " description: " + getDescription() + " code: " + getCode() + " lastUpdatedDate: " + getLastUpdatedDate() + " insertDate: " + getInsertDate();
    }

    public Country(){
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
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
                                                SELECT country.id
                                                FROM %s core_country
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
