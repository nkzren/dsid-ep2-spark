package org.cansados.controller;

import org.cansados.model.InventoryItem;
import org.cansados.model.YearPeriod;
import org.cansados.service.inventory.InventoryService;
import org.cansados.service.spark.SparkLauncherService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@RequestScoped
@Path("spark")
public class SparkController {

    @Inject
    SparkLauncherService launcherService;

    @Inject
    InventoryService inventory;

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
    public Map<String, Object> getAverage(
            @QueryParam("from") Integer from,
            @QueryParam("to") Integer to,
            @QueryParam("id") String inventoryId
    ) {
        YearPeriod period = new YearPeriod(from, to);
        launcherService.average(period, inventoryId);

        List<InventoryItem> items = inventory.listByYear(period, inventoryId);
        return Map.of("years", items.stream().map(item -> {
            Integer year = item.getYear();
            Double average = 42.0;
            return Map.of("year", year, "average", average);
        }));
    }
}
