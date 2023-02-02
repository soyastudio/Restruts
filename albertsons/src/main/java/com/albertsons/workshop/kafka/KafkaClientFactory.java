package com.albertsons.workshop.kafka;

import java.util.HashMap;
import java.util.Map;

public abstract class KafkaClientFactory {
    protected Map<String, KafkaClient> clients = new HashMap();

    public KafkaClient get(String name) {
        if(!clients.containsKey(name)) {
            throw new IllegalArgumentException("Kafka client is not defined: " + name);
        }
        return clients.get(name);
    }
}
