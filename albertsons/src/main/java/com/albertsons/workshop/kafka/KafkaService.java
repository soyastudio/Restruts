package com.albertsons.workshop.kafka;

import java.util.Map;
import java.util.Objects;

public class KafkaService {
    protected Map<String, KafkaClient> clients;

    public KafkaService(Map<String, KafkaClient> clients) {
        this.clients = clients;

    }

    public KafkaClient get(String name) {
        Objects.requireNonNull(name);

        String key = name.toUpperCase();
        if (!clients.containsKey(key)) {
            throw new IllegalArgumentException("Kafka client is not defined: " + name);
        }
        return clients.get(key);
    }

}
