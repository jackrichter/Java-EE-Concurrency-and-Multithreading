package com.linkedin.concurrency.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("resource")
public class GreetResource {

    @GET
    public String greetUser() {
        return "Java EE Concurrency starts!";
    }
}
