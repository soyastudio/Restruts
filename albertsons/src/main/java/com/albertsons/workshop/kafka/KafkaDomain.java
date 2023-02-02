package com.albertsons.workshop.kafka;

import soya.framework.action.Domain;

@Domain(
        name = "kafka",
        path = "/kafka",
        title = "Kafka Service",
        description = "Kafka Service"
)
public interface KafkaDomain {
}
