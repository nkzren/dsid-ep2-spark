package org.cansados.controller;

import org.cansados.model.InventoryItem;
import org.cansados.model.YearPeriod;
import org.cansados.service.inventory.InventoryService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@RequestScoped
@Path("inventory")
public class InventoryController {

    @Inject
    InventoryService inventory;

    @GET
    public List<InventoryItem> getByYearPeriodAndInventoryId(
            @QueryParam("from") Integer from,
            @QueryParam("to") Integer to,
            @QueryParam("id") String inventoryId
    ) {
        return inventory.listByYear(new YearPeriod(from, to), inventoryId);
    }
}
