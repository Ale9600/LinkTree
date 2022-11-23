package base.utility.module.utilities.entities;


import base.utility.module.utilities.responses.ResponseConstants;

import java.util.Date;

public abstract class BasicLinkDto extends BasicDto {

    public String ENTITY_NAME;
    public String TABLE_NAME;

    private Long id = -1L;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    //Audit camps
    protected Long version = 0L;

    private Date insertDate;

    private Date lastUpdatedDate;

    public void toOutputWithoutAuditDatesAndMessage() {
        setInsertDate(null);
        setLastUpdatedDate(null);
        setMessage(null);
    }

    public void toOutputWithoutMessage(){
        setMessage(null);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message = ResponseConstants.MESSAGE_OK;

    public abstract String obtainEntityName();
}