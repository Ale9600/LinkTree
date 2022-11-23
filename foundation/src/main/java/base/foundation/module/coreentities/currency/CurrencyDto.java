package base.foundation.module.coreentities.currency;

import base.utility.module.utilities.entities.DtoWithStatusAndCode;
import org.hibernate.annotations.Immutable;

@Immutable
public class CurrencyDto extends DtoWithStatusAndCode {

    private int status;

    private String description;

    @Override
    public String toString(){

        return "Dto: " + this.obtainEntityName() + " - id = " + getId() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public CurrencyDto(){
    }

    public CurrencyDto(String message){
        this.setMessage(message);
        this.setId(-1L);
    }

    public CurrencyDto(String message, Long id){
        this.setMessage(message);
        this.setId(id);
    }

    public CurrencyDto(Currency currency){
        this.setId(currency.getId());
        this.setInsertDate(currency.getInsertDate());
        this.setLastUpdatedDate(currency.getLastUpdatedDate());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String obtainEntityName() {
        return Currency.ENTITY_NAME;
    }
}
