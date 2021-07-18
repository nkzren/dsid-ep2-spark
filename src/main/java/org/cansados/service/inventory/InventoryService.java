package org.cansados.service.inventory;

import com.mongodb.client.MongoClient;
import org.cansados.model.InventoryItem;
import org.cansados.model.YearPeriod;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.QueryParam;
import java.util.List;

@ApplicationScoped
public class InventoryService {

    @Inject
    MongoClient client;

    public List<InventoryItem> listByYear(YearPeriod period, String id) {
        String baseQuery = "{ $and: [ { ID: ?1 }, { YEAR: { $gte : ?2 } }, { YEAR: { $lte : ?3 } }] }";

        return InventoryItem.find(baseQuery, id, period.getFrom(), period.getTo()).list();
    }
}
