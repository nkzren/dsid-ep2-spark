package org.cansados.model.db;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import javax.enterprise.inject.Model;

@Model
@MongoEntity(collection = "averages", database = "dsid")
public class AverageItem extends PanacheMongoEntity {
    @BsonProperty("inventoryId")
    private String inventoryId;

    @BsonProperty("year")
    private Integer year;

    @BsonProperty("avg")
    private Double average;

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }
}
