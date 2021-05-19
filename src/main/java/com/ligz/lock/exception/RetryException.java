package com.ligz.lock.exception;

public class RetryException extends RuntimeException {

    public RetryException(String message) {
        super(message);
    }
}
