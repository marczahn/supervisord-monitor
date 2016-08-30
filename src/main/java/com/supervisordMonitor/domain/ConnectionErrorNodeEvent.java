package com.supervisordMonitor.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class ConnectionErrorNodeEvent {
    private String nodeName;

    private String data;

    public ConnectionErrorNodeEvent() {
    }

    @JsonCreator
    public ConnectionErrorNodeEvent(@JsonProperty("nodeName") String nodeName, @JsonProperty("data") String data) {
        this.nodeName = nodeName;
        this.data = data;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {

        this.data = data;
    }
}
