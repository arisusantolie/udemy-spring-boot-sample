package com.appdeveloperblog.app.ws.mobileappws.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler { //untuk nge handle semua exception pada class ini

    @ExceptionHandler(value = {UserServiceException.class}) //untuk handle bnyk execption tinggal tambah , misal : ,Exception.class
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request){

        ErrorMessage errorMessage =new ErrorMessage(new Date(), ErrorMessages.NO_RECORD_FOUND.getErrorMessage()); //custom format return response api

        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(value = {Exception.class})
//    public ResponseEntity<Object> handleOtherException(Exception ex,WebRequest request){
//
//        CustomExceptionFormat customExceptionFormat=new CustomExceptionFormat(new Date(), ex.getMessage(),HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.value());
//
//        return new ResponseEntity<>(customExceptionFormat,new HttpHeaders(), HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(value=CustomGenericException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody //pakai ini biar tidak ngikut default runtime
    public ErrorMessage customException(RuntimeException ex) {
        //System.out.println("run time");
        ErrorMessage cm = new ErrorMessage(new Date(),ex.getMessage());

        return cm;
    }
}
