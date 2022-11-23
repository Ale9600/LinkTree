package base.foundation.module.coreentities.tabgen;

import base.foundation.module.coreentities.user.User;
import base.utility.module.utilities.entities.DtoWithStatusAndCode;
import org.hibernate.annotations.Immutable;

@Immutable
public class TabGenDto extends DtoWithStatusAndCode {

    private String description;

    @Override
    public String toString(){

        return "Dto: TABGEN - id = " + getId() + " code = " + getCode() + " description = " + getDescription() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public TabGenDto(){
    }

    public TabGenDto(String message){
        this.setMessage(message);
        this.setId(-1L);
    }

    public TabGenDto(String message, Long id){
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
}
