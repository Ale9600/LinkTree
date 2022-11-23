package base.utility.module.utilities.queries;

import java.util.List;

public class NativeQueryReturnType {

    private String message;

    private boolean success = true;

    private List<Object[]> result;

    public NativeQueryReturnType(String message, List<Object[]> result) {
        this.message = message;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object[]> getResult() {
        return result;
    }

    public void setResult(List<Object[]> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
