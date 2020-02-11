package com.revolut.backend.task.api.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/test")
public class TestController {

    @GET
    @Produces(TEXT_PLAIN)
    public String test() {
        return "IM OK";
    }
}
