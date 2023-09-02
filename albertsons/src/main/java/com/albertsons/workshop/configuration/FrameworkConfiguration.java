package com.albertsons.workshop.configuration;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class FrameworkConfiguration {

    @PostConstruct
    void init() throws Exception {
        ClassPathScanningCandidateComponentProvider componentProvider;
    }
}
