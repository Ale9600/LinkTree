package base.foundation.module.coreentities.company;

import base.foundation.module.coreentities.user.User;
import base.utility.module.utilities.entities.DtoWithStatusAndCode;
import org.hibernate.annotations.Immutable;

@Immutable
public class CompanyDto extends DtoWithStatusAndCode {

    private String description;

    private Long countryId;

    private String address;

    private String businessName;

    private String phoneNumber;

    private String vatNumber;

    private String email;

    @Override
    public String toString(){

        return "Dto: COMPANY - id = " + getId() + " code = " + getCode() + " description = " + getDescription() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public CompanyDto(){
    }

    public CompanyDto(String message){
        this.setMessage(message);
        this.setId(-1L);
    }

    public CompanyDto(String message, Long id){
        this.setMessage(message);
        this.setId(id);
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String obtainEntityName() {
        return Company.ENTITY_NAME;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
}
