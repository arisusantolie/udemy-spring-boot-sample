package com.appdeveloperblog.app.ws.mobileappws.exception;

public class UserServiceException extends RuntimeException{ //extend runtime tidak perlu add throws

    private static final long serialVersionUID = -3762008879555484245L;

    public UserServiceException(String message) {
        super(message);
    }
}
