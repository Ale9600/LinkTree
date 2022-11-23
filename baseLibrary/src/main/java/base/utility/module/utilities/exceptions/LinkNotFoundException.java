package base.utility.module.utilities.exceptions;

public class LinkNotFoundException extends Exception{

    public LinkNotFoundException(String errorMessage, String entityName) {
        super(String.format(errorMessage, entityName));
    }
}
