package org.cansados.service.spark;

import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.cansados.aggregations.WordCounter;
import org.cansados.service.common.CansadosConfig;
import org.cansados.service.spark.listener.SyncSparkListener;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@RequestScoped
public class SparkLauncherService {

    SparkLauncher launcher;

    CountDownLatch countdown;

    @Inject
    public SparkLauncherService(CansadosConfig config) {
        super();
        this.launcher = new SparkLauncher()
                .setMaster("local[5]")
                .setSparkHome(config.getSparkHome())
                .setAppResource(config.getAggregationsPath());
        this.countdown = new CountDownLatch(1);
    }


    public void wordCount() {
        try {
            this.launcher.setMainClass(WordCounter.class.getCanonicalName());
            SparkAppHandle.Listener handle = new SyncSparkListener(this.countdown);
            this.launcher.startApplication(handle);

            countdown.await();
            System.out.println("Finished task");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
