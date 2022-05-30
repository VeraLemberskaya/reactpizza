package com.lemberskay.reactpizza.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotAuthorizedException extends RuntimeException{
    public NotAuthorizedException(){}

    public NotAuthorizedException(String message){
        super(message);
    }

    public NotAuthorizedException(String message, Throwable cause){
        super(message, cause);
    }

    public NotAuthorizedException(Throwable cause){
        super(cause);
    }
}
