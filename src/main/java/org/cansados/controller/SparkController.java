package org.cansados.controller;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.PanacheQuery;
import org.apache.spark.launcher.SparkAppHandle;
import org.cansados.model.YearPeriod;
import org.cansados.model.db.AverageItem;
import org.cansados.service.spark.SparkLauncherService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.*;

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
    public Response getAverage(
            @QueryParam("from") Integer from,
            @QueryParam("to") Integer to,
            @QueryParam("id") String inventoryId
    ) {
        YearPeriod period = new YearPeriod(from, to);

        // Deletes all related items before starting new calculation
        String baseQuery = "{ $and: [ { inventoryId: ?1 }, { year: { $gte : ?2 } }, { year: { $lte : ?3 } }] }";
        AverageItem.delete(baseQuery, inventoryId, from, to);

        Optional<SparkAppHandle> maybeHandle = launcherService.average(period, inventoryId);
        if (maybeHandle.isEmpty()) {
            return Response
                    .status(INTERNAL_SERVER_ERROR)
                    .entity("Error on spark launcher execution")
                    .build();
        } else {
            SparkAppHandle handle = maybeHandle.get();
            if (!handle.getState().equals(SparkAppHandle.State.FINISHED)) {
                return Response
                        .status(INTERNAL_SERVER_ERROR)
                        .entity("Error on spark launcher. Status: " + handle.getState().toString())
                        .build();
            }
            List<AverageItem> result = AverageItem.find(baseQuery, inventoryId, from, to).list();

            return Response.ok(result).build();
        }
    }
}
