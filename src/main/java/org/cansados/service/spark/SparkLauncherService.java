package org.cansados.service.spark;

import org.apache.spark.launcher.SparkLauncher;
import org.cansados.aggregations.WordCounter;
import org.cansados.service.common.CansadosConfig;

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
                .setMaster("local[*]")
                .setSparkHome(config.getSparkHome())
                .setAppResource("dsid-ep2-spark-1.0.0-SNAPSHOT.jar");
    }


    public void wordCount() {
        try {
            this.launcher.setMainClass(WordCounter.class.getCanonicalName());
            Process process = this.launcher.launch();
            process.waitFor();
            System.out.println("Finished task");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
