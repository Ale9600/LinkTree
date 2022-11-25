package core.md.module.page;

import base.utility.module.utilities.entities.DtoWithStatusAndCode;
import org.hibernate.annotations.Immutable;

@Immutable
public class PageDto extends DtoWithStatusAndCode {

    public String code;
    public String key;
     public String value;
   public String username;
   public String description;
   public String links;

    @Override
    public String toString(){

        return "Dto: PAGE - id = " + getId() + " code = " + getCode() + " description = " + getDescription() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public PageDto(){
    }

    public PageDto(String message){
        this.setMessage(message);
        this.setId(-1L);
    }

    public PageDto(String message, Long id){
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
        return ENTITY_NAME;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }
}
