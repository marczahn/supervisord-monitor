package com.supervisordMonitor.domain.supervisor;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class NodeConfiguration {
    private final String name;

    private final String host;

    private final Integer port;

    private final String username;

    private final String password;

    private final Boolean ssl;

    public NodeConfiguration(String name, String host, Integer port, String username, String password, Boolean ssl) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.ssl = ssl;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean isSsl() {
        return ssl != null ? ssl : false;
    }

    public String getName() {
        return name;
    }
}
