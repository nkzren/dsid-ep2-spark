package org.cansados.service.inventory;

import org.cansados.model.db.InventoryItem;
import org.cansados.model.YearPeriod;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class InventoryService {

    public List<InventoryItem> listByYear(YearPeriod period, String id) {
        String baseQuery = "{ $and: [ { ID: ?1 }, { YEAR: { $gte : ?2 } }, { YEAR: { $lte : ?3 } }] }";

        return InventoryItem.find(baseQuery, id, period.getFrom(), period.getTo()).list();
    }
}
