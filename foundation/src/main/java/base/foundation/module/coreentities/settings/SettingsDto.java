package base.foundation.module.coreentities.settings;

import base.foundation.module.coreentities.user.User;
import base.utility.module.utilities.entities.DtoWithStatusAndCode;
import io.micronaut.core.annotation.Introspected;
import org.hibernate.annotations.Immutable;

@Immutable
@Introspected
public class SettingsDto extends DtoWithStatusAndCode {

    private String description;

    private String statusDescription;

    @Override
    public String toString(){

        return "Dto: SETTINGS - id = " + getId() + " code = " + getCode() + " description = " + getDescription() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public SettingsDto(){
    }

    public SettingsDto(String message){
        this.setMessage(message);
        this.setId(-1L);
    }

    public SettingsDto(String message, Long id){
        this.setMessage(message);
        this.setId(id);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String obtainEntityName() {
        return User.ENTITY_NAME;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
