package com.revolut.backend.task.exception;

public class MissedCurrencyIdException extends RuntimeException {
    private static final String MESSAGE = "Currency id is not specified.";

    public MissedCurrencyIdException() {
        super(MESSAGE);
    }
}
