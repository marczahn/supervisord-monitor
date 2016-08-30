package com.supervisordMonitor.domain;

import com.supervisordMonitor.domain.supervisor.dto.StateDto;

public class NodeStateFetchedEvent {
    private String nodeName;

    private StateDto state;

    public NodeStateFetchedEvent() {
    }

    public NodeStateFetchedEvent(String nodeName, StateDto state) {
        this.nodeName = nodeName;
        this.state = state;
    }

    public String getNodeName() {
        return nodeName;
    }

    public StateDto getState() {
        return state;
    }
}
