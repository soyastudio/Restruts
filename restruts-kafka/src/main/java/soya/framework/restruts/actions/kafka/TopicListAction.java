package soya.framework.restruts.actions.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.ParameterMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@OperationMapping(domain = "kafka",
        name = "topics",
        path = "/kafka/topics",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON)
public class TopicListAction extends KafkaAction<String[]> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM)
    private String query;

    @Override
    public String[] execute() throws Exception {
        try {
            List<String> topics = topics(adminClient());
            if (query != null) {
                List<String> filtered = filter(query, topics);
                return filtered.toArray(new String[filtered.size()]);

            } else {
                return topics.toArray(new String[topics.size()]);

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



    private List<String> filter(String query, List<String> results) {
        List<String> filtered = new ArrayList<>();
        results.forEach(e -> {
            if (match(e, query)) {
                filtered.add(e);
            }
        });
        return filtered;
    }

    private boolean match(String value, String patten) {
        return value.startsWith(patten);
    }

    private List<String> topics(AdminClient adminClient) {
        Future<Set<String>> future = adminClient.listTopics().names();
        while (!future.isDone()) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            List<String> results = new ArrayList<>(future.get());
            Collections.sort(results);

            return results;

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);

        }
    }
}
