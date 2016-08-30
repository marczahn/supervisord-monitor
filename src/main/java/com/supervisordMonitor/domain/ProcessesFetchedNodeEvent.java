package com.supervisordMonitor.domain;

import com.supervisordMonitor.domain.supervisor.dto.ProcessDto;

public class ProcessesFetchedNodeEvent {
    private String nodeName;

    private ProcessDto[] processes;

    public ProcessesFetchedNodeEvent() {
    }

    public ProcessesFetchedNodeEvent(String nodeName, ProcessDto[] processes) {
        this.nodeName = nodeName;
        this.processes = processes;
    }

    public String getNodeName() {
        return nodeName;
    }

    public ProcessDto[] getProcesses() {
        return processes;
    }
}
