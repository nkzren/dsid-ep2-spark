package org.cansados.service.spark;

import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.cansados.aggregations.*;
import org.cansados.model.db.InventoryItem;
import org.cansados.model.YearPeriod;
import org.cansados.service.common.CansadosConfig;
import org.cansados.service.inventory.InventoryService;
import org.cansados.service.spark.listener.SyncSparkListener;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@RequestScoped
public class SparkLauncherService {

    @Inject
    InventoryService inventoryService;

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

    public Optional<SparkAppHandle> average(YearPeriod period, String inventoryId, String groupBy, String columnName) {
        this.countdown = new CountDownLatch(1);
        try {
            this.launcher.addAppArgs(buildArgs(period, inventoryId, columnName));

            if ("year".equals(groupBy)) {
                this.launcher.setMainClass(AverageAggregatorByYear.class.getCanonicalName());
            } else if ("month".equals(groupBy)) {
                this.launcher.setMainClass(AverageAggregatorByMonth.class.getCanonicalName());
            } else {
                throw new IllegalArgumentException("Invalid groupBy value: " + groupBy);
            }

            SparkAppHandle.Listener listener = new SyncSparkListener(this.countdown);
            SparkAppHandle handle = this.launcher.startApplication(listener);

            countdown.await();
            System.out.println("Finished task");
            return Optional.of(handle);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<SparkAppHandle> stdev(YearPeriod period, String inventoryId, String groupBy, String columnName) {
        this.countdown = new CountDownLatch(1);
        try {
            this.launcher.addAppArgs(buildArgs(period, inventoryId, columnName));

            if ("year".equals(groupBy)) {
                this.launcher.setMainClass(StdevAggregatorByYear.class.getCanonicalName());
            } else if ("month".equals(groupBy)) {
                this.launcher.setMainClass(StdevAggregatorByMonth.class.getCanonicalName());
            } else {
                throw new IllegalArgumentException("Invalid groupBy value: " + groupBy);
            }

            SparkAppHandle.Listener listener = new SyncSparkListener(this.countdown);
            SparkAppHandle handle = this.launcher.startApplication(listener);

            countdown.await();
            System.out.println("Finished task");
            return Optional.of(handle);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private String[] buildArgs(YearPeriod period, String inventoryId, String columnName) {
        List<String> argList = new ArrayList<>();

        // DO NOT CHANGE THE ORDER OF THE ADDS. AGGREGATOR FUNCTION TAKES THOSE 4 ARGUMENTS INTO CONSIDERATION
        argList.add(config.getMongoConnection());

        argList.add(config.getAwsAccessKey());
        argList.add(config.getAwsSecretKey());

        argList.add(inventoryId);

        argList.add(columnName);

        List<Integer> yearsAvailable = inventoryService.listByYear(period, inventoryId).stream()
                .map(InventoryItem::getYear)
                .collect(Collectors.toList());

        for (Integer currentYear : yearsAvailable) {
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
