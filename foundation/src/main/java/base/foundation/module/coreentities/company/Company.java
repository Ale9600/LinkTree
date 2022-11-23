package base.foundation.module.coreentities.company;

import base.foundation.module.coreentities.country.Country;
import base.utility.module.utilities.entities.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "core_company")
public class Company extends BasicEntity{

    public static final String TABLE_NAME = "core_company";
    public static final String ENTITY_NAME = "Company";

    @Column
    private String description;

    @Column
    private String vatNumber;

    @Column
    private String businessName;

    @Column
    private String address;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @Override
    public String toString(){

        return "Entity: COMPANY - id = " + getId() + " code = " + getCode() + " description = " + getDescription() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public Company(){}

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Queries
    public static final String QUERY_LIST = """
                                                SELECT company.id as id,
                                                        company.code as code
                                                FROM %s company
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
