package org.cansados.service.spark.listener;

import org.apache.spark.launcher.SparkAppHandle;

public class DefaultSparkListener implements SparkAppHandle.Listener {
    @Override
    public void stateChanged(SparkAppHandle sparkAppHandle) {
        System.out.println("Spark app state is: " + sparkAppHandle.getState().toString());
    }

    @Override
    public void infoChanged(SparkAppHandle sparkAppHandle) {
        System.out.println("Info changed: " + sparkAppHandle.getState().toString());
    }
}
