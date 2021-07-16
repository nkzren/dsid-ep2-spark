package org.cansados.service.spark.listener;

import org.apache.spark.launcher.SparkAppHandle;

import java.util.concurrent.CountDownLatch;

public class SyncSparkListener implements SparkAppHandle.Listener {

    // CDI eyes only
    @Deprecated
    public SyncSparkListener() {
    }

    public SyncSparkListener(CountDownLatch countdown) {
        this.countdown = countdown;
    }

    CountDownLatch countdown;

    @Override
    public void stateChanged(SparkAppHandle sparkAppHandle) {
        SparkAppHandle.State state = sparkAppHandle.getState();
        System.out.println("Spark app state is: " + state);
        if (state != null && state.isFinal()) {
            countdown.countDown();
        }
    }

    @Override
    public void infoChanged(SparkAppHandle sparkAppHandle) {
        System.out.println("Info changed: " + sparkAppHandle.getState().toString());
    }
}
