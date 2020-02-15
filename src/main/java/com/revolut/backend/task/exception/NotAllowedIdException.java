package com.revolut.backend.task.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static org.eclipse.jetty.http.HttpStatus.BAD_REQUEST_400;

@Provider
public class NotAllowedIdException extends RuntimeException implements ExceptionMapper<NotAllowedIdException> {
    public static final String MESSAGE = "You are not allowed to predefine account id before creation.";

    public NotAllowedIdException() {
        super(MESSAGE);
    }

    @Override
    public Response toResponse(NotAllowedIdException exception) {
        return Response.status(BAD_REQUEST_400).entity(MESSAGE).build();
    }
}
