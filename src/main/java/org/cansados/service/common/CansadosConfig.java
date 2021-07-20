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

    @ConfigProperty(name = "spark.aws.access.key")
    String awsAccessKey;


    @ConfigProperty(name = "spark.aws.secret.key")
    String awsSecretKey;

    @ConfigProperty(name = "quarkus.mongodb.connection-string")
    String mongoConnection;

    public String getSparkHome() {
        return sparkHome;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public String getAggregationsPath() {
        return aggregationsPath;
    }

    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    public String getMongoConnection() {
        return mongoConnection;
    }
}
