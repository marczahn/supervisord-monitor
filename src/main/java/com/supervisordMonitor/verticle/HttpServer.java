package com.supervisordMonitor.verticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.supervisordMonitor.domain.Node;
import com.supervisordMonitor.domain.NodeRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HttpServer extends AbstractVerticle {

    @Autowired
    NodeRegistry nodeRegistry;

    @Autowired
    Gson gson;

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.get("/nodes").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("Content-Type", "application/json");

            try {
                response.setStatusCode(200).end(gson.toJson(nodeRegistry.all()));
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatusCode(500).end(e.getMessage());
            }
        });

        router.get("/nodes/:node").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("Content-Type", "application/json");
            String nodeName = routingContext.request().getParam("node");
            if (!nodeRegistry.has(nodeName)) {
                response.setStatusCode(404).end();

                return;
            }
            Node node = nodeRegistry.get(nodeName);
            try {
                response.setStatusCode(200).end(gson.toJson(node));
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatusCode(500).end(e.getMessage());
            }
        });
//        router.get("/web/*").handler(StaticHandler.create("webroot/build/production/SupervisorMonitor"));
        router.get("/web/*").handler(StaticHandler.create());

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(5001, "127.0.0.1");
    }
}
