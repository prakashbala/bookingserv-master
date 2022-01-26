package com.paypal.bfs.test.bookingserv.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This handler handles all exceptions in the booking module
 */
@ControllerAdvice
public class BookingExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DateTimeParseException.class})
    protected ResponseEntity<Object> handleBookingException(DateTimeParseException ex, WebRequest webRequest) {
        List<String> errors = prepareDefaultDateTimeErrors();
        CustomError apiError = new CustomError(HttpStatus.BAD_REQUEST, errors, "BOOK-FORM-01");

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @ExceptionHandler({BookingException.class})
    protected ResponseEntity<Object> handleBookingException(BookingException ex, WebRequest webRequest) {
        CustomError apiError = new CustomError(ex.getStatus(), ex.getMessage(), ex.getErrorCode());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleBookingException(Exception ex, WebRequest webRequest) {
        CustomError apiError = new CustomError(HttpStatus.INTERNAL_SERVER_ERROR, "Please try again later!",
                "BOOK-SERV-01");

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = prepareErrors(ex);

        CustomError error = new CustomError(HttpStatus.BAD_REQUEST, errors, "BOOK-MAND-01");

        return new ResponseEntity<>(error, headers, error.getStatus());
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = prepareDefaultDateTimeErrors();
        errors.add("Please verify your input");

        CustomError apiError = new CustomError(HttpStatus.BAD_REQUEST, errors, "BOOK-INV-01");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    private List<String> prepareErrors(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return errors;
    }

    private List<String> prepareDefaultDateTimeErrors() {
        return Stream.of("Date of birth should be in format yyyy-MM-dd",
                "Check in/out time should be in format yyyy-MM-dd'T'HH:mm:ss").collect(Collectors.toList());
    }

    @SuppressWarnings("unused") //getters are required so suppressing unused warnings
    private static class CustomError {

        private final String errorCode;
        private final List<String> details;
        private final HttpStatus status;

        public CustomError(HttpStatus status, String message, String errorCode) {
            super();
            this.errorCode = errorCode;
            this.details = Collections.singletonList(message);
            this.status = status;
        }

        public CustomError(HttpStatus status, List<String> errors, String errorCode) {
            super();
            this.errorCode = errorCode;
            this.details = errors;
            this.status = status;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public List<String> getDetails() {
            return details;
        }

        public String getErrorCode() {
            return errorCode;
        }

    }
}
