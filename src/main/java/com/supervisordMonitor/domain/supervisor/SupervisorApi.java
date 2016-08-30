package com.supervisordMonitor.domain.supervisor;

import com.supervisordMonitor.domain.supervisor.connection.ApiConnector;
import com.supervisordMonitor.domain.supervisor.dto.ProcessDto;
import com.supervisordMonitor.domain.supervisor.dto.StateDto;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;

public class SupervisorApi {

    ApiConnector apiConnector;

    public SupervisorApi() {
    }

    public SupervisorApi(ApiConnector apiConnector) {
        this.apiConnector = apiConnector;
    }

    public Observable<StateDto> getState() {
        return apiConnector.send("supervisor.getState").map(response ->
                new StateDto((HashMap<String, Object>) response)
        );
    }

    public Observable<String> getSupervisorVersion() {
        return apiConnector.send("supervisor.getSupervisorVersion").map(response ->
                response.toString()
        );
    }

    public Observable<ProcessDto[]> getAllProcessInfo() {
        return apiConnector.send("supervisor.getAllProcessInfo").map(response -> {
            Object[] mappedResponse = (Object[])response;
            ProcessDto[] processes = new ProcessDto[mappedResponse.length];
            for (int i = 0; i < mappedResponse.length; ++i) {
                processes[i] = new ProcessDto((Map<String, Object>)mappedResponse[i]);
            }

            return processes;
        });
    }
}
