package org.cansados.model.db;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import javax.enterprise.inject.Model;

@Model
@MongoEntity(collection = "stdev", database = "dsid")
public class StdevItem extends PanacheMongoEntity {

    @BsonProperty("inventoryId")
    private String inventoryId;

    @BsonProperty("year")
    private Integer year;

    @BsonProperty("month")
    private Integer month;

    @BsonProperty("stdev")
    private Double stdev;

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

    public Double getStdev() {
        return stdev;
    }

    public void setStdev(Double stdev) {
        this.stdev = stdev;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

}
