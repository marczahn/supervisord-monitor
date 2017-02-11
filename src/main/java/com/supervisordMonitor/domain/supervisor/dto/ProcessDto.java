package com.supervisordMonitor.domain.supervisor.dto;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect
public class ProcessDto {
    private String name;

    private String group;

    private String description;

    private Integer start;

    private Integer stop;

    private Integer now;

    private StateDto state;

    private String spawnError;

    private Integer exitStatus;

    private Integer pid;

    public ProcessDto(Map<String, Object> response) {
        name = response.getOrDefault("name", "Undefined").toString();
        group = response.getOrDefault("group", "Undefined").toString();
//        description = response.getOrDefault("description", "Undefined").toString();
        start = response.containsKey("start") ? (Integer)response.get("start") : null;
        stop = response.containsKey("stop") ? (Integer)response.get("stop") : null;
//        now = response.containsKey("now") ? (Integer)response.get("now") : null;
        state = new StateDto(response);
        spawnError = response.getOrDefault("spawnerr", "").toString();
        exitStatus = (Integer) response.getOrDefault("exitstatus", null);
        pid = (Integer) response.getOrDefault("pid", null);
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getDescription() {
        return description;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getStop() {
        return stop;
    }

    public Integer getNow() {
        return now;
    }

    public StateDto getState() {
        return state;
    }

    public String getSpawnError() {
        return spawnError;
    }

    public Integer getExitStatus() {
        return exitStatus;
    }

    public Integer getPid() {
        return pid;
    }
}
