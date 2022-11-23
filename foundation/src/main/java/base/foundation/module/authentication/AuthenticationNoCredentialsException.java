package base.foundation.module.authentication;

import static base.foundation.module.authentication.AuthenticationConstants.MESSAGE_NO_CREDENTIALS_AUTHENTICATION_ERROR;

public class AuthenticationNoCredentialsException extends Exception{

    public AuthenticationNoCredentialsException() {
        super(MESSAGE_NO_CREDENTIALS_AUTHENTICATION_ERROR);
    }
}
