package org.cansados.service.common;

import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@Startup
@ApplicationScoped
public class CansadosConfig {

    @ConfigProperty(name = "spark.home")
    String sparkHome;

    @ConfigProperty(name = "spark.aggregations.jarPath")
    String aggregationsPath;

    public String getSparkHome() {
        return sparkHome;
    }

    public String getAggregationsPath() {
        return aggregationsPath;
    }
}
