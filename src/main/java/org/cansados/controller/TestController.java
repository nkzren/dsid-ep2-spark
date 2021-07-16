package org.cansados.controller;

import org.cansados.service.spark.WordCounterService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("test")
public class TestController {

    @Inject
    WordCounterService wordCounter;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        wordCounter.start();
        return "ae";
    }
}
