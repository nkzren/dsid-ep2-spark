package org.cansados.controller;

import org.cansados.service.spark.SparkLauncherService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
@Path("spark")
public class SparkController {

    @Inject
    SparkLauncherService launcherService;

    @GET
    @Path("wordCount")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        launcherService.wordCount();
        return "ae";
    }

    @GET
    @Path("average")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getAverage() {
        Map<String, Object> map = new HashMap<>();
        map.put("options", Map.of("test", "value"));
        return map;
    }
}
