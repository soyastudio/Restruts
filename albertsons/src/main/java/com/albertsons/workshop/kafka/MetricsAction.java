package com.albertsons.workshop.kafka;

import org.apache.kafka.common.Metric;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import java.util.Collection;

@ActionDefinition(domain = "kafka",
        name = "admin-metrics",
        path = "/admin/metrics",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON)
public class MetricsAction extends KafkaAction<Metric[]> {

    @Override
    public Metric[] execute() throws Exception {
        Collection<? extends Metric> results = adminClient().metrics().values();
        return results.toArray(new Metric[results.size()]);
    }
}
