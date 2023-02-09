package com.albertsons.workshop.configuration;

import com.albertsons.workshop.kafka.KafkaClient;
import com.albertsons.workshop.kafka.KafkaPubSubTestRunner;
import com.albertsons.workshop.kafka.KafkaService;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;

@Configuration
public class WorkspaceConfiguration {

    @Autowired
    Environment environment;

    @Bean()
    Workspace workspace() {
        return new Workspace(new File(environment.getProperty("workspace.home")));
    }

    @Bean()
    KafkaService kafkaService() throws IOException {
        Map<String, KafkaClient> clients = new HashMap<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        properties.load(classLoader.getResourceAsStream("kafka-config.properties"));

        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            String path = properties.getProperty(name);

            Properties prop = new Properties();
            prop.load(classLoader.getResourceAsStream(path));

            clients.put(name.toUpperCase(), KafkaClient.create(prop));
        }

        return new KafkaService(clients);
    }

    @Bean
    KafkaPubSubTestRunner kafkaPubSubTestRunner(KafkaService kafkaService, Workspace workspace) {
        return new KafkaPubSubTestRunner(kafkaService, workspace);
    }

}
