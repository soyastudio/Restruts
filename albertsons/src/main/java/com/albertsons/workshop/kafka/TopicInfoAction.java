package com.albertsons.workshop.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ActionDefinition(domain = "kafka",
        name = "admin-topic-info",
        path = "/admin/topic",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON)
public class TopicInfoAction extends KafkaAction<PartitionStatus[]> {

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM, required = true)
    private String topic;

    @Override
    public PartitionStatus[] execute() throws Exception {
        KafkaConsumer<String, byte[]> kafkaConsumer = kafkaClient().consumer();

        Map<TopicPartition, PartitionInfo> partitionPartitionInfoMap = new LinkedHashMap<>();
        kafkaConsumer.partitionsFor(topic).forEach(e -> {
            partitionPartitionInfoMap.put(new TopicPartition(e.topic(), e.partition()), e);
        });

        List<TopicPartition> partitions = new ArrayList<>(partitionPartitionInfoMap.keySet());
        kafkaConsumer.assign(partitions);
        Map<TopicPartition, Long> beginOffsets = kafkaConsumer.beginningOffsets(partitions);
        Map<TopicPartition, Long> endOffsets = kafkaConsumer.endOffsets(partitions);

        List<PartitionStatus> list = new ArrayList<>();
        partitionPartitionInfoMap.entrySet().forEach(e -> {
            list.add(new PartitionStatus(e.getValue(), beginOffsets.get(e.getKey()), endOffsets.get(e.getKey())));
        });

        return list.toArray(new PartitionStatus[list.size()]);
    }
}
