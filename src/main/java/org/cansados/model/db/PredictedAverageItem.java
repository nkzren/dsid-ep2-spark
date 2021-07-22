package org.cansados.model.db;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import javax.enterprise.inject.Model;

@Model
@MongoEntity(collection = "predicted_avg", database = "dsid")
public class PredictedAverageItem extends PanacheMongoEntity {
    @BsonProperty("inventoryId")
    private String inventoryId;

    @BsonProperty("year")
    private Integer year;

    @BsonProperty("month")
    private Integer month;

    @BsonProperty("avg")
    private Double average;

    @BsonProperty("predicted_avg")
    private Double predictedAvg;

    public Double getPredictedAvg() {
        return predictedAvg;
    }

    public void setPredictedAvg(Double predictedAvg) {
        this.predictedAvg = predictedAvg;
    }

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

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }
}
