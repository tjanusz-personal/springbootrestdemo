package com.demo.springbootrestdemo.web;

import com.demo.springbootrestdemo.domain.DemoWebError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Global exception handler advice that will permit adding exception information into the result body.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle any IllegalArgumentException cases and map to 400 and can add whatever body we want here for JSON response.
     * @param ex - IllegalArgument exception causing this whole mess
     * @param request - original WebRequest
     * @return
     * @throws JsonProcessingException - if can't serialize the error (probably don't want this for real but this is
     * just explanatory anyway.
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleInvalidArguments(RuntimeException ex, WebRequest request) throws
            JsonProcessingException {
        DemoWebError webError = new DemoWebError(HttpStatus.BAD_REQUEST.toString(), ex.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyOfResponse = objectMapper.writeValueAsString(webError);
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handle generic exception cases and map to 500 and can add whatever body we want here for JSON response.
     * @param ex - Generic runtime exception causing this mess
     * @param request - original WebRequest
     * @return
     * @throws JsonProcessingException - if can't serialize the error
     */
    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleRuntimeError(RuntimeException ex, WebRequest request) throws
            JsonProcessingException {
        DemoWebError webError = new DemoWebError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyOfResponse = objectMapper.writeValueAsString(webError);
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}

