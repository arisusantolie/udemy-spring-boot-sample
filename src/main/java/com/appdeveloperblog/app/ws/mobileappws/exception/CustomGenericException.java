package com.appdeveloperblog.app.ws.mobileappws.exception;

public class CustomGenericException extends RuntimeException {

    private static final long serialVersionUID = 1987474765911272153L;
    private String message;
    private boolean error = false;

    public CustomGenericException() {
    }

    public CustomGenericException(String message, boolean error) {
        this.message = message;
        this.error = error;
    }
    public CustomGenericException(String message) {
        this.message = message;
        this.error = true;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}
