package com.paypal.bfs.test.bookingserv.utils;

import org.springframework.http.HttpStatus;

/**
 * Application will throw BookingException for any issues in input/user errors.
 */
public class BookingException extends RuntimeException {

    private final String errorCode;
    private final String message;
    private final HttpStatus status;


    public BookingException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
