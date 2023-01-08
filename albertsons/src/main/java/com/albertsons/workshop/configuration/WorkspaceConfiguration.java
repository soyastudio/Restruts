package com.albertsons.workshop.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;

@Configuration
public class WorkspaceConfiguration {

    @Autowired
    Environment environment;

    @Bean()
    Workspace workspace() {
        return new Workspace(new File(environment.getProperty("workspace.home")));
    }
}
