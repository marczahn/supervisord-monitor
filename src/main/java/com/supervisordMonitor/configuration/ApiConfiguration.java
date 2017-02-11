package com.supervisordMonitor.configuration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.supervisordMonitor.domain.supervisor.NodeConfiguration;
import com.supervisordMonitor.domain.supervisor.SupervisorApi;
import com.supervisordMonitor.domain.supervisor.SupervisorApiMap;
import com.supervisordMonitor.domain.supervisor.connection.ApiConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@Configuration
public class ApiConfiguration {
    @Autowired
    Gson gson;

    @Autowired
    Environment environment;

    @Bean
    public SupervisorApiMap supervisorApiMap(ApplicationArguments arguments) {
        SupervisorApiMap map = new SupervisorApiMap();
        String filename = environment.getProperty("config");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filename);
        } catch (FileNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
        }
        JsonReader jsonReader = new JsonReader(fileReader);
        List<NodeConfiguration> configurations = gson.fromJson(
            jsonReader,
            new TypeToken<List<NodeConfiguration>>(){}.getType()
        );
        configurations.forEach(entry -> {
            ApiConnector apiConnector = null;
            try {
                apiConnector = new ApiConnector(entry);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            SupervisorApi api = new SupervisorApi(apiConnector);

            map.put(entry.getName(), api);
        });


        return map;
    }
}
