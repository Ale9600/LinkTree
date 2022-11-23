package base.foundation.module.coreentities.translation;

import base.foundation.module.coreentities.user.User;
import base.utility.module.utilities.entities.DtoWithStatusAndCode;
import org.hibernate.annotations.Immutable;

@Immutable
public class TranslationDto extends DtoWithStatusAndCode {

    private String description;

    private String defaultDescription;

    private String type;

    @Override
    public String toString(){

        return "Dto: TRANSLATION - id = " + getId() + " code = " + getCode() + " description = " + getDescription() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public TranslationDto(){
    }

    public TranslationDto(String message){
        this.setMessage(message);
        this.setId(-1L);
    }

    public TranslationDto(String message, Long id){
        this.setMessage(message);
        this.setId(id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getDefaultDescription() {
        return defaultDescription;
    }

    public void setDefaultDescription(String defaultDescription) {
        this.defaultDescription = defaultDescription;
    }

    public String obtainEntityName() {
        return User.ENTITY_NAME;
    }
}
