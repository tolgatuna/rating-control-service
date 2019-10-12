package com.duriancodes.ratingcontrolservice.advice;

import com.duriancodes.ratingcontrolservice.exception.BookNotFoundException;
import com.tdd.book.swagger.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException be) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode("404");
        errorResponse.setMessage(be.getMessage());
        return handleExceptionInternal(be, errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, null);
    }
}
