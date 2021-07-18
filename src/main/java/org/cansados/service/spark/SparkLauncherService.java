package org.cansados.service.spark;

import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.cansados.aggregations.AverageAggregator;
import org.cansados.aggregations.WordCounter;
import org.cansados.model.YearPeriod;
import org.cansados.service.common.CansadosConfig;
import org.cansados.service.spark.listener.SyncSparkListener;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RequestScoped
public class SparkLauncherService {

    SparkLauncher launcher;

    CountDownLatch countdown;

    CansadosConfig config;

    @Inject
    public SparkLauncherService(CansadosConfig config) {
        super();
        this.launcher = new SparkLauncher()
                .setMaster("local[5]")
                .setSparkHome(config.getSparkHome())
                .setAppResource(config.getAggregationsPath());
        this.config = config;
    }


    public void wordCount() {
        this.countdown = new CountDownLatch(1);
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

    public void average(YearPeriod period, String inventoryId) {
        this.countdown = new CountDownLatch(1);
        try {
            this.launcher
                    .setMainClass(AverageAggregator.class.getCanonicalName())
                    .addAppArgs(buildArgs(period, inventoryId));

            SparkAppHandle.Listener handle = new SyncSparkListener(this.countdown);
            this.launcher.startApplication(handle);

            countdown.await();
            System.out.println("Finished task");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String[] buildArgs(YearPeriod period, String inventoryId) {
        List<String> argList = new ArrayList<>();
        for (int currentYear = period.getFrom(); currentYear <= period.getTo(); currentYear++) {
            StringBuilder sb = new StringBuilder(this.config.getDatasourceUrl());
            if (!this.config.getDatasourceUrl().endsWith("/")) {
                sb.append("/");
            }
            sb.append(currentYear).append("/");
            sb.append(inventoryId).append(".csv");

            argList.add(sb.toString());
        }

        return argList.toArray(String[]::new);
    }

}
