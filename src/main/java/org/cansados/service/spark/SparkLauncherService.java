package org.cansados.service.spark;

import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.cansados.aggregations.WordCounter;
import org.cansados.service.common.CansadosConfig;
import org.cansados.service.spark.listener.DefaultSparkListener;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;

@RequestScoped
public class SparkLauncherService {

    SparkLauncher launcher;

    @Inject
    public SparkLauncherService(CansadosConfig config) {
        super();
        this.launcher = new SparkLauncher()
                .setMaster("local[5]")
                .setSparkHome(config.getSparkHome())
                .setAppResource(config.getAggregationsPath());
    }


    public void wordCount() {
        try {
            this.launcher.setMainClass(WordCounter.class.getCanonicalName());
            SparkAppHandle handle = this.launcher.startApplication(new DefaultSparkListener());
            handle.wait();
            System.out.println("Finished task");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
