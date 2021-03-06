package com.revolut.backend.task.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static org.eclipse.jetty.http.HttpStatus.BAD_REQUEST_400;

@Provider
public class MissedCurrencyIdException extends RuntimeException implements ExceptionMapper<MissedCurrencyIdException> {
    public static final String MESSAGE = "Currency id is not specified.";

    public MissedCurrencyIdException() {
        super(MESSAGE);
    }

    @Override
    public Response toResponse(MissedCurrencyIdException exception) {
        return Response.status(BAD_REQUEST_400).entity(MESSAGE).build();
    }
}
