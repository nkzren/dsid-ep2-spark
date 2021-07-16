package org.cansados.service.spark;

import org.apache.spark.launcher.SparkLauncher;
import org.cansados.service.common.CansadosConfig;
import org.cansados.spark.functions.WordCounter;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;

@RequestScoped
public class WordCounterService {

    SparkLauncher launcher;

    @Inject
    public WordCounterService(CansadosConfig config) {
        super();
        this.launcher = new SparkLauncher()
                .setMaster("local[*]")
                .setSparkHome(config.getSparkHome())
                .setAppResource("dsid-ep2-spark-1.0.0-SNAPSHOT.jar")
                .setMainClass(WordCounter.class.getCanonicalName());
    }


    public void start() {
        try {
            Process process = this.launcher.launch();
            process.waitFor();
            System.out.println("Finished task");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
