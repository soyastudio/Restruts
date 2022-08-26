package soya.framework.restruts.actions.kafka;

import org.apache.kafka.common.Metric;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;

import java.util.Collection;

@OperationMapping(domain = "kafka",
        name = "metrics",
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
