package base.foundation.module.coreentities.country;

import base.utility.module.utilities.entities.DtoWithStatusAndCode;
import org.hibernate.annotations.Immutable;

@Immutable
public class CountryDto extends DtoWithStatusAndCode {

    private String username;

    private String password;

    public String email = null;

    private int status;


    private Long companyId;

    private Long languageId = null;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCountryId() {
        return languageId;
    }

    public void setCountryId(Long languageId) {
        this.languageId = languageId;
    }

    @Override
    public String toString(){

        return "Dto: " + this.obtainEntityName() + " - id = " + getId() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate() + " languageId: " + getCountryId();
    }

    public CountryDto(){
    }

    public CountryDto(String message){
        this.setMessage(message);
        this.setId(-1L);
    }

    public CountryDto(String message, Long id){
        this.setMessage(message);
        this.setId(id);
    }

    public CountryDto(Country country){
        this.setId(country.getId());
        this.setInsertDate(country.getInsertDate());
        this.setLastUpdatedDate(country.getLastUpdatedDate());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountryname() {
        return username;
    }

    public void setCountryname(String username) {
        this.username = username;
    }

    public String obtainEntityName() {
        return Country.ENTITY_NAME;
    }
}
