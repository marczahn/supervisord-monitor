package com.supervisordMonitor;

import com.supervisordMonitor.verticle.HttpServer;
import com.supervisordMonitor.verticle.SupervisordNodeAggregator;
import com.supervisordMonitor.verticle.SupervisordNodeCollector;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    @Autowired
    private HttpServer httpServer;

    @Autowired
    private SupervisordNodeCollector collector;

    @Autowired
    private SupervisordNodeAggregator aggregator;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void initVertx() {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(httpServer);
        vertx.deployVerticle(collector);
        vertx.deployVerticle(aggregator);
    }
}