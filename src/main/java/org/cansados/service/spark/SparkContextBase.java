package org.cansados.service.spark;

import org.apache.spark.launcher.SparkLauncher;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

public abstract class SparkContextBase {

    public static final Logger LOGGER = Logger.getLogger(SparkContextBase.class);

    @ConfigProperty(name = "spark.master.url")
    public String masterUrl;

    public SparkLauncher launcher;

    public SparkContextBase() {
        this.launcher = new SparkLauncher()
                .setMaster(masterUrl);
    }

}
