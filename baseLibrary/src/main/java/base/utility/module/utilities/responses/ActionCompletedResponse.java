package base.utility.module.utilities.responses;

import base.utility.module.utilities.entities.BasicDto;

import static base.utility.module.utilities.responses.ResponseConstants.MESSAGE_OK;
import static java.util.Objects.isNull;

public  class ActionCompletedResponse<T extends BasicDto> {

    public ActionCompletedResponse(String message) {
        this.message = message;
    }

    public ActionCompletedResponse(T dto) {
        this.message = dto.getMessage();
        this.data = dto;
        if (dto.getId() != -1){
            this.success = true;
        }
        //this.success = dto.isSuccess();
    }

    public String message;

    public boolean success = false;

    public T data;

    public void toOutputNoData(){
        this.setData(null);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
