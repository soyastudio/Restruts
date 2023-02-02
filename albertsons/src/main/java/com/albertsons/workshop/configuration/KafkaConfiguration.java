package com.albertsons.workshop.configuration;

import com.albertsons.workshop.kafka.KafkaClient;
import com.albertsons.workshop.kafka.KafkaClientFactory;
import org.springframework.context.annotation.Configuration;
import soya.framework.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

@Configuration
public class KafkaConfiguration extends KafkaClientFactory {

    @PostConstruct
    void init() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        properties.load(classLoader.getResourceAsStream("kafka-config.properties"));

        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            String path = properties.getProperty(name);

            Properties prop = new Properties();
            prop.load(classLoader.getResourceAsStream(path));

            clients.put(name, KafkaClient.create(prop));
        }

    }

}
