package base.foundation.module.coreentities.user;

import base.foundation.module.coreentities.company.Company;
import base.foundation.module.coreentities.country.Country;
import base.foundation.module.coreentities.language.Language;
import base.utility.module.utilities.entities.BasicEntity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Entity(name = "core_user")
public class User extends BasicEntity{

    public static final String TABLE_NAME = "core_user";
    public static final String ENTITY_NAME = "User";

    @OneToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private boolean resetPassword = true;

    @OneToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    @OneToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @Override
    public String toString(){

        return "Entity: USER - id = " + getId() + " code: " + getCode() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate() + " username = " + getUsername() + " language: " + getLanguage() + " company: " + getCompany();
    }

    public User(){
    }

    //GETTER AND SETTER
    public static String getTableName(){
        return TABLE_NAME;
    }

    public static String getEntityName(){
        return ENTITY_NAME;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Company getCompany() {
        if (isNull(company)){
            return null;
        }
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Language getLanguage() {
        if (isNull(language)){
            return null;
        }
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public boolean isResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    //Queries
    public static final String QUERY_LIST = """
                                                SELECT cu.id as id,
                                                        cu.code as code
                                                FROM %s cu
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
