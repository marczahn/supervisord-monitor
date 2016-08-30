package com.supervisordMonitor.verticle;

import com.google.gson.Gson;
import com.supervisordMonitor.domain.ConnectionErrorNodeEvent;
import com.supervisordMonitor.domain.NodeStateFetchedEvent;
import com.supervisordMonitor.domain.ProcessesFetchedNodeEvent;
import com.supervisordMonitor.domain.supervisor.SupervisorApi;
import com.supervisordMonitor.domain.supervisor.SupervisorApiMap;
import com.supervisordMonitor.domain.supervisor.dto.ProcessDto;
import com.supervisordMonitor.domain.supervisor.dto.StateDto;
import io.vertx.core.AbstractVerticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Map;

@Component
public class SupervisordNodeCollector extends AbstractVerticle {

    @Autowired
    private SupervisorApiMap supervisorApiMap;

    @Autowired
    Gson gson;

    @Override
    public void start() throws Exception {
        vertx.setPeriodic(5000, id -> {
            for (Map.Entry<String, SupervisorApi> entry : supervisorApiMap.entrySet()) {
                String nodeName = entry.getKey();
                SupervisorApi api = entry.getValue();

                Observable<StateDto> response = api.getState();
                response.subscribe(
                    state -> {
                        vertx.eventBus().publish(
                            "supervisordMonitor.node.stateFetched",
                            gson.toJson(new NodeStateFetchedEvent(nodeName, state))
                        );
                        fetchInformation(api, nodeName);
                    },
                    e -> {
                        vertx.eventBus().publish(
                            "supervisordMonitor.node.connectionFailed",
                            gson.toJson(new ConnectionErrorNodeEvent(nodeName, e.getMessage()))
                        );
                    }
                );
            }
        });
    }

    private void fetchInformation(SupervisorApi api, String nodeName) {
        Observable<ProcessDto[]> nodeProcesses = api.getAllProcessInfo();
        nodeProcesses.subscribe(
            processes -> {
                vertx.eventBus().publish(
                    "supervisordMonitor.node.processesFetched",
                    gson.toJson(new ProcessesFetchedNodeEvent(nodeName, processes))
                );
            },
            e -> {
                System.out.println(e.getMessage());
            }
        );
    }
}
