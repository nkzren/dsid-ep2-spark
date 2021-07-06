package org.cansados.service.spark;

public class AverageAggregator extends SparkContextBase {

    public AverageAggregator() {
        super();
        this.launcher.setMainClass(this.getClass().getCanonicalName());
    }

}
