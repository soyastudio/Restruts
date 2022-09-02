package soya.framework.restruts.actions.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.ParameterMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@OperationMapping(domain = "kafka",
        name = "fetch",
        path = "/kafka/fetch",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class FetchAction extends ConsumeAction<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private Integer partition;

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private Long offset;

    @Override
    public String execute() throws Exception {
        long timestamp = System.currentTimeMillis();
        List<ConsumerRecord<String, byte[]>> list = new ArrayList<>();

        KafkaConsumer<String, byte[]> consumer = kafkaClient().consumer();
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        consumer.assign(Collections.singletonList(topicPartition));
        consumer.seek(topicPartition, offset);

        List<ConsumerRecord<String, byte[]>> results = new ArrayList<>();
        while (!isTimeout(timestamp) && results.size() == 0) {
            ConsumerRecords<String, byte[]> records = consumer.poll(pollDuration());
            records.forEach(e -> {
                if (e.offset() == offset) {
                    results.add(e);
                }
            });

            consumer.commitSync();

            if (consumer.position(topicPartition) > offset) {
                break;
            }

        }

        if (results.size() == 0) {
            return null;

        } else {
            ConsumerRecord<String, byte[]> record = results.get(0);

            return new String(record.value());
        }
    }
}
