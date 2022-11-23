package base.utility.module.utilities.entities;

import io.micronaut.core.annotation.Introspected;

@Introspected
public abstract class DtoWithStatusAndCode extends  BasicDto{

    private String code;

    private String statusDescription;

    private int status;

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
