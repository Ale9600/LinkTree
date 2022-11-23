package base.foundation.module.coreentities.user;

import base.utility.module.utilities.entities.DtoWithStatusAndCode;
import io.micronaut.core.annotation.Introspected;
import org.hibernate.annotations.Immutable;

@Immutable
public class UserDto extends DtoWithStatusAndCode {

    private String username;

    private String password;

    public String email = null;

    private int status;

    private Long companyId;

    private Long languageId;

    private Long countryId;

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

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

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    @Override
    public String toString(){

        return "Dto: " + this.obtainEntityName() + " - id = " + getId() + " code: " + getCode() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate() + " languageId: " + getLanguageId();
    }

    public UserDto(){
    }

    public UserDto(String message){
        this.setMessage(message);
        this.setId(-1L);
    }

    public UserDto(String message, Long id){
        this.setMessage(message);
        this.setId(id);
    }

    public UserDto(User user){
        this.setId(user.getId());
        this.setInsertDate(user.getInsertDate());
        this.setLastUpdatedDate(user.getLastUpdatedDate());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String obtainEntityName() {
        return User.ENTITY_NAME;
    }
}
