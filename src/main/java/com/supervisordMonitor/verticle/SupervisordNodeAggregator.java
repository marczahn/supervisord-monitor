package com.supervisordMonitor.verticle;

import com.google.gson.Gson;
import com.supervisordMonitor.domain.*;
import io.vertx.core.AbstractVerticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupervisordNodeAggregator extends AbstractVerticle {
    @Autowired
    Gson gson;

    @Autowired
    NodeRegistry nodeRegistry;

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("supervisordMonitor.node.connectionFailed", message -> {
            try {
                ConnectionErrorNodeEvent event = gson.fromJson(message.body().toString(), ConnectionErrorNodeEvent.class);
                handleConnectionError(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        vertx.eventBus().consumer("supervisordMonitor.node.stateFetched", message -> {
            try {
                NodeStateFetchedEvent event = gson.fromJson(
                    message.body().toString(),
                    NodeStateFetchedEvent.class
                );
                handleState(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        vertx.eventBus().consumer("supervisordMonitor.node.processesFetched", message -> {
            try {
                String body = message.body().toString();
                ProcessesFetchedNodeEvent event = gson.fromJson(body, ProcessesFetchedNodeEvent.class);
                handleProcesses(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handleState(NodeStateFetchedEvent event) throws Exception {
        Node node = getOrCreate(event.getNodeName());
        node.setAlive(true);
        node.setConnectionError(null);
        node.setState(event.getState());
    }

    private void handleProcesses(ProcessesFetchedNodeEvent event) throws Exception {
        Node node = getOrCreate(event.getNodeName());
        node.setAlive(true);
        node.setConnectionError(null);
        node.setProcesses(event.getProcesses());
    }

    private void handleConnectionError(ConnectionErrorNodeEvent nodeEvent) throws Exception {
        Node node = getOrCreate(nodeEvent.getNodeName());
        node.reset();
        node.setAlive(false);
        node.setConnectionError(nodeEvent.getData());
    }

    private Node getOrCreate(String nodeName) throws Exception {
        if (!nodeRegistry.has(nodeName)) {
            nodeRegistry.add(nodeName);
        }

        return nodeRegistry.get(nodeName);
    }
}
