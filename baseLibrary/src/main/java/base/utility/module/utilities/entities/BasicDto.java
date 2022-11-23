package base.utility.module.utilities.entities;


import base.utility.module.utilities.responses.ResponseConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public abstract class BasicDto {

    public String ENTITY_NAME;
    public String TABLE_NAME;

    private Long id = -1L;

    private boolean enabled = true;

    //private int success = 0;
//
    //public int getSuccess() {
    //    return success;
    //}
//
    //public void setSuccess(int success) {
    //    this.success = success;
    //}

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date insertDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
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

    @JsonIgnore
    private String message = ResponseConstants.MESSAGE_OK;

    public abstract String obtainEntityName();
}