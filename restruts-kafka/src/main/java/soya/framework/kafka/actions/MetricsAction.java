package soya.framework.kafka.actions;

import org.apache.kafka.common.Metric;
import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;

import java.util.Collection;

@OperationMapping(domain = "kafka",
        name = "admin-metrics",
        path = "/kafka/admin/metrics",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON)
public class MetricsAction extends KafkaAction<Metric[]> {

    @Override
    public Metric[] execute() throws Exception {
        Collection<? extends Metric> results = adminClient().metrics().values();
        return results.toArray(new Metric[results.size()]);
    }
}
