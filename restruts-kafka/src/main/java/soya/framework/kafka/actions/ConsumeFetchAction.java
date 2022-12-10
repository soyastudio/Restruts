package soya.framework.kafka.actions;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.ParameterType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ActionDefinition(domain = "kafka",
        name = "consume-fetch",
        path = "/consume/fetch",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ConsumeFetchAction extends ConsumeAction<String> {

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true)
    private Integer partition;

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true)
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
