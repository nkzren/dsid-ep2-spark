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

    @ConfigProperty(name = "spark.datasource.baseUrl")
    String datasourceUrl;

    public String getSparkHome() {
        return sparkHome;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public String getAggregationsPath() {
        return aggregationsPath;
    }
}
