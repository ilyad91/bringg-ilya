package com.candidate.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler({ BookNotFoundException.class })
//    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
//        return handleExceptionInternal(ex, "Book not found",
//                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
//    }

}
