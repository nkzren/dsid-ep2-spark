package org.cansados.model.db;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import javax.enterprise.inject.Model;

@Model
@MongoEntity(collection = "inventory", database = "dsid")
public class InventoryItem extends PanacheMongoEntity {

    @BsonProperty("ID")
    String inventoryId;

    @BsonProperty("YEAR")
    Integer year;

    public String getInventoryId() {
        return inventoryId;
    }

    public Integer getYear() {
        return year;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
