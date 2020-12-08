package com.appdeveloperblog.app.ws.mobileappws.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class CustomExceptionFormat {

    private Date timeStamp;
    private String message;
    private HttpStatus status;
    private int errorCode;

    public CustomExceptionFormat(Date timeStamp, String message, HttpStatus status, int errorCode) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.status = status;
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public CustomExceptionFormat() {
    }



    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
