package inu.project.shareu.advice;

import inu.project.shareu.advice.exception.AuthenticationEntryPointException;
import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.MajorException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.model.response.common.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({
            MemberException.class,
            MajorException.class,
            ItemException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse expectException(Exception e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return exceptionResponse;
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse exceptionResponse(AuthenticationEntryPointException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return exceptionResponse;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse exceptionResponse(AccessDeniedException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
        return exceptionResponse;
    }
}