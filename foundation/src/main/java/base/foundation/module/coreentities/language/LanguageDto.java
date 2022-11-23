package base.foundation.module.coreentities.language;

import base.utility.module.utilities.entities.DtoWithStatusAndCode;
import org.hibernate.annotations.Immutable;

@Immutable
public class LanguageDto extends DtoWithStatusAndCode {

    private int status;

    private String description;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){

        return "Dto: " + this.obtainEntityName() + " - id = " + getId() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public LanguageDto(){
    }

    public LanguageDto(String message){
        this.setMessage(message);
        this.setId(-1L);
    }

    public LanguageDto(String message, Long id){
        this.setMessage(message);
        this.setId(id);
    }

    public LanguageDto(Language language){
        this.setId(language.getId());
        this.setInsertDate(language.getInsertDate());
        this.setLastUpdatedDate(language.getLastUpdatedDate());
    }

    public String obtainEntityName() {
        return Language.ENTITY_NAME;
    }
}
