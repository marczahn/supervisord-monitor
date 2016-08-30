package com.supervisordMonitor.domain.supervisor.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Map;

@JsonAutoDetect
public class StateDto {
    final private String name;

    final private Integer code;

    public StateDto() {
        name = "";
        code = 0;
    }

    public StateDto(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public StateDto(Map<String, Object> response) {
        name = response.getOrDefault("statename", "Undefined").toString();
        code = (Integer) response.getOrDefault("statecode", -1);
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }
}
