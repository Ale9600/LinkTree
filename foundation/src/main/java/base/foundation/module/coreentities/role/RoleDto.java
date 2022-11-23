package base.foundation.module.coreentities.role;

import base.foundation.module.coreentities.user.User;
import base.utility.module.utilities.entities.DtoWithStatusAndCode;
import io.micronaut.core.annotation.Introspected;
import org.hibernate.annotations.Immutable;

@Immutable
public class RoleDto extends DtoWithStatusAndCode {

    private String description;

    @Override
    public String toString(){

        return "Dto: ROLE - id = " + getId() + " code = " + getCode() + " description = " + getDescription();
    }

    public RoleDto(){
    }

    public RoleDto(String message){
        this.setMessage(message);
        this.setId(-1L);
    }

    public RoleDto(String message, Long id){
        this.setMessage(message);
        this.setId(id);
    }

    public RoleDto(String message, Long id, int success){
        this.setMessage(message);
        this.setId(id);
        //this.setSuccess(success);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String obtainEntityName() {
        return Role.ENTITY_NAME;
    }

}
