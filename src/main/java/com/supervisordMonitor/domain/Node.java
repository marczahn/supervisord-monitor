package com.supervisordMonitor.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.supervisordMonitor.domain.supervisor.dto.ProcessDto;
import com.supervisordMonitor.domain.supervisor.dto.StateDto;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect
public class Node {
    private String connectionError;

    private String name;

    private Boolean alive = false;

    private StateDto state = new StateDto();

    private String supervisorVersion;

    private ProcessDto[] processes;

    public Node(String name) {
        this.name = name;
    }

    public void reset() {
        state = new StateDto();
        alive = false;
        supervisorVersion = null;
        processes = new ProcessDto[0];
        connectionError = null;
    }

    public String getName() {
        return name;
    }

    public Boolean isAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    public StateDto getState() {
        return state;
    }

    public void setState(StateDto state) {
        this.state = state;
    }

    public String getSupervisorVersion() {
        return supervisorVersion;
    }

    public void setSupervisorVersion(String supervisorVersion) {
        this.supervisorVersion = supervisorVersion;
    }

    public void setProcesses(ProcessDto[] processes) {
        this.processes = processes;
    }

    public ProcessDto[] getProcesses() {
        return processes;
    }

    public String getConnectionError() {
        return connectionError;
    }

    public void setConnectionError(String connectionError) {
        this.connectionError = connectionError;
    }
}
