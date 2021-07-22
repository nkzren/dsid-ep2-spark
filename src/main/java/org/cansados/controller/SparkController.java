package org.cansados.controller;

import org.apache.spark.launcher.SparkAppHandle;
import org.cansados.model.YearPeriod;
import org.cansados.model.db.AverageItem;
import org.cansados.model.db.PredictedAverageItem;
import org.cansados.model.db.StdevItem;
import org.cansados.service.spark.SparkLauncherService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

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
    @Path("average/{groupBy}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAverage(
            @PathParam("groupBy") String groupBy,
            @QueryParam("from") Integer from,
            @QueryParam("to") Integer to,
            @QueryParam("id") String inventoryId,
            @QueryParam("columnName") String columnName
    ) {
        YearPeriod period = new YearPeriod(from, to);

        // Deletes all related items before starting new calculation
        String baseQuery = "{ inventoryId: ?1 }";
        AverageItem.delete(baseQuery, inventoryId);

        Optional<SparkAppHandle> maybeHandle = launcherService.average(period, inventoryId, groupBy, columnName);
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

    @GET
    @Path("stdev/{groupBy}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStdev(
            @PathParam("groupBy") String groupBy,
            @QueryParam("from") Integer from,
            @QueryParam("to") Integer to,
            @QueryParam("id") String inventoryId,
            @QueryParam("columnName") String columnName
    ) {
        YearPeriod period = new YearPeriod(from, to);

        // Deletes all related items before starting new calculation
        String baseQuery = "{ $and: [ { inventoryId: ?1 }, { year: { $gte: ?2, $lte: ?3 } } ] }";
        StdevItem.delete(baseQuery, inventoryId, from, to);

        Optional<SparkAppHandle> maybeHandle = launcherService.stdev(period, inventoryId, groupBy, columnName);
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
            List<StdevItem> result = StdevItem.find(baseQuery, inventoryId, from, to).list();

            return Response.ok(result).build();
        }
    }

    @GET
    @Path("predict_avg")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrediction(
            @QueryParam("from") Integer from,
            @QueryParam("to") Integer to,
            @QueryParam("id") String inventoryId,
            @QueryParam("toPredict") String toPredict,
            @QueryParam("auxValue") String auxValue
    ) {
        YearPeriod period = new YearPeriod(from, to);

        // Deletes all related items before starting new calculation
        String baseQuery = "{ $and: [ { inventoryId: ?1 }, { year: { $gte: ?2, $lte: ?3 } } ] }";
        PredictedAverageItem.delete(baseQuery, inventoryId, from, to);

        Optional<SparkAppHandle> maybeHandle = launcherService.leastSquares(period, inventoryId, toPredict, auxValue);
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
            List<StdevItem> result = PredictedAverageItem.find(baseQuery, inventoryId, from, to).list();

            return Response.ok(result).build();
        }
    }
}
