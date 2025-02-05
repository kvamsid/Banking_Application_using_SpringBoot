package com.example.banking_app.exception;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Handling specific exceptions- AccountException
    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorDetails> handleAccountException(AccountException exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(), exception.getMessage(), request.getDescription(false), "ACCOUNT_NOT_FOUND"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //Handling generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericException(AccountException exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(), exception.getMessage(), request.getDescription(false), "INTERNAL_SERVER_ERROR"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
